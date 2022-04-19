package database.Column;

public interface ForeignKey extends TableColumn{

    TableColumn getForeignKey();
    boolean hasForeignKey();
    TableColumn setForeignKey(TableColumn fkColumn);

}
