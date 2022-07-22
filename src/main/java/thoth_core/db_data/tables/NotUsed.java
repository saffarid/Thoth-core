package thoth_core.db_data.tables;

import database.Column.TableColumn;
import thoth_core.db_lite_structure.StructureDescription;
import thoth_core.exceptions.NotContainsException;
import thoth_core.db_data.db_data_element.implement.NotUse;
import thoth_core.db_data.db_data_element.implement.Product;
import thoth_core.db_data.db_data_element.properties.Identifiable;

import java.sql.ResultSet;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NotUsed
        extends Data<NotUse> {

    public NotUsed() {
        super();
        setName(StructureDescription.NotUsed.TABLE_NAME);
    }

    @Override
    public Map< String, List< HashMap<String, Object> > > convertToMap(List<? extends Identifiable> list) {
        HashMap< String, List< HashMap<String, Object> > > res = new HashMap<>();
        List<HashMap<String, Object>> datas = new LinkedList<>();
        for (Identifiable identifiable : list) {
            HashMap<String, Object> map = new HashMap<>();
            NotUse notUse = (NotUse) identifiable;
            map.put(StructureDescription.NotUsed.PRODUCT_ID, notUse.getProduct().getId());
            map.put(StructureDescription.NotUsed.CAUSE, notUse.getCause());
            datas.add(map);
        }
        res.put(StructureDescription.NotUsed.TABLE_NAME, datas);
        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) throws ParseException {
        datas.clear();
        for (HashMap<String, Object> row : data) {
            try {
                datas.add(
                        new NotUse(
                                String.valueOf(row.get(StructureDescription.NotUsed.ID)),
                                (Product) getFromTableById(StructureDescription.Products.TABLE_NAME, String.valueOf(row.get(
                                        StructureDescription.NotUsed.PRODUCT_ID))),
                                (String) row.get(StructureDescription.NotUsed.CAUSE)
                        )
                );
            } catch (NotContainsException e) {
                e.printStackTrace();
            }
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
