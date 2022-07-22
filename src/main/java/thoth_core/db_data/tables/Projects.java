package thoth_core.db_data.tables;

import database.Column.TableColumn;
import thoth_core.db_data.db_data_element.properties.*;
import thoth_core.db_lite_structure.StructureDescription;
import thoth_core.exceptions.NotContainsException;
import thoth_core.db_data.db_data_element.implement.Project;
import thoth_core.db_data.db_data_element.implement.StorageCell;

import java.sql.ResultSet;
import java.text.ParseException;
import java.util.*;

public class Projects
        extends Data<Projectable> {
    public Projects() {
        super();
        setName(StructureDescription.Project.NAME);
    }

    @Override
    public Map<String, List<HashMap<String, Object>>> convertToMap(List<? extends Identifiable> list) {
        HashMap<String, List<HashMap<String, Object>>> res = new LinkedHashMap<>();
        List<HashMap<String, Object>> datasDesc = new LinkedList<>();
        List<HashMap<String, Object>> datasComposition = new LinkedList<>();

        for (Identifiable identifiable : list) {
            Projectable projectable = (Projectable) identifiable;

            HashMap<String, Object> description = new HashMap<>();

            //Заполнение карты описания
            description.put(StructureDescription.Project.Desc.PROJECT_ID, projectable.getId());
            description.put(StructureDescription.Project.Desc.PROJECT_NAME, projectable.getName());
            description.put(StructureDescription.Project.Desc.PROJECT_TYPE_ID, projectable.getType().getValue());
            description.put(StructureDescription.Project.Desc.NOTE, projectable.getNote());
            datasDesc.add(description);
            //Заполнение карты состава
            for (Storing storing : projectable.getComposition()) {
                HashMap<String, Object> composition = new HashMap<>();
                composition.put(StructureDescription.Project.Composite.PROJECT_ID, projectable.getId());
                composition.put(StructureDescription.Project.Composite.PRODUCT_ID, storing.getStoragable().getId());
                composition.put(StructureDescription.Project.Composite.COUNT, storing.getCount());
                composition.put(StructureDescription.Project.Composite.COUNT_TYPE_ID, storing.getCountType().getValue());
                composition.put(StructureDescription.Project.Composite.PRICE, storing.getPrice());
                composition.put(StructureDescription.Project.Composite.CURRENCY_ID, storing.getCurrency().getCurrency().getCurrencyCode());
                composition.put(StructureDescription.Project.Composite.COURSE, storing.getCurrency().getCourse());
                datasComposition.add(composition);
            }
        }
        res.put(StructureDescription.Project.Desc.TABLE_NAME, datasDesc);
        res.put(StructureDescription.Project.Composite.TABLE_NAME, datasComposition);
        return res;
    }

    /**
     * Функция считывает описание покупки
     */
    private void readDescription(List<HashMap<String, Object>> data) {
        for (HashMap<String, Object> row : data) {
            try {
                datas.add(
                        new Project(
                                String.valueOf(row.get(StructureDescription.Project.Desc.PROJECT_ID)),
                                String.valueOf(row.get(StructureDescription.Project.Desc.PROJECT_NAME)),
                                (Typable) getFromTableById(StructureDescription.ProjectTypes.TABLE_NAME, String.valueOf(row.get(
                                        StructureDescription.Project.Desc.PROJECT_TYPE_ID))),
                                String.valueOf(row.get(StructureDescription.Project.Desc.NOTE))
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
                Projectable byId = getById(String.valueOf(row.get(StructureDescription.Project.Composite.PROJECT_ID)));
                List<Storing> composition = byId.getComposition();

                composition.add(new StorageCell(
                        String.valueOf(row.get(StructureDescription.Project.Composite.ID)),
                        (Storagable) getFromTableById(StructureDescription.Products.TABLE_NAME, String.valueOf(row.get(
                                StructureDescription.Project.Composite.PRODUCT_ID))),
                        Double.parseDouble(String.valueOf(row.get(StructureDescription.Project.Composite.COUNT))),
                        (Typable) getFromTableById(StructureDescription.CountTypes.TABLE_NAME, String.valueOf(row.get(
                                StructureDescription.Project.Composite.COUNT_TYPE_ID))),
                        Double.parseDouble(String.valueOf(row.get(StructureDescription.Project.Composite.PRICE))),
                        (Finance) getFromTableById(StructureDescription.Currency.TABLE_NAME, String.valueOf(row.get(
                                StructureDescription.Project.Composite.CURRENCY_ID))),
                        Double.parseDouble(String.valueOf(row.get(StructureDescription.Project.Composite.COURSE)))
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
