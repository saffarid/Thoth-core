package thoth_core.thoth_lite;

import thoth_core.thoth_lite.config.impl.Config;
import thoth_core.thoth_lite.config.Configuration;
import thoth_core.thoth_lite.config.PeriodAutoupdateDatabase;
import thoth_core.thoth_lite.db_data.DBData;
import thoth_core.thoth_lite.db_data.db_data_element.implement.FinancialOperation;
import thoth_core.thoth_lite.db_data.db_data_element.properties.*;
import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Composite;
import thoth_core.thoth_lite.db_data.tables.Data;
import thoth_core.thoth_lite.db_lite_structure.AvaliableTables;
import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription;
import thoth_core.thoth_lite.exceptions.DontSetSystemInfoException;
import thoth_core.thoth_lite.exceptions.NotContainsException;
import thoth_core.thoth_lite.info.SystemInfoKeys;
import thoth_core.thoth_lite.timer.CheckerFinishable;
import thoth_core.thoth_lite.timer.Traceable;
import thoth_core.thoth_lite.timer.WhatDo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;

public class ThothLite {

    private final String VERSION_CORE = "c1.0";

    /**
     * Карта основных сведений о системе
     */
    private final Map<SystemInfoKeys, String> info = new HashMap<>();
    /**
     * Объект конфигурации системы
     */
    private final Config config = Config.getInstance();
    /**
     * Локальная, считанная, БД
     */
    private final DBData dbData = DBData.getInstance();
    /**
     * Объект-посредник для работы с БД
     */
    private DataBaseLite database;

    private final Accountant accountant;
    private final Runnable reReader = () -> {
        try {
            database.readDataBase();
        } catch (SQLException | ClassNotFoundException e) {
            CoreLogger.log.error(e.getMessage(), e);
        }
    };
    private final Traceable watcherPurchasesFinish = new CheckerFinishable();
    private final Traceable watcherOrdersFinish = new CheckerFinishable();

    private static ThothLite thoth;
    /**
     * Задача перечитывания базы
     */
    private ScheduledFuture<?> scheduledFutureReReadDb;

    private ScheduledThreadPoolExecutor periodReReadDb = new ScheduledThreadPoolExecutor(1);

    private ThothLite(HashMap<SystemInfoKeys, Object> initialData) throws DontSetSystemInfoException {

        CoreLogger.log.info("Init empty local base");
        //Инициализируем объект для работы с БД
        try {
            database = new DataBaseLite();
        } catch (SQLException | ClassNotFoundException e) {
            CoreLogger.log.error(e.getMessage(), e);
        }
        //Читаем единоразовые настройки
        readInfo(initialData);

        //Подписка на покупки
        try {
            DBData.getInstance().getTable(getTableName(AvaliableTables.PURCHASABLE)).subscribe((Flow.Subscriber) watcherPurchasesFinish);
        } catch (NotContainsException e) {
            CoreLogger.log.error(e.getMessage(), e);
        }

        //Считываем содержимое БД
        reReader.run();
        //Запускаем периодическое считывание
        changeDelayReReadDb();

        accountant = new Accountant();

        //Проверяем
        CoreLogger.log.info("Init thoth-core is Done");
    }

    private void readInfo(HashMap<SystemInfoKeys, Object> initialData) {
        try {
            List<HashMap<String, Object>> infoDatas = database.getDataFromTable(StructureDescription.Info.TABLE_NAME);
            for (HashMap<String, Object> row : infoDatas) {
                info.put(
                        SystemInfoKeys.valueOf(String.valueOf(row.get(StructureDescription.Info.ID))),
                        String.valueOf(row.get(StructureDescription.Info.VALUE))
                );
            }

            List<SystemInfoKeys> missingKeys = new LinkedList<>();
            for(SystemInfoKeys key : SystemInfoKeys.values()){
                if(!info.containsKey(key)) missingKeys.add(key);
            }

            if (!missingKeys.isEmpty() && initialData == null) {
                throw new DontSetSystemInfoException(missingKeys);
            }else if(!missingKeys.isEmpty() && !initialData.isEmpty()){
                initSystemCurrency(initialData);
            }

        } catch (SQLException | ClassNotFoundException e) {
            CoreLogger.log.error(e.getMessage(), e);
        }
    }

