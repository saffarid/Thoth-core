package thoth_core.db_data.tables;

import database.Column.TableColumn;
import thoth_core.db_lite_structure.StructureDescription;
import thoth_core.db_data.db_data_element.implement.ListElement;
import thoth_core.db_data.db_data_element.properties.Identifiable;
import thoth_core.db_data.db_data_element.properties.Typable;

import java.sql.ResultSet;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExpensesTypes
        extends Data<Typable>{

    public ExpensesTypes() {
        super();
        setName(StructureDescription.ExpensesTypes.TABLE_NAME);
    }

    @Override
    public Map<String, List<HashMap<String, Object>>> convertToMap(List<? extends Identifiable> list) {
        HashMap<String, List<HashMap<String, Object>>> res = new HashMap<>();
        List<HashMap<String, Object>> data = new LinkedList<>();
        for( Identifiable identifiable : list){
            HashMap<String, Object> row = new HashMap<>();
            Typable typable = (Typable) identifiable;
            row.put(StructureDescription.ExpensesTypes.ID, typable.getId());
            row.put(StructureDescription.ExpensesTypes.EXPENSES_TYPE, typable.getValue());
            data.add(row);
        }
        res.put(StructureDescription.ExpensesTypes.TABLE_NAME, data);
        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) throws ParseException {
        datas.clear();
        for(HashMap<String, Object> row : data){
            datas.add(
                    new ListElement(
                            String.valueOf(row.get(StructureDescription.ExpensesTypes.ID)),
                            String.valueOf(row.get(StructureDescription.ExpensesTypes.EXPENSES_TYPE))
                    )
            );
        }
        publisher.submit(datas);
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, ResultSet resultSet) throws ParseException {

    }

    @Override
    public void readTableWithTableColumn(StructureDescription.TableTypes tableType, List<HashMap<TableColumn, Object>> data) throws ParseException {

    }
}
