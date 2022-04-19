package thoth_core.thoth_lite.db_data.tables;

import database.Column.TableColumn;
import thoth_core.thoth_lite.db_data.db_data_element.properties.*;
import thoth_core.thoth_lite.exceptions.NotContainsException;
import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription;
import thoth_core.thoth_lite.db_data.db_data_element.implement.Purchase;
import thoth_core.thoth_lite.db_data.db_data_element.implement.StorageCell;

import java.sql.ResultSet;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription.Purchase.*;

public class Purchases
        extends Data<Purchasable> {

    public Purchases() {
        super();
        setName(NAME);
    }

    @Override
    public Map<String, List<HashMap<String, Object>>> convertToMap(List<? extends Identifiable> list) {
        HashMap<String, List<HashMap<String, Object>>> res = new LinkedHashMap<>();
        List<HashMap<String, Object>> datasDesc = new LinkedList<>();
        List<HashMap<String, Object>> datasComposition = new LinkedList<>();

        for (Identifiable identifiable : list) {
            Purchasable purchasable = (Purchasable) identifiable;

            HashMap<String, Object> description = new HashMap<>();

            //Заполнение карты описания
            description.put(Desc.PURCHASE_ID, purchasable.getId());
            description.put(Desc.STORE_ID, (purchasable.getPartner()!=null)?purchasable.getPartner().getName():null);
            description.put(Desc.DELIVERY_DATE, purchasable.finishDate().format(DateTimeFormatter.ISO_DATE));
            description.put(Desc.IS_DELIVERED, purchasable.isDelivered() ? 1 : 0);
            datasDesc.add(description);
            //Заполнение карты состава
            for (Storing storing : purchasable.getComposition()) {
                HashMap<String, Object> composition = new HashMap<>();
                composition.put(Composite.PURCHASE_ID, purchasable.getId());
                composition.put(Composite.PRODUCT_ID, storing.getStoragable().getId());
                composition.put(Composite.COUNT, storing.getCount());
                composition.put(Composite.COUNT_TYPE_ID, storing.getCountType().getValue());
                composition.put(Composite.PRICE, storing.getPrice());
                composition.put(Composite.CURRENCY_ID, storing.getCurrency().getCurrency().getCurrencyCode());
                composition.put(Composite.COURSE, storing.getCurrency().getCourse());
                datasComposition.add(composition);
            }
        }
        res.put(Desc.TABLE_NAME, datasDesc);
        res.put(Composite.TABLE_NAME, datasComposition);
        return res;
    }

    /**
     * Функция считывает описание покупки
     */
    private void readDescription(List<HashMap<String, Object>> data) {
        for (HashMap<String, Object> row : data) {
            try {
                datas.add(
                        new Purchase(
                                String.valueOf(row.get(Desc.PURCHASE_ID)),
                                (Partnership) getFromTableById(StructureDescription.Partners.TABLE_NAME, String.valueOf(row.get(Desc.STORE_ID))),
                                LocalDate.parse(String.valueOf(row.get(Desc.DELIVERY_DATE))),
                                (int)row.get(Desc.IS_DELIVERED)==1
                        )
                );
            } catch (NotContainsException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Функция считывает состав покупки
     */
    private void readComposition(List<HashMap<String, Object>> data) {
        for(HashMap<String, Object> row : data){

            try {
                Purchasable byId = getById(String.valueOf(row.get(Composite.PURCHASE_ID)));
                List<Storing> composition = byId.getComposition();

                composition.add(new StorageCell(
                        String.valueOf(row.get(Composite.ID)),
                        (Storagable) getFromTableById(StructureDescription.Products.TABLE_NAME, String.valueOf(row.get(Composite.PRODUCT_ID))),
                        Double.parseDouble(String.valueOf(row.get(Composite.COUNT))),
                        (Typable) getFromTableById(StructureDescription.CountTypes.TABLE_NAME, String.valueOf(row.get(Composite.COUNT_TYPE_ID))),
                        Double.parseDouble(String.valueOf(row.get(Composite.PRICE))),
                        (Finance) getFromTableById(StructureDescription.Currency.TABLE_NAME, String.valueOf(row.get(Composite.CURRENCY_ID))),
                        Double.parseDouble(String.valueOf(row.get(Composite.COURSE)))
                ));

            } catch (NotContainsException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) throws ParseException {

        switch (tableType) {
            case DESC: {
                datas.clear();
                readDescription(data);
                break;
            }
            case COMPOSITE: {
                readComposition(data);
                break;
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
