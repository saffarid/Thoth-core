package thoth_core.db_data.tables;

import database.Column.TableColumn;
import thoth_core.db_lite_structure.StructureDescription;
import thoth_core.db_data.db_data_element.implement.Currency;
import thoth_core.db_data.db_data_element.properties.Finance;
import thoth_core.db_data.db_data_element.properties.Identifiable;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Currencies
        extends Data<Finance> {

    public Currencies() {
        super();
        setName(StructureDescription.Currency.TABLE_NAME);
    }

    @Override
    public Map< String, List< HashMap<String, Object> > > convertToMap(List<? extends Identifiable> list) {

        HashMap< String, List< HashMap<String, Object> > > res = new HashMap<>();

        List<HashMap<String, Object>> datas = new LinkedList<>();

        for (Identifiable identifiable : list) {
            HashMap<String, Object> map = new HashMap<>();
            Finance currency = (Finance) identifiable;
            map.put(StructureDescription.Currency.CURRENCY, currency.getCurrency().getCurrencyCode());
            map.put(StructureDescription.Currency.COURSE, currency.getCourse());
            datas.add(map);
        }

        res.put(StructureDescription.Currency.TABLE_NAME, datas);

        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) {
        datas.clear();
        for (HashMap<String, Object> row : data) {
            datas.add(new Currency(
                    (String) row.get(StructureDescription.Currency.CURRENCY),
                    (Double) row.get(StructureDescription.Currency.COURSE)
            ));
        }
        publisher.submit(datas);
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, ResultSet resultSet) {

    }

    @Override
    public void readTableWithTableColumn(StructureDescription.TableTypes tableType, List<HashMap<TableColumn, Object>> data) {

    }
}
