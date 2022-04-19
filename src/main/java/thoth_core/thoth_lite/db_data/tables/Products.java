package thoth_core.thoth_lite.db_data.tables;

import database.Column.TableColumn;
import thoth_core.thoth_lite.db_data.db_data_element.implement.Product;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Typable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Identifiable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Storagable;
import thoth_core.thoth_lite.exceptions.NotContainsException;
import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription.Products.*;

public class Products
        extends Data<Storagable> {

    public Products() {
        super();
        setName(TABLE_NAME);
    }

    @Override
    public Map<String, List<HashMap<String, Object>>> convertToMap(List<? extends Identifiable> identifiable) {
        HashMap<String, List<HashMap<String, Object>>> res = new HashMap<>();
        List<HashMap<String, Object>> datas = new LinkedList<>();
        for (Identifiable data : identifiable) {
            Storagable storagable = (Storagable) data;
            HashMap<String, Object> map = new HashMap<>();
            map.put(ARTICLE, storagable.getId());
            map.put(NAME, storagable.getName());
            map.put(PRODUCT_TYPE_ID, storagable.getType().getValue());
            map.put(COUNT, storagable.getCount());
            map.put(COUNT_TYPE_ID, storagable.getCountType().getValue());
            map.put(ADRESS, storagable.getAdress().getValue());
            datas.add(map);
        }
        res.put(TABLE_NAME, datas);
        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) {
        datas.clear();
        for (HashMap<String, Object> row : data) {
            try {
                addData(
                        new Product(
                                (String) row.get(ARTICLE),
                                (String) row.get(NAME),
                                (Typable) getFromTableById(StructureDescription.ProductTypes.TABLE_NAME, String.valueOf(row.get(PRODUCT_TYPE_ID))),
                                Double.parseDouble(String.valueOf(row.get(COUNT))),
                                (Typable) getFromTableById(StructureDescription.CountTypes.TABLE_NAME, String.valueOf(row.get(COUNT_TYPE_ID))),
                                (Typable) getFromTableById(StructureDescription.Storage.TABLE_NAME, String.valueOf(row.get(ADRESS))),
                                (String) row.get(NOTE)
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

