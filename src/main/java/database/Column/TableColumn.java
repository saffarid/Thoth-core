package database.Column;

import database.Column.Implements.CustomColumn;
import database.Column.Implements.PrimaryKeyAutoncrement;
import database.Column.Implements.PrimaryKeyCustom;
import database.Exceptions.NotSupportedOperation;
import database.Table;

public interface TableColumn {

    String getName();
    TableColumn setName(String name) throws NotSupportedOperation;

    DataTypes getType();
    TableColumn setType(DataTypes type) throws NotSupportedOperation;

    Table getTable();
    TableColumn setTable(Table table);

    String comandForCreate();

     static TableColumn getInstance(ColumnTypes type){
        switch (type){
            case CUSTOM_COLUMN:{
                return new CustomColumn();
            }
            case PRIMARYKEY_CUSTOM:{
                return new PrimaryKeyCustom();
            }
            case PRIMARYKEY_AUTOINCREMENT:{
                return new PrimaryKeyAutoncrement();
            }
            default:{
                return null;
            }
        }
    }
}
