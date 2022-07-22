package thoth_core.db_data.tables;

import database.Column.TableColumn;
import thoth_core.db_lite_structure.StructureDescription;
import thoth_core.exceptions.NotContainsException;
import thoth_core.db_data.db_data_element.implement.FinancialOperation;
import thoth_core.db_data.db_data_element.properties.Finance;
import thoth_core.db_data.db_data_element.properties.FinancialAccounting;
import thoth_core.db_data.db_data_element.properties.Identifiable;
import thoth_core.db_data.db_data_element.properties.Typable;

import java.sql.ResultSet;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Expenses
        extends Data<FinancialAccounting> {

    public Expenses() {
        super();
        setName(StructureDescription.Expenses.TABLE_NAME);
    }

    @Override
    public Map<String, List<HashMap<String, Object>>> convertToMap(List<? extends Identifiable> list) {
        HashMap<String, List<HashMap<String, Object>>> res = new HashMap<>();
        List<HashMap<String, Object>> data = new LinkedList<>();
        for (Identifiable identifiable : list) {
            HashMap<String, Object> row = new HashMap<>();

            FinancialAccounting financialAccounting = (FinancialAccounting) identifiable;

            row.put(StructureDescription.Expenses.EXPENSES_TYPE_ID, financialAccounting.getCategory().getValue());
            row.put(StructureDescription.Expenses.VALUE, financialAccounting.getValue());
            row.put(StructureDescription.Expenses.DATE, financialAccounting.getDate().format(DateTimeFormatter.ISO_DATE));
            row.put(StructureDescription.Expenses.CURRENCY_ID, (financialAccounting.getFinance() == null) ? (null) : (financialAccounting.getFinance().getCurrency().getCurrencyCode()));
            row.put(StructureDescription.Expenses.COURSE, financialAccounting.getCourse());
            row.put(StructureDescription.Expenses.COMMENT, financialAccounting.getComment());

            data.add(row);
        }
        res.put(StructureDescription.Expenses.TABLE_NAME, data);
        return res;
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data)
            throws ParseException {
        datas.clear();

        for (HashMap<String, Object> row : data) {
            try {
                datas.add(
                        new FinancialOperation(
                                String.valueOf(row.get(StructureDescription.Expenses.ID)),
                                (Typable) getFromTableById(StructureDescription.ExpensesTypes.TABLE_NAME, String.valueOf(row.get(
                                        StructureDescription.Expenses.EXPENSES_TYPE_ID))),
                                (Double) row.get(StructureDescription.Expenses.VALUE),
                                LocalDate.parse((String) row.get(StructureDescription.Expenses.DATE)),
                                (row.get(StructureDescription.Expenses.CURRENCY_ID) == null) ? (null) : ((Finance) getFromTableById(StructureDescription.Currency.TABLE_NAME, String.valueOf(row.get(
                                        StructureDescription.Expenses.CURRENCY_ID)))),
                                (Double) row.get(StructureDescription.Expenses.COURSE),
                                String.valueOf(row.get(StructureDescription.Expenses.COMMENT))
                        )
                );
            } catch (NotContainsException e) {
                e.printStackTrace();
            }
        }

        publisher.submit(datas);
    }

    @Override
    public void readTable(StructureDescription.TableTypes tableType, ResultSet resultSet)
            throws ParseException {

    }

    @Override
    public void readTableWithTableColumn(StructureDescription.TableTypes tableType, List<HashMap<TableColumn, Object>> data)
            throws ParseException {

    }

}
