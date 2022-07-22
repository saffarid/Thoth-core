package thoth_core.db_data.db_data_element.properties;

public interface Identifiable {

    /**
     * @return Идентификатор объекта
     * */
    String getId();
    /**
     * Функция устанавливает идентификатор объекта.
     * @param id устанавливаемый идентификатор
     * */
    void setId(String id);

}
