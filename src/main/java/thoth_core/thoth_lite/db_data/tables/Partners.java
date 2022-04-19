package thoth_core.thoth_lite.db_data.tables;

import database.Column.TableColumn;
import thoth_core.thoth_lite.db_data.db_data_element.implement.Partner;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Partnership;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Identifiable;
import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription.Partners.*;


public class Partners
        extends Data<Partnership>
{

    public Partners() {
        super();
        setName(TABLE_NAME);
    }

    @Override
    public Map< String, List< HashMap<String, Object> > > convertToMap(List<? extends Identifiable> list){
        HashMap< String, List< HashMap<String, Object> > > res = new HashMap<>();
        List<HashMap<String, Object>> datas = new LinkedList<>();
        for(Identifiable identifiable : list){
            HashMap<String, Object> map = new HashMap<>();
            Partnership partner = (Partnership) identifiable;
            map.put(ID, partner.getId());
            map.put(NAME, partner.getName());
            map.put(PHONE, partner.getPhone());
            map.put(WEB, partner.getWeb());
            map.put(COMMENT, partner.getComment());
            datas.add(map);
        }
        res.put(TABLE_NAME, datas);
        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) {
        datas.clear();
        for(HashMap<String, Object> row : data){
            datas.add(new Partner(
                    String.valueOf(row.get(ID)),
                    String.valueOf(row.get(NAME)),
                    String.valueOf(row.get(PHONE)),
                    String.valueOf(row.get(WEB)),
                    String.valueOf(row.get(COMMENT))
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
