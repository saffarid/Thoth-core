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

public class CountTypes
        extends Data<Typable>
        implements TableReadable {

    public CountTypes() {
        super();
        setName(StructureDescription.CountTypes.TABLE_NAME);
    }

    @Override
    public Map< String, List< HashMap<String, Object> > > convertToMap(List<? extends Identifiable> identifiable) {

        HashMap< String, List< HashMap<String, Object> > > res = new HashMap<>();

        List<HashMap<String, Object>> datas = new LinkedList<>();

        for (Identifiable data : identifiable) {
            HashMap<String, Object> map = new HashMap<>();
            Typable typable = (Typable) data;
            map.put(StructureDescription.CountTypes.ID, typable.getId());
            map.put(StructureDescription.CountTypes.COUNT_TYPE, typable.getValue());
            datas.add(map);
        }

        res.put(StructureDescription.CountTypes.TABLE_NAME, datas);

        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) {
        datas.clear();
        for (HashMap<String, Object> row : data) {
            datas.add(
                    new ListElement(
                            String.valueOf(row.get(StructureDescription.CountTypes.ID)),
                            (String) row.get(StructureDescription.CountTypes.COUNT_TYPE)
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
