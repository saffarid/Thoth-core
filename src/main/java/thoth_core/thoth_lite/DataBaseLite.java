package thoth_core.thoth_lite;

import database.Column.Autoincrement;
import database.Column.PrimaryKey;
import database.Column.TableColumn;
import database.ContentValues;
import database.DataBaseManager;
import database.Table;
import database.WhereValues;
import thoth_core.thoth_lite.db_data.DBData;
import thoth_core.thoth_lite.db_lite_structure.full_structure.DBLiteStructure;
import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription.TableTypes;
import thoth_core.thoth_lite.exceptions.NotContainsException;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class DataBaseLite {

    private static Logger LOG;

    private final String URL_DB = "db/storage.tho";
    private final File dbFile;

    private DBLiteStructure structure = new DBLiteStructure();
    private DataBaseManager dbManager = DataBaseManager.getDbManager();

    static {
        LOG = Logger.getLogger(DBLiteStructure.class.getName());
    }

    public DataBaseLite()
            throws SQLException, ClassNotFoundException {

        dbFile = new File(URL_DB);
        if (!this.dbFile.exists()) {
            firstInit();
        }

    }

    /**
     * Первичная инициализация БД
     */
    private void firstInit()
            throws SQLException, ClassNotFoundException {
        CoreLogger.log.info("Create database");
        dbManager.createDatabase(this.dbFile);

        CoreLogger.log.info("Add table in database");
        for (Table table : structure.getTables()) {
            dbManager.createTable(table, this.dbFile);

            if (!table.getContentValues().isEmpty()) {
                for (ContentValues values : table.getContentValues())
                    dbManager.insert(
                            table,
                            values,
                            dbFile
                    );
            }
        }
        CoreLogger.log.info("Create database is done");
    }

    /**
     * Инициализация начала транзакции
     */
    public void beginTransaction()
            throws SQLException {
        CoreLogger.log.info("Begin transaction");
        dbManager.beginTransaction(dbFile);
    }

    public void close() {
        try {
            dbManager.closeConnection(dbFile);
        } catch (SQLException e) {
            CoreLogger.log.error("Close connection error ", e);
        }
    }

    public void commitTransaction()
            throws SQLException {
        CoreLogger.log.info("Commit transaction");
        dbManager.commitTransaction(dbFile);
    }

    private ContentValues convertToContentValues(String tableName, HashMap<String, Object> data) {
        CoreLogger.log.info("Convert to ContentValues");
        ContentValues contentValues = new ContentValues();

        for (String columnName : data.keySet()) {

            TableColumn columnByName = structure.getTable(tableName).getColumnByName(columnName);
            //Исключает колонки с автоинкрементируемым индексом
            if (!(columnByName instanceof Autoincrement)) {
                contentValues.put(columnByName, data.get(columnName));
            }
        }
        CoreLogger.log.info("Convert to ContentValues is Done");
        return contentValues;
    }

    private WhereValues convertToWhereValues(String tableName, HashMap<String, Object> data) {
        CoreLogger.log.info("Convert to WhereValues");
        WhereValues whereValues = new WhereValues();

        PrimaryKey primaryKeyColumn = structure.getTable(tableName).getPrimaryKeyColumn();
        whereValues.put(
                primaryKeyColumn, data.get(primaryKeyColumn.getName())
        );
        CoreLogger.log.info("Convert to WhereValues is Done");
        return whereValues;
    }

    public List<HashMap<String, Object>> getDataFromTable(String tableName)
            throws SQLException, ClassNotFoundException {
        return getDataFromTable(structure.getTable(tableName));
    }

    public List<HashMap<String, Object>> getDataFromTable(Table tableName)
            throws SQLException, ClassNotFoundException {
        return dbManager.getDataTable(dbFile, tableName, false);
    }

    public List<Table> getTables() {
        return structure.getTables();
    }

    public List<Table> getTablesByType(TableTypes type) {
        return structure.getTables().stream()
                .filter(table -> table.getType().equals(type))
                .collect(Collectors.toList());
    }

    public void insert(String tableName, List<HashMap<String, Object>> datas)
            throws SQLException {
        CoreLogger.log.info("Insert into table " + tableName);
        for (HashMap<String, Object> data : datas) {
            dbManager.insert(
                    structure.getTable(tableName),
                    convertToContentValues(tableName, data),
                    dbFile
            );
        }
        CoreLogger.log.info("Insert into table " + tableName + " is Done");
    }

    /**
     * Чтение содержимого таблиц БД
     */
    public void readDataBase()
            throws SQLException, ClassNotFoundException {
        CoreLogger.log.info("Read database");
        List<Table> collect = structure.getTables()
                .stream()
                .filter(table -> !table.getType().equals(TableTypes.SYSTEM_TABLE.getType()))
                .collect(Collectors.toList());
        for (Table table : collect) {
            setDataTable(
                    table,
                    getDataFromTable(table)
            );
        }
        CoreLogger.log.info("Read database is Done");
    }

    /**
     * Чтение содержимого таблицы
     */
    public void readTable(String table)
            throws SQLException, ClassNotFoundException {
        Table readingTable = structure.getTable(table);

        CompletableFuture.supplyAsync(() -> {
            List<HashMap<String, Object>> dataTable = new LinkedList<>();
            try {
                dataTable = getDataFromTable(readingTable);
            } catch (SQLException | ClassNotFoundException e) {
                CoreLogger.log.error(e.getMessage(), e);
            }
            return dataTable;
        }).thenAccept(hashMaps -> {
            setDataTable(readingTable, hashMaps);
        });
    }

    /**
     * Функция отменяет транзакцию
     */
    public void rollbackTransaction() {
        dbManager.rollbackTransaction(dbFile);
    }

    /**
     * Функция удаляет записи из таблицы.
     *
     * @param tableName имя таблицы.
     * @param datas     удаляемые данные.
     */
    public void remove(String tableName, List<HashMap<String, Object>> datas)
            throws SQLException {
        CoreLogger.log.info("Remove from table " + tableName);
        for (HashMap<String, Object> data : datas) {
            dbManager.removedRow(
                    structure.getTable(tableName)
                    , convertToWhereValues(tableName, data)
                    , dbFile
            );
        }
        CoreLogger.log.info("Remove from table " + tableName + " is Done");
    }

    /**
     * Чтение содержимого таблицы
     */
    private void setDataTable(Table table, List<HashMap<String, Object>> data) {
        CoreLogger.log.info("Read table " + table.getName());
        try {
            TableTypes tableType = TableTypes.valueOf(table.getType());
            DBData.getInstance()
                    .getTableReadable(table.getName())
                    .readTable(tableType, data);
            CoreLogger.log.info("Read table " + table.getName() + " is Done");
        } catch (ParseException e) {
            CoreLogger.log.error("Parse table" + table.getName() + " error:", e);
        } catch (NotContainsException e) {
            CoreLogger.log.error("Not Contains in table" + table.getName() + " error:", e);
        }
    }

    public void update(String tableName, List<HashMap<String, Object>> datas)
            throws SQLException {
        CoreLogger.log.info("Update table " + tableName);
        for (HashMap<String, Object> data : datas) {
            dbManager.update(
                    structure.getTable(tableName)
                    , convertToContentValues(tableName, data)
                    , convertToWhereValues(tableName, data)
                    , dbFile
            );
        }
        CoreLogger.log.info("Update table " + tableName + " is Done");
    }

}
