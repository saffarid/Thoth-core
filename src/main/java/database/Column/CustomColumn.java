package database.Column;

public interface CustomColumn
        extends TableColumn
        , ForeignKey
        , NotNull
        , Unique{
}
