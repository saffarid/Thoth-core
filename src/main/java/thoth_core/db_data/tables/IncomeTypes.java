package thoth_core.db_data.tables;

import database.Column.TableColumn;
import thoth_core.db_lite_structure.StructureDescription;
import thoth_core.db_data.db_data_element.implement.ListElement;
import thoth_core.db_data.db_data_element.properties.Typable;
import thoth_core.db_data.db_data_element.properties.Identifiable;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IncomeTypes
        extends Data<Typable> {

    public IncomeTypes() {
        super();
        setName(StructureDescription.IncomesTypes.TABLE_NAME);
    }

    @Override
    public Map< String, List< HashMap<String, Object> > > convertToMap(List<? extends Identifiable> list) {
        HashMap< String, List< HashMap<String, Object> > > res = new HashMap<>();

        List<HashMap<String, Object>> datas = new LinkedList<>();

        for (Identifiable identifiable : list) {
            HashMap<String, Object> map = new HashMap<>();
            Typable typable = (Typable) identifiable;
            map.put(StructureDescription.IncomesTypes.INCOMES_TYPE, typable.getValue());
            datas.add(map);
        }
        res.put(StructureDescription.IncomesTypes.TABLE_NAME, datas);

        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) {
        datas.clear();
        for (HashMap<String, Object> row : data) {
            datas.add(
                    new ListElement(
                            String.valueOf(row.get(StructureDescription.IncomesTypes.ID)),
                            (String) row.get(StructureDescription.IncomesTypes.INCOMES_TYPE)
                    )
            );
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
