package thoth_core.thoth_lite.db_data.tables;

import database.Column.TableColumn;
import thoth_core.thoth_lite.db_data.db_data_element.implement.NotUse;
import thoth_core.thoth_lite.db_data.db_data_element.implement.Product;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Identifiable;
import thoth_core.thoth_lite.exceptions.NotContainsException;
import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription;

import java.sql.ResultSet;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription.NotUsed.*;

public class NotUsed
        extends Data<NotUse> {

    public NotUsed() {
        super();
        setName(TABLE_NAME);
    }

    @Override
    public Map< String, List< HashMap<String, Object> > > convertToMap(List<? extends Identifiable> list) {
        HashMap< String, List< HashMap<String, Object> > > res = new HashMap<>();
        List<HashMap<String, Object>> datas = new LinkedList<>();
        for (Identifiable identifiable : list) {
            HashMap<String, Object> map = new HashMap<>();
            NotUse notUse = (NotUse) identifiable;
            map.put(PRODUCT_ID, notUse.getProduct().getId());
            map.put(CAUSE, notUse.getCause());
            datas.add(map);
        }
        res.put(TABLE_NAME, datas);
        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) throws ParseException {
        datas.clear();
        for (HashMap<String, Object> row : data) {
            try {
                datas.add(
                        new NotUse(
                                String.valueOf(row.get(ID)),
                                (Product) getFromTableById(StructureDescription.Products.TABLE_NAME, String.valueOf(row.get(PRODUCT_ID))),
                                (String) row.get(CAUSE)
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
