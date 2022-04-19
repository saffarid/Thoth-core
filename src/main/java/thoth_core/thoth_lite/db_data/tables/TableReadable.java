package thoth_core.thoth_lite.db_data.tables;

import database.Column.TableColumn;
import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription;

import java.sql.ResultSet;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public interface TableReadable {
    void readTable(StructureDescription.TableTypes tableType, List<HashMap<String, Object>> data) throws ParseException;
    void readTable(StructureDescription.TableTypes tableType, ResultSet resultSet) throws ParseException;
    void readTableWithTableColumn(StructureDescription.TableTypes tableType, List<HashMap<TableColumn, Object>> data) throws ParseException;
}
