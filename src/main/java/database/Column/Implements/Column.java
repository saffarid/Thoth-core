package database.Column.Implements;

import database.Column.DataTypes;
import database.Column.TableColumn;
import database.Exceptions.NotSupportedOperation;
import database.Table;

public abstract class Column
implements TableColumn {

    /**
     * Наименование столбца
     * */
    protected String name;

    /**
     * Тип данных столбца.
     * Переменная должна хранить значение равное ключу в файлах типов колонок.
     * */
    protected DataTypes type;

    /**
     * Таблица, в которую добавляется колонка
     * */
    private Table tableParent;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TableColumn setName(String name) throws NotSupportedOperation {
        this.name = name;
        return this;
    }

    @Override
    public DataTypes getType() {
        return type;
    }

    @Override
    public TableColumn setType(DataTypes type) throws NotSupportedOperation{
        this.type = type;
        return this;
    }

    @Override
    public Table getTable() {
        return tableParent;
    }

    @Override
    public TableColumn setTable(Table table) {
        tableParent = table;
        return this;
    }
}