    /**
     * Функция устанавливает системную валюту при первом запуске
     * */
    private void initSystemCurrency(HashMap<SystemInfoKeys, Object> data) throws SQLException {
        HashMap<String, Object> dataForInfoTable = new HashMap<>();
        dataForInfoTable.put(StructureDescription.Info.ID, SystemInfoKeys.SYSTEM_CURRENCY_CODE.name());
        dataForInfoTable.put(StructureDescription.Info.VALUE, ((Currency)data.get(SystemInfoKeys.SYSTEM_CURRENCY_CODE)).getCurrencyCode());
        List<HashMap<String, Object>> datasForInfo = new LinkedList<>();
        datasForInfo.add(dataForInfoTable);

        HashMap<String, Object> dataForCurrencyTable = new HashMap<>();
        dataForCurrencyTable.put(StructureDescription.Currency.CURRENCY, ((Currency) data.get(SystemInfoKeys.SYSTEM_CURRENCY_CODE)).getCurrencyCode());
        dataForCurrencyTable.put(StructureDescription.Currency.COURSE, 1.0);

        List<HashMap<String, Object>> datasForCurrency = new LinkedList<>();
        datasForCurrency.add(dataForCurrencyTable);

        database.insert(StructureDescription.Info.TABLE_NAME, datasForInfo);
        database.update(StructureDescription.Currency.TABLE_NAME, datasForCurrency);

        readInfo(null);
    }

    public void acceptPurchase(Purchasable purchasable)
            throws NotContainsException {

        /*   Приём покупки включает в себя:
         *  1. Обновление записей в таблице покупок - установка флага isDelivered.
         *  2. Обновление записей в таблице продуктов - обновление кол-ва продукта.
         * */
        //Определим список продуктов и обновим их кол-во.
        List<Storagable> listStoragable = new LinkedList<>();
        ;
        for (Storing storing : purchasable.getComposition()) {
            Storagable storagable = storing.getStoragable();
            /*--- При обновлении цены, необходимо проверять совпадение цены в БД с считанными данными. ---*/
            storagable.setCount(storagable.getCount() + storing.getCount());
            listStoragable.add(storagable);
        }

        //Упакуем покупку в список для дальнейшей обработки
        List<Purchasable> purchase = new LinkedList<>();
        purchase.add(purchasable);

        try {
            //Для атомарности операции стартуем транзакцию
            database.beginTransaction();
            updateInTable(getTableName(AvaliableTables.PURCHASABLE), purchase);
            updateInTable(getTableName(AvaliableTables.STORAGABLE), listStoragable);
            database.commitTransaction();
        } catch (SQLException e) {
            CoreLogger.log.error(e.getMessage(), e);
            database.rollbackTransaction();
        }

    }

    /**
     * Отмена периодического перечитывания базы
     */
    public void cancelAutoReReadDb() {
        if (scheduledFutureReReadDb == null) return;
        CoreLogger.log.info("Reread database cancel");
        scheduledFutureReReadDb.cancel(false);
    }

    /**
     * Изменение периода перечитывания базы
     */
    public void changeDelayReReadDb() {
        PeriodAutoupdateDatabase newDelay = config.getDatabase().getPeriod();
        if (config.getDatabase().isAutoupdate()) {
            cancelAutoReReadDb();
            if (newDelay != PeriodAutoupdateDatabase.NEVER) {
                scheduledFutureReReadDb = periodReReadDb.scheduleWithFixedDelay(reReader, newDelay.getValue(), newDelay.getValue(), TimeUnit.MINUTES);
            }
        }
    }

    /**
     * Функция завершает выполнение всех процессов
     */
    public void close() {
        database.close();
        config.exportConfig();
        CoreLogger.log.info("Good bye, my friend. I will miss you.");
    }

    public void forceRereadDatabase()
            throws SQLException, ClassNotFoundException {
        reReader.run();
    }

    /**
     * Функция возвращает конфигурацию системы в формате json.
     */
    public Configuration getConfig() {
        return config;
    }

    /**
     * @param table запрашиваемая таблица.
     * @return список содержимого таблицы.
     */
    public List<? extends Identifiable> getDataFromTable(AvaliableTables table)
            throws NotContainsException {
        return dbData.getTable(getTableName(table)).getDatas();
    }

    /**
     * @param key ключ доступа к информационному полю
     * @throws NotContainsException
     * @return информационное поле
     * */
    public Object getInfoField(SystemInfoKeys key) throws NotContainsException{
        if (!info.containsKey(key)) throw new NotContainsException();
        return info.get(key);
    }

    /**
     * Функция создает объект системы с настройками локали по-умолчанию
     *
     * @throws DontSetSystemInfoException
     */
    public static ThothLite getInstance() throws DontSetSystemInfoException {
        if (thoth == null) {
            thoth = new ThothLite(null);
        }
        return thoth;
    }

    /**
     * Функция создает объект системы с заданными настройками локали
     */
    public static ThothLite getInstance(HashMap<SystemInfoKeys, Object> initialData) {
        if (thoth == null) {
            thoth = new ThothLite(initialData);
        }
        return thoth;
    }

