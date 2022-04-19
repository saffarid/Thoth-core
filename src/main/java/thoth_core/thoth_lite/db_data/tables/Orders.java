package thoth_core.thoth_lite.db_data.tables;

import database.Column.TableColumn;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Orderable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Identifiable;
import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription;

import java.sql.ResultSet;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription.Orders.*;

public class Orders
        extends Data<Orderable> {

    public Orders() {
        super();
        setName(TABLE_NAME);
    }

    @Override
    public Map< String, List< HashMap<String, Object> > > convertToMap(List<? extends Identifiable> list) {
        HashMap< String, List< HashMap<String, Object> > > res = new HashMap<>();
        List<HashMap<String, Object>> datas = new LinkedList<>();
        for (Identifiable identifiable : list) {
            HashMap<String, Object> map = new HashMap<>();
            Orderable orderable = (Orderable) identifiable;
            map.put(CUSTOMER_ID, orderable.getPartner().getId());
            map.put(PROJECT_ID, orderable.getProjectable().getId());
            map.put(DATE_START, orderable.startDate().format(DateTimeFormatter.ISO_DATE));
            map.put(DATE_FINISH, orderable.finishDate().format(DateTimeFormatter.ISO_DATE));
            map.put(STATUS_ID, orderable.getStatus().getValue() );
            map.put(AUTOFINISH, 0);
            map.put(IS_MONTHLY, 0);
            datas.add(map);
        }
        res.put(TABLE_NAME, datas);
        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) throws ParseException {
        datas.clear();
        for (HashMap<String, Object> row : data) {
//            try {
//                addData(
//                        new Order(
//                                String.valueOf(row.get(ID)),
//                                String.valueOf(row.get(S))
//                                (Partner) getFromTableById(StructureDescription.Partners.TABLE_NAME, String.valueOf(row.get(CUSTOMER_ID))),
//                                (Projectable) getFromTableById(StructureDescription.ProjectsList.TABLE_NAME, String.valueOf(row.get(PROJECT_ID))),
//                                (int) row.get(IS_MONTHLY) == 1,
//                                (String) row.get(DATE_START),
//                                (String) row.get(DATE_FINISH),
//                                (Listed) getFromTableById(StructureDescription.OrderStatus.TABLE_NAME, String.valueOf(row.get(STATUS_ID))),
//                                (int) row.get(AUTOFINISH) == Finishable.AUTOFINISH
//                        )
//                );
//            } catch (NotContainsException e) {
//                e.printStackTrace();
//            }
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
