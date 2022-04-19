package thoth_core.thoth_lite.db_data.tables;

import database.Column.TableColumn;
import thoth_core.thoth_lite.db_data.db_data_element.implement.ListElement;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Typable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Identifiable;
import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription.CountTypes.*;

public class CountTypes
        extends Data<Typable>
        implements TableReadable {

    public CountTypes() {
        super();
        setName(TABLE_NAME);
    }

    @Override
    public Map< String, List< HashMap<String, Object> > > convertToMap(List<? extends Identifiable> identifiable) {

        HashMap< String, List< HashMap<String, Object> > > res = new HashMap<>();

        List<HashMap<String, Object>> datas = new LinkedList<>();

        for (Identifiable data : identifiable) {
            HashMap<String, Object> map = new HashMap<>();
            Typable typable = (Typable) data;
            map.put(ID, typable.getId());
            map.put(COUNT_TYPE, typable.getValue());
            datas.add(map);
        }

        res.put(TABLE_NAME, datas);

        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) {
        datas.clear();
        for (HashMap<String, Object> row : data) {
            datas.add(
                    new ListElement(
                            String.valueOf(row.get(ID)),
                            (String) row.get(COUNT_TYPE)
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
