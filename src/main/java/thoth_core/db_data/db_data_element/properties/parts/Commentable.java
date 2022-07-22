package thoth_core.db_data.db_data_element.properties.parts;

public interface Commentable {

    /**
     * @return комментарий к записи
     * */
    String getComment();
    /**
     * Функция устанавливает комментарий к записи
     * */
    void setComment(String comment);

}
