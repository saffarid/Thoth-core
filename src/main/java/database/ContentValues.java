package database;

import database.Column.Autoincrement;
import database.Column.ForeignKey;
import database.Column.PrimaryKey;
import database.Column.TableColumn;

import java.util.*;
import java.util.stream.Collectors;

public class ContentValues extends HashMap<TableColumn, Object> {

    private final String TEMPLATE_COMAND_INSERT = "(\n\t%1$s\n) values (\n\t%2$s)";
    private final String TEMPLATE_COMAND_UPDATE = "`%1$s`=%2$s\n";

    /**
     * Преобразование объекта в строку при создании таблицы в БД
     */
    public String toStringInsert() {
        StringBuilder colName = new StringBuilder("");
        StringBuilder colValue = new StringBuilder("");

        String strColNameTemplate = "`%1$s`";
        String strColValTemplate = "\'%1$s\'";

        List<TableColumn> columns = new LinkedList<>(keySet());
        //Отфильтруем все значения по null
        columns = columns.stream().filter(column -> get(column) != null).collect(Collectors.toList());

        for (TableColumn column : columns) {

            //Проверяем TableColumn на наличие внешнего ключа
            Object value = get(column);

            if (column instanceof ForeignKey && ((ForeignKey)column).hasForeignKey()) {
                //Колонка содержит внешний ключ
                colValue.append( getValueSubrequest( (ForeignKey) column, value ) );
            } else {
                //Колонка не содержит внешний ключ
                if (value instanceof String) {
                    colValue.append(
                            String.format(strColValTemplate, value)
                    );
                } else {
                    colValue.append(value);
                }
            }
            colName.append(
                    String.format(strColNameTemplate, column.getName())
            );
            if (columns.indexOf(column) != (columns.size() - 1)) {
                colName.append(", \n\t");
                colValue.append(", \n\t");
            }

        }

        return String.format(TEMPLATE_COMAND_INSERT, colName.toString(), colValue.toString());
    }

    /**
     * Функция формирует строки подзапросов для использования в блоке values запроса Insert
     */
    private String getValueSubrequest(ForeignKey column, Object value) {
        TableColumn foreignKey = column.getForeignKey();
        //Если внешний ключ установлен, объект не будет равен null
        if (foreignKey != null) {
            /*
             * Необходимо определить ID строки внешней таблицы, в contentValues содержится значения для пользователя
             * */
            String subRequestTemplate = "(select `%1$s` from `%2$s` where %3$s)";
            String whereTempalte =
                    (value instanceof String)
                            ? ("`%1$s` = \'%2$s\'")
                            : ("`%1$s` = %2$s");
            StringBuilder whereBuilder = new StringBuilder("");
            if (foreignKey instanceof Autoincrement) {
                //Обработка случая, когда внешний ключ ссылается на первичный автоинкрементируемый ключ внешней таблицы
                /*
                 * Формируем подзапрос select ID from `внешняя таблица` where columnName = columnValue.
                 * ID - foreignKey.getName, внешняя таблица - foreignKey.getTable, foreignKey.getName - column, columnValue - get(column)
                 */

                //Определяем все колонки кроме первичного ключа
                List<TableColumn> collect = foreignKey
                        .getTable()
                        .getColumns()
                        .stream()
                        .filter(col -> !(col instanceof PrimaryKey))
                        .collect(Collectors.toList());
                /*Формируем блок Where.
                * Логика формирования блока colName1 = value or colName2 = value or colName3 = value.*/
                for (TableColumn col : collect) {
                    whereBuilder.append(
                            String.format(whereTempalte, col.getName().trim(), value.toString().trim())
                    );
                    if (collect.indexOf(col) != collect.size() - 1) {
                        whereBuilder.append(" or ");
                    }
                }

                //Формируем строку подзапроса
                return String.format(
                        subRequestTemplate,
                        foreignKey.getName(),   //1
                        foreignKey.getTable().getName(),     //2
                        whereBuilder.toString()
                );

//                colValue.append("(" + subRequest + ")");
            } else {
                //Обработка подзапроса если есть информация, какой столбец интересует внешний ключ
                //foreignKey - представляет столбец в котором ищем информацию для определения идентификатора
                Table tableParent = foreignKey.getTable();
                return String.format(
                        subRequestTemplate,
                        tableParent.getPrimaryKeyColumn().getName(),   //1
                        tableParent.getName(),     //2
                        String.format(whereTempalte, foreignKey.getName().trim(), value.toString().trim())
                );
//                colValue.append("(" + subRequest + ")");
            }
        }
        return null;
    }

    /**
     * Преобразование объекта в строку при обновлении записи в БД
     */
    public String toStringUpdate() {
        StringBuilder res = new StringBuilder("");

        List<TableColumn> columns = new LinkedList<>(keySet());
        //Отфильтруем все значения по null
        columns = columns.stream().filter(column -> get(column) != null).collect(Collectors.toList());

        for( TableColumn column : columns ){

            Object value = get(column);

            if(column instanceof ForeignKey && ((ForeignKey)column).hasForeignKey()){

                res.append(String.format(TEMPLATE_COMAND_UPDATE, column.getName(), getValueSubrequest((ForeignKey) column, value) ) );

            } else {

                if(value instanceof String){
                    res.append(String.format(TEMPLATE_COMAND_UPDATE, column.getName(), "\'" + value.toString() + "\'"));
                }else{
                    res.append(String.format(TEMPLATE_COMAND_UPDATE, column.getName(), value.toString()));
                }

            }

            if (columns.indexOf(column) != (columns.size() - 1)) {
                res.append(", ");
            }

        }

        return res.toString();
    }
}