    public String getTableName(AvaliableTables table) {
        switch (table) {
            case COUNT_TYPES: {
                return StructureDescription.CountTypes.TABLE_NAME;
            }
            case EXPENSES_TYPES: {
                return StructureDescription.ExpensesTypes.TABLE_NAME;
            }
            case INCOMES_TYPES: {
                return StructureDescription.IncomesTypes.TABLE_NAME;
            }
            case ORDER_STATUS: {
                return StructureDescription.OrderStatus.TABLE_NAME;
            }
            case PRODUCT_TYPES: {
                return StructureDescription.ProductTypes.TABLE_NAME;
            }
            case STORING: {
                return StructureDescription.Storage.TABLE_NAME;
            }

            case CURRENCIES: {
                return StructureDescription.Currency.TABLE_NAME;
            }
            case EXPENSES: {
                return StructureDescription.Expenses.TABLE_NAME;
            }
            case INCOMES: {
                return StructureDescription.Incomes.TABLE_NAME;
            }
            case ORDERABLE: {
                return StructureDescription.Orders.TABLE_NAME;
            }
            case PARTNERS: {
                return StructureDescription.Partners.TABLE_NAME;
            }
            case STORAGABLE: {
                return StructureDescription.Products.TABLE_NAME;
            }
            case PROJECTABLE: {
                return StructureDescription.Project.NAME;
            }
            case PURCHASABLE: {
                return StructureDescription.Purchase.NAME;
            }
            default:
                return null;
        }
    }

    public String getVersion(){
        return new StringBuilder(VERSION_CORE)
                .append("_")
                .append(info.get(SystemInfoKeys.VERSION_DB))
                .toString();
    }

    public void insertToTable(AvaliableTables table, List<? extends Identifiable> datas)
            throws NotContainsException, ClassNotFoundException {
        String tableName = getTableName(table);
        try {
            database.beginTransaction();
            insertToTable(tableName, datas);
            database.commitTransaction();
        } catch (SQLException exception) {
            database.rollbackTransaction();
        } finally {
            try {
                database.readDataBase();
            } catch (SQLException exception) {
                CoreLogger.log.error(exception.getMessage(), exception);
            }
        }
    }

    /**
     * Вставка новых записей в таблицы БД.
     * Отличие от public функции заключается в начале и закреплении транзакции.
     *
     * @param tableName наименование таблицы для вставки новых записей.
     * @param datas     добавляемые данные.
     */
    private void insertToTable(String tableName, List<? extends Identifiable> datas)
            throws SQLException, NotContainsException {

        Data table = dbData.getTable(tableName);
        Data products = dbData.getTable(StructureDescription.Products.TABLE_NAME);

        List<Storagable> datasProducts = new LinkedList<>(); //Список продуктов, входящих в состав
        List<FinancialAccounting> finOps = new LinkedList<>(); //Список финансовых операций

        for (Identifiable data : datas) {
            // Проверяем исходный объект на реализацию Composite
            if (!(data instanceof Composite)) continue;

            Composite composite = (Composite) data;
            //Запрашиваем состав
            //Проходим по составу и добавляем каждую запись в список
            for (Storing storing : composite.getComposition()) {
                Storagable storagable = storing.getStoragable(); //Продукт
                finOps.add(
                        new FinancialOperation(
                                "-1", storagable.getType(), storing.getCount(), LocalDate.now(), storing.getCurrency(), storing.getCurrency().getCourse(), ""
                        )
                );

                if (!products.contains(storagable)) {
                    datasProducts.add(storagable);
                }
            }
        }

        /* Добавляем записи в таблицу продуктов в случае
          если исходный объект был составным и есть что записывать */
        if (!datasProducts.isEmpty()) {
            Map<String, List<HashMap<String, Object>>> compositeData = products.convertToMap(datasProducts);
            for (String tableForInsert : compositeData.keySet()) {
                database.insert(tableForInsert, compositeData.get(tableForInsert));
            }
        }

        // Добавляем записи исходного объекта в таблицу
        Map<String, List<HashMap<String, Object>>> data = table.convertToMap(datas);
        for (String tableForInsert : data.keySet()) {
            database.insert(tableForInsert, data.get(tableForInsert));
        }

        // Если исходный объект instanceof Purchasable - добавляем записи в таблицу расходов
        if (data instanceof Purchasable) {
            Map<String, List<HashMap<String, Object>>> hashMap = DBData.getInstance().getTable(StructureDescription.Expenses.TABLE_NAME).convertToMap(finOps);
            for (String key : hashMap.keySet()) {
                database.insert(
                        StructureDescription.Expenses.TABLE_NAME,
                        hashMap.get(key)
                );
            }
        }

        // Блок работает только тогда, когда добавляются записи в PRODUCT_TYPES
        if (tableName.equals(StructureDescription.ProductTypes.TABLE_NAME)) {
            Map<String, List<HashMap<String, Object>>> expTypes = DBData.getInstance().getTable(StructureDescription.ExpensesTypes.TABLE_NAME).convertToMap(datas);
            Map<String, List<HashMap<String, Object>>> incTypes = DBData.getInstance().getTable(StructureDescription.IncomesTypes.TABLE_NAME).convertToMap(datas);

            for (String tableForInsert : expTypes.keySet()) {
                database.insert(tableForInsert, expTypes.get(tableForInsert));
            }
            for (String tableForInsert : incTypes.keySet()) {
                database.insert(tableForInsert, incTypes.get(tableForInsert));
            }
        }
    }

