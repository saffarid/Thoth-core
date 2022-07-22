package thoth_core.db_data.tables;

import database.Column.TableColumn;
import thoth_core.db_lite_structure.StructureDescription;
import thoth_core.db_data.db_data_element.implement.Partner;
import thoth_core.db_data.db_data_element.properties.Partnership;
import thoth_core.db_data.db_data_element.properties.Identifiable;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Partners
        extends Data<Partnership>
{

    public Partners() {
        super();
        setName(StructureDescription.Partners.TABLE_NAME);
    }

    @Override
    public Map< String, List< HashMap<String, Object> > > convertToMap(List<? extends Identifiable> list){
        HashMap< String, List< HashMap<String, Object> > > res = new HashMap<>();
        List<HashMap<String, Object>> datas = new LinkedList<>();
        for(Identifiable identifiable : list){
            HashMap<String, Object> map = new HashMap<>();
            Partnership partner = (Partnership) identifiable;
            map.put(StructureDescription.Partners.ID, partner.getId());
            map.put(StructureDescription.Partners.NAME, partner.getName());
            map.put(StructureDescription.Partners.PHONE, partner.getPhone());
            map.put(StructureDescription.Partners.WEB, partner.getWeb());
            map.put(StructureDescription.Partners.COMMENT, partner.getComment());
            datas.add(map);
        }
        res.put(StructureDescription.Partners.TABLE_NAME, datas);
        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) {
        datas.clear();
        for(HashMap<String, Object> row : data){
            datas.add(new Partner(
                    String.valueOf(row.get(StructureDescription.Partners.ID)),
                    String.valueOf(row.get(StructureDescription.Partners.NAME)),
                    String.valueOf(row.get(StructureDescription.Partners.PHONE)),
                    String.valueOf(row.get(StructureDescription.Partners.WEB)),
                    String.valueOf(row.get(StructureDescription.Partners.COMMENT))
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
