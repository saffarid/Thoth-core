package database;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class DataBaseSQL {

    /**
     * Список таблиц
     * */
    protected List<Table> tables;

    public DataBaseSQL() {
        tables = new LinkedList<>();
    }

    /**
     * @param name наименование таблицы.
     * @return Объект класса Table по переданному наименованию таблицы, если объект не найдет возвращается null.
     * @see Table
     * */
    public Table getTable(String name){
        Optional<Table> first = tables
                .stream()
                .filter(table -> table.getName().equals(name))
                .findFirst();
        if(first.isPresent()) return first.get();
        else return null;
    }

    public List<Table> getTables() {
        return tables;
    }
}
