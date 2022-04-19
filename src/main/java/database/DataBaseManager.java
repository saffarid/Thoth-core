package database;

import database.Column.TableColumn;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class DataBaseManager {

    private static Logger LOG;
    private static DataBaseManager dbManager;
    private Map<File, Connection> conns;

    static {
        LOG = Logger.getLogger(DataBaseManager.class.getName());
    }

    public DataBaseManager() throws SQLException, ClassNotFoundException {
        conns = new HashMap<>();
    }

    public static DataBaseManager getDbManager() throws SQLException, ClassNotFoundException {
        if (dbManager == null) {
            dbManager = new DataBaseManager();
        }
        return dbManager;
    }

    public void alterTable(Table oldTable,
                           Table newTable,
                           File db) {
        /*
         * Последовательно определяем все изменения внесённые в таблицу:
         * 1. Определяем изменение наименования таблицы
         *   1.1. Если наименование изменено, меняем информацию в таблицах tables list. За счет внешнего ключа ссылка
         *        на наименование таблицы всегда будет актуальна.
         *
         * */
    }

    /**
     * Функция инициирует начало транцакции
     * */
    public void beginTransaction(File db)
            throws SQLException {
        if(conns.containsKey(db)) {
            DataBaseWrapper.beginTransaction(conns.get(db), null);
        }
    }

    /**
     * Функция закрывает соединение с БД
     */
    public void closeConnection(File db)
            throws SQLException {
        if (conns.containsKey(db)) {
            conns.get(db).close();
            conns.remove(db);
        }
    }

    /**
     * Функция удаляет все записи из таблицы
     */
    public void clear(String tableName)
            throws SQLException {
    }

    /**
     * Завершение транзакции
     * */
    public void commitTransaction(File db)
            throws SQLException {
        if(conns.containsKey(db)){
            DataBaseWrapper.commitTransaction(conns.get(db), null);
        }
    }

    /**
     * Функция создает новую БД
     */
    public void createDatabase(File db) throws SQLException, ClassNotFoundException {
        getConnection(db);
    }

    /**
     * Функция отвечает за создание новой таблицы в БД
     */
    public void createTable(Table table, File db) throws SQLException {
        DataBaseWrapper.createTable(table, table.getColumns(), conns.get(db));
    }

    /**
     * Функция возвращает соединение с БД
     */
    public Connection getConnection(File db) throws SQLException, ClassNotFoundException {
        if (!conns.containsKey(db)) {
            openConnection(db);
        }
        return conns.get(db);
    }

    /**
     * Функция возвращает список созданных таблиц
     *
     * @param db    файл пользовательской БД
     * @param table таблица, с которой производится считывание информации
     */
    public List<HashMap<String, Object>> getDataTable(File db,
                                                      Table table,
                                                      boolean useSubRequest)
            throws SQLException, ClassNotFoundException {
        List<HashMap<String, Object>> res = new LinkedList<>();
        ResultSet select = DataBaseWrapper.select(
                table, null, null, getConnection(db), useSubRequest
        );
        while (select.next()) {
            HashMap<String, Object> contentValues = new HashMap<>();
            for (int i = 1; i < select.getMetaData().getColumnCount() + 1; i++) {
                contentValues.put(
                        select.getMetaData().getColumnName(i),
                        select.getObject(i)
                );
            }
            res.add(contentValues);
        }
        return res;
    }

    /**
     * Функция отвечает за вставку записей в таблицу БД
     *
     * @param tableName     наименование таблицы для вставки записей
     * @param contentValues Объект вставляемых данных
     */
    public void insert(Table tableName,
                       ContentValues contentValues,
                       File db)
            throws SQLException {
        DataBaseWrapper.insert(tableName, contentValues, conns.get(db));
    }

    /**
     * Функция открывает соединение с БД/
     *
     * @param db Путь до базы данных
     */
    private void openConnection(File db) throws SQLException, ClassNotFoundException {
        conns.put(db, DataBaseWrapper.openConnetion(db));
    }

    /**
     * Функция отвечает за переименовывание колонок в таблице
     */
    public void renameColumn(String tableName,
                             TableColumn oldTableColumn,
                             TableColumn newTableColumn) throws SQLException {
    }

    /**
     * Функция отвечает за удаление колонки из таблицы
     */
    public void removeColumn(String tableName,
                             TableColumn tableColumn) throws SQLException {
    }

    /**
     * Функция отвечает за удаление записи из таблицы БД
     */
    public void removedRow(Table table,
                           WhereValues whereValues,
                           File db)
            throws SQLException {
        DataBaseWrapper.delete(conns.get(db), table, whereValues);
    }

    /**
     * Функция отвечает за удаление таблицы
     */
    public void removeTables(Table table,
                             File dbFile) throws SQLException, ClassNotFoundException {
        DataBaseWrapper.dropTable(table, getConnection(dbFile));
    }

    /**
     * Функция отвечает за переименование таблицы
     */
    public void renameTable(String oldName,
                            String newName) throws SQLException {

    }

    /**
     * Функция отвечает за перемещение компонента из одной таблицы в другую
     */
    public void replace(String fromTable,
                        String toTable,
                        ContentValues contentValues,
                        WhereValues whereValues) throws SQLException {
    }

    /**
     * Функция отменяет транзакцию
     * */
    public void rollbackTransaction(
            File db
    ){
        try {
            DataBaseWrapper.rollbackTransaction(conns.get(db), null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Функция возвращает выборку строк из таблицы
     */
    public List<LinkedHashMap<TableColumn, Object>> select(Table table,
                                                           List<TableColumn> columns,
                                                           WhereValues where,
                                                           File db) throws SQLException {
        ResultSet select = DataBaseWrapper.select(table, columns, where, conns.get(db), false);
        List<LinkedHashMap<TableColumn, Object>> res = new LinkedList<>();
        while (select.next()) {
            LinkedHashMap<TableColumn, Object> row = new LinkedHashMap<>();
            for (TableColumn tableColumn : columns) {
                row.put(tableColumn, select.getObject(tableColumn.getName()));
            }
            res.add(row);
        }
        return res;
    }

    /**
     * Функция отвечает за обновление щаписи в таблице
     */
    public void update(Table table,
                       ContentValues contentValues,
                       WhereValues whereValues,
                       File db)
            throws SQLException {
        DataBaseWrapper.update(table, contentValues, whereValues, conns.get(db));
    }
}
