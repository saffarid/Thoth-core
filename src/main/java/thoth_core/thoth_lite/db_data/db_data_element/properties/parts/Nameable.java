package thoth_core.thoth_lite.db_data.db_data_element.properties.parts;

public interface Nameable {

    /**
     * @return наименование объекта
     * */
    String getName();

    /**
     * Функция устанавливает наименование объекта
     * @param name устанавливаемое наименование объекта
     * */
    void setName(String name);

}