    /**
     * Подписка на публикацию наступления плановой даты завершения заказа
     */
    public void orderSubscribe(Flow.Subscriber<HashMap<WhatDo, Finishable>> subscriber) {
        watcherOrdersFinish.subscribe(subscriber);
    }

    public void purchaseComplete(String purchaseId)
            throws NotContainsException, SQLException {

        Data purchasesTable = dbData.getTable(StructureDescription.Purchase.NAME);

        List<Purchasable> purchasableList = new LinkedList<>();
        purchasableList.add((Purchasable) purchasesTable.getById(purchaseId));

        Map<String, List<HashMap<String, Object>>> datas = purchasesTable.convertToMap(purchasableList);
        for (String tableName : datas.keySet()) {
            database.update(tableName, datas.get(tableName));
        }

        /*
         * if (продукт НЕ ХРАНИТСЯ в считанной БД и в SQLite){
         *   Вставляем новые записи в БД
         * }else{
         *   Обновляем существующие записи
         * }
         * */

    }

    /**
     * Подписка на публикацию наступления даты получения покупки
     */
    public void purchasesSubscribe(Flow.Subscriber<HashMap<WhatDo, Finishable>> subscriber) {
        watcherPurchasesFinish.subscribe(subscriber);
    }

    /**
     * Функция удаляет данные из таблицы.
     *
     * @param table таблица из которой удаляются записи.
     * @param datas удаляемые записи.
     */
    public void removeFromTable(AvaliableTables table, List<? extends Identifiable> datas)
            throws NotContainsException, ClassNotFoundException {
        String tableName = getTableName(table);
        try {
            database.beginTransaction();
            removeFromTable(tableName, datas);
            database.commitTransaction();
        } catch (SQLException exception) {
            database.rollbackTransaction();
        } finally {
            try {
                database.readTable(tableName);
            } catch (SQLException exception) {
                CoreLogger.log.error(exception.getMessage(), exception);
            }
        }
    }

    /**
     * Функция удаляет данные из таблицы.
     * Отличие от public функции заключается в начале и закреплении транзакции.
     *
     * @param tableName наименование таблицы из которой удаляются записи.
     * @param datas     удаляемые записи.
     */
    private void removeFromTable(String tableName, List<? extends Identifiable> datas)
            throws SQLException, NotContainsException {
        Map<String, List<HashMap<String, Object>>> data = dbData.getTable(tableName).convertToMap(datas);
        for (String name : data.keySet()) {
            database.remove(name, data.get(name));
        }
    }

    /**
     * Функция подписывает на изменения в таблице
     */
    public void subscribeOnTable(
            AvaliableTables table
            , Flow.Subscriber subscriber
    ) throws NotContainsException {
        CoreLogger.log.info("Subscribed to " + table);
        DBData.getInstance().getTable(getTableName(table)).subscribe(subscriber);
    }

    public void updateInTable(AvaliableTables table, List<? extends Identifiable> datas)
            throws NotContainsException {
        try {
            database.beginTransaction();
            updateInTable(getTableName(table), datas);
            database.commitTransaction();
        } catch (SQLException exception) {
            database.rollbackTransaction();
        } finally {
            try {
                database.readTable(getTableName(table));
            } catch (SQLException | ClassNotFoundException exception) {
            }
        }
    }

    /**
     * Функция обновляет записи в таблице.
     * Отличие от public функции заключается в начале и закреплении транзакции.
     *
     * @param tableName наименование таблицы в которой обновляются записи.
     * @param datas     обновлённые записи.
     */
    private void updateInTable(String tableName, List<? extends Identifiable> datas)
            throws SQLException, NotContainsException {
        Map<String, List<HashMap<String, Object>>> data = dbData.getTable(tableName).convertToMap(datas);
        for (String name : data.keySet()) {
            database.update(name, data.get(name));
        }
    }

}
