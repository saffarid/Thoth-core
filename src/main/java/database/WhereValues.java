package database;


import database.Column.Autoincrement;
import database.Column.ForeignKey;
import database.Column.PrimaryKey;
import database.Column.TableColumn;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class WhereValues extends HashMap<TableColumn, Object> {

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("");
        String templateFullName = "\"%1s\".`%2s`";
        LinkedList<TableColumn> tablesColumns = new LinkedList<>(this.keySet());

        for (TableColumn tableColumn : tablesColumns) {

            Object value = this.get(tableColumn);
            String whereTemplate = (value instanceof String) ? ("%1s=\'%2s\'") : ("%1s=%2s");
            if (tableColumn instanceof ForeignKey) {
                TableColumn foreignKey = ((ForeignKey)tableColumn).getForeignKey();

                if(foreignKey != null) {
                    String subreqTemplate = "(select `%1s` from `%2s` where %3s)";
                    if (foreignKey instanceof Autoincrement) {
                        //Внешний ключ ссылается на автоинкрементируемый первичный ключ внешней таблицы
                        /*Формируем подзапрос следующего вида
                         * select id from fkTable where "все колонки кроме id" = value
                         */
                        //Определяем колонки для вывода информации

                        List<TableColumn> collect = foreignKey.getTable().getColumns()
                                .stream()
                                .filter(column1 -> !(column1 instanceof PrimaryKey))
                                .collect(Collectors.toList());

                        StringBuilder whereSubrequesResult = new StringBuilder("");

                        for (TableColumn column : collect) {
                            whereSubrequesResult.append(
                                    String.format(
                                            whereTemplate
                                            , String.format(templateFullName, column.getTable().getName(), column.getName())
                                            , value
                                    )
                            );
                            if (collect.indexOf(column) != collect.size() - 1) {
                                whereSubrequesResult.append(" or \n\t");
                            }
                        }

                        String subrequest = String.format(
                                subreqTemplate
                                , foreignKey.getName()
                                , foreignKey.getTable().getName()
                                , whereSubrequesResult.toString()
                        );

                        result.append(
                                String.format("`%1s` = %2s", tableColumn.getName(), subrequest)
                        );
                    } else {
                        //Внешний ключ ссылается на кастомную колонку внешней таблицы
                        /*Формируем подзапрос следующего вида
                         * select foreignKey from fkTable where fk.id = value*/
                        String subrequest =
                                String.format(
                                        subreqTemplate
                                        , foreignKey.getTable().getPrimaryKeyColumn().getName()
                                        , foreignKey.getTable().getName()
                                        , String.format(
                                                whereTemplate
                                                , String.format(templateFullName, foreignKey.getTable().getName(), foreignKey.getName())
                                                , value
                                        )
                                );
                        result.append(
                                String.format("`%1s` = %2s", tableColumn.getName(), subrequest)
                        );
                    }
                }

            } else {
                //Колонка не содержит внешнего ключа
                if(value instanceof String){
                    result.append(
                            String.format("`%1s` = \'%2s\'", tableColumn.getName(), value.toString())
                    );
                }else{
                    result.append(
                            String.format("`%1s` = %2s", tableColumn.getName(), value)
                    );
                }
            }

            if (tablesColumns.indexOf(tableColumn) != tablesColumns.size() - 1) {
                result.append(" and \n\t");
            }

        }

        return result.toString();
    }
}
