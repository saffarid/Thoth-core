package thoth_core.thoth_lite.db_data.db_data_element.properties.parts;

public interface Typable {

    /**
     * @return тип объекта
     * */
    thoth_core.thoth_lite.db_data.db_data_element.properties.Typable getType();

    /**
     * @param type устанавливаемый тип
     * */
    void setType(thoth_core.thoth_lite.db_data.db_data_element.properties.Typable type);

}
