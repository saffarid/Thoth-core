package database.Column;

public enum DataTypes {

    ID("integer"),
    NOTE("varchar(255)"),
    TEXT("text"),
    INT("integer"),
    DOUBLE("double"),
    BOOL("bool");

    private String dataType;

    DataTypes(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }
}
