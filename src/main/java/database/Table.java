package database;

import java.util.*;

import database.Column.*;
import database.Column.TableColumn;

/**
 * Шаблон описания создаваемой/считываемой таблицы
 */
public class Table {

    private final String UNIQ_CONSTR = "constraint `%1s_%2s_uniq` unique(%3s)";
    private final String FK_CONSTR = "constraint `%1s_%2s_fk` foreign key (%3s) references `%4s` (%5s)";

    /**
     * Наименование таблицы
     */
    protected String name;

    /**
     * Тип таблицы
     */
    protected String type;

    /**
     * Содержимое таблицы
     */
    protected List<ContentValues> contentValues;

    /**
     * Список столбцов
     */
    protected List<TableColumn> columns;

    /**
     * Список столбцов для ограничения UNIQUE
     */
    protected List<TableColumn> uniqueColumns;

    /**
     * Список столбцов для ограничения NOT NULL
     */
    protected List<TableColumn> notNullColumns;

    /**
     * Список для ограничения FOREIGN KEY
     * Ключом является колонка текущей таблицы, значение - внешняя таблица
     */
    protected Map<TableColumn, TableColumn> foreignKeysColumns;

    public Table() {
        columns = new LinkedList<>();
        notNullColumns = new LinkedList<>();
        uniqueColumns = new LinkedList<>();
        foreignKeysColumns = new HashMap<>();
        contentValues = new LinkedList<>();
    }

    /**
     * Функция отвечает за добавление новой колонки в описание таблицы.
     *
     * @param column новая колонка.
     * @return true - если колонка добавлена.
     */
    public boolean addColumn(TableColumn column) {
        boolean res = false;
        boolean colPresent = columns
                .stream()
                .filter(column1 -> column1.getName().equals(column.getName()))
                .findAny()
                .isPresent();
        if (!colPresent) {
            columns.add(column);
            column.setTable(this);
            res = true;
        }

        if (column instanceof NotNull) {
            if (((NotNull) column).isNotNull() && !notNullColumns.contains(column)) notNullColumns.add(column);
        }
        if (column instanceof Unique) {
            if (((Unique) column).isUnique() && !uniqueColumns.contains(column)) uniqueColumns.add(column);
        }
        if (column instanceof ForeignKey) {
            if (((ForeignKey) column).getForeignKey() != null)
                foreignKeysColumns.put(column, ((ForeignKey) column).getForeignKey());
        }

        return res;
    }

    /**
     * Функция копирует в текущую таблицу информацию из переданной таблицы.
     *
     * @param copyTable копируемая таблица
     */
    public Table copy(Table copyTable) {
        this.name = copyTable.getName();
        this.type = copyTable.getType();

        for (TableColumn column : copyTable.getColumns()) {
            addColumn(column);
        }

        for (ContentValues row : copyTable.getContentValues()) {
            LinkedList<TableColumn> tableColumns = new LinkedList<TableColumn>(row.keySet());
            ContentValues copiedRow = new ContentValues();

            for (TableColumn tableColumn : tableColumns) {
                copiedRow.put(getColumnByName(tableColumn.getName()), row.get(tableColumn));
            }

            contentValues.add(copiedRow);
        }

        return this;
    }

    public List<TableColumn> getColumns() {
        return columns;
    }

    public String getConstrainsForeignKey() {
        StringBuilder res = new StringBuilder("");
        if (!foreignKeysColumns.keySet().isEmpty()) {

            ArrayList<TableColumn> tableColumns = new ArrayList(foreignKeysColumns.keySet());

            for (TableColumn column : tableColumns) {
                Table foreignKeyTable = foreignKeysColumns.get(column).getTable();
                // Определяем наименование колонки первичного ключа внешней таблицы
                String foreignKeyPrimaryKey = foreignKeyTable.getPrimaryKeyColumn().getName();

                res.append(String.format(
                        FK_CONSTR,
                        name,                                   //1
                        foreignKeyTable.getName(),              //2
                        "`" + column.getName() + "`",           //3
                        foreignKeyTable.getName(),              //4
                        "`" + foreignKeyPrimaryKey + "`"        //5
                        )
                );

                if (tableColumns.indexOf(column) != tableColumns.size() - 1) {
                    res.append(", \n\t");
                }
            }
        }
        return res.toString().trim();
    }

    /**
     * @return строка со всеми именовынными ограничениями по уникальности
     * */
    public String getConstrainsUnique() {
        StringBuilder res = new StringBuilder("");
        for (TableColumn column : uniqueColumns) {
            res.append(String.format(UNIQ_CONSTR, this.name, column.getName().trim(), "`" + column.getName().trim() + "`"));
            if (uniqueColumns.indexOf(column) != uniqueColumns.size() - 1) {
                res.append(", \n\t");
            }
        }
        return res.toString().trim();
    }

    public List<ContentValues> getContentValues() {
        return contentValues;
    }

    public String getName() {
        return name;
    }

    /**
     * @return колонка - первичный ключ.
     */
    public PrimaryKey getPrimaryKeyColumn() {
        return (PrimaryKey) (columns.stream().filter(column -> column instanceof PrimaryKey).findFirst().get());
    }

    /**
     * Функция ищет колонку по её наименованию
     */
    public TableColumn getColumnByName(String colName) {
        return getColumnByName(this, colName);
    }

    public TableColumn getColumnByName(Table table, String colName) {
        Optional<TableColumn> first = table.getColumns()
                .stream()
                .filter(column -> column.getName().equals(colName))
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            return null;
        }
    }

    public String getType() {
        return type;
    }

    public boolean hasForeignKeys() {
        return !foreignKeysColumns.isEmpty();
    }

    public boolean hasUniques() {
        return !uniqueColumns.isEmpty();
    }

    public boolean removeColumn(TableColumn column) {
        boolean res = false;

        if (columns.contains(column)) {
            columns.remove(column);
            res = true;
        }

        if (column instanceof NotNull) {
            if (((NotNull) column).isNotNull() && notNullColumns.contains(column)) notNullColumns.remove(column);
        }
        if (column instanceof Unique) {
            if (((Unique) column).isUnique() && uniqueColumns.contains(column)) uniqueColumns.remove(column);
        }

        return res;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
