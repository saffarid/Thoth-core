package thoth_core.db_data.tables;

import database.Column.TableColumn;
import thoth_core.db_lite_structure.StructureDescription;
import thoth_core.exceptions.NotContainsException;
import thoth_core.db_data.db_data_element.implement.Product;
import thoth_core.db_data.db_data_element.properties.Typable;
import thoth_core.db_data.db_data_element.properties.Identifiable;
import thoth_core.db_data.db_data_element.properties.Storagable;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Products
        extends Data<Storagable> {

    public Products() {
        super();
        setName(StructureDescription.Products.TABLE_NAME);
    }

    @Override
    public Map<String, List<HashMap<String, Object>>> convertToMap(List<? extends Identifiable> identifiable) {
        HashMap<String, List<HashMap<String, Object>>> res = new HashMap<>();
        List<HashMap<String, Object>> datas = new LinkedList<>();
        for (Identifiable data : identifiable) {
            Storagable storagable = (Storagable) data;
            HashMap<String, Object> map = new HashMap<>();
            map.put(StructureDescription.Products.ARTICLE, storagable.getId());
            map.put(StructureDescription.Products.NAME, storagable.getName());
            map.put(StructureDescription.Products.PRODUCT_TYPE_ID, storagable.getType().getValue());
            map.put(StructureDescription.Products.COUNT, storagable.getCount());
            map.put(StructureDescription.Products.COUNT_TYPE_ID, storagable.getCountType().getValue());
            map.put(StructureDescription.Products.ADRESS, storagable.getAdress().getValue());
            datas.add(map);
        }
        res.put(StructureDescription.Products.TABLE_NAME, datas);
        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) {
        datas.clear();
        for (HashMap<String, Object> row : data) {
            try {
                addData(
                        new Product(
                                (String) row.get(StructureDescription.Products.ARTICLE),
                                (String) row.get(StructureDescription.Products.NAME),
                                (Typable) getFromTableById(StructureDescription.ProductTypes.TABLE_NAME, String.valueOf(row.get(
                                        StructureDescription.Products.PRODUCT_TYPE_ID))),
                                Double.parseDouble(String.valueOf(row.get(StructureDescription.Products.COUNT))),
                                (Typable) getFromTableById(StructureDescription.CountTypes.TABLE_NAME, String.valueOf(row.get(
                                        StructureDescription.Products.COUNT_TYPE_ID))),
                                (Typable) getFromTableById(StructureDescription.Storage.TABLE_NAME, String.valueOf(row.get(
                                        StructureDescription.Products.ADRESS))),
                                (String) row.get(StructureDescription.Products.NOTE)
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

