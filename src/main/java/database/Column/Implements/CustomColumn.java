package database.Column.Implements;

import database.Column.TableColumn;

public class CustomColumn
    extends Column
        implements database.Column.CustomColumn {

    private final String templateForCreate = "`%1s` %2s%3s";

    /**
     * Уникальность значений в каждой записи данного столбца
     * */
    protected boolean isUnique;

    /**
     * Возможность записи пустых значений
     * */
    protected boolean isNotNull;

    /**
     * Колонка внешней таблицы, на которую ссылается внешний ключ
     * */
    protected TableColumn foreignKey;

    public CustomColumn() {
        name = null;
        type = null;
        isUnique = false;
        isNotNull = false;
        foreignKey = null;
    }

    @Override
    public TableColumn getForeignKey() {
        return foreignKey;
    }

    @Override
    public boolean hasForeignKey() {
        return (foreignKey != null);
    }

    @Override
    public TableColumn setForeignKey(TableColumn fkColumn) {
        this.foreignKey = fkColumn;
        return this;
    }

    @Override
    public boolean isNotNull() {
        return isNotNull;
    }

    @Override
    public TableColumn setNotNull(boolean notNull) {
        this.isNotNull = notNull;
        return this;
    }

    @Override
    public boolean isUnique() {
        return isUnique;
    }

    @Override
    public TableColumn setUnique(boolean unique) {
        this.isUnique = unique;
        return this;
    }

    @Override
    public String comandForCreate() {
        return String.format(templateForCreate
                , name
                , type.getDataType()
                , isNotNull?" not null":""
//                , isUnique?" unique":""
        );
    }
}
