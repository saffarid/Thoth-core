package thoth_core.thoth_lite.db_data;

import thoth_core.thoth_lite.ThothLite;
import thoth_core.thoth_lite.exceptions.NotContainsException;
import thoth_core.thoth_lite.db_data.tables.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class DBData {

    private static DBData dbData;
    private List<Data> tables;

    private DBData() {
        tables = new LinkedList<>();

        tables.add(new CountTypes());
        tables.add(new ExpensesTypes());
        tables.add(new IncomeTypes());
        tables.add(new OrderStatus());
        tables.add(new ProductTypes());
        tables.add(new Storage());

        tables.add(new Currencies());
        tables.add(new Expenses());
        tables.add(new Incomes());
        tables.add(new NotUsed());
        tables.add(new Orders());
        tables.add(new Partners());
        tables.add(new Products());
        tables.add(new Projects());
        tables.add(new Purchases());

    }

    public static DBData getInstance(){
        if(dbData == null){
            dbData = new DBData();
        }
        return dbData;
    }

    public Data getTable(
            String name
    ) throws NotContainsException {
        Optional<Data> res = tables.stream()
                .filter(data ->  name.startsWith(data.getName()))
                .findFirst();
        if(!res.isPresent()) throw new NotContainsException();
        return res.get();
    }

    public TableReadable getTableReadable(
            String name
    ) throws NotContainsException {
        return getTable(name);
    }



}
