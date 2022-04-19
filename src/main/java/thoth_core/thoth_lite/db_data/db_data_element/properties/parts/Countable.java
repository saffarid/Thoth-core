package thoth_core.thoth_lite.db_data.db_data_element.properties.parts;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Typable;

/**
 * Реализация интерфейса делает объект исчисляемым
 * */
public interface Countable {

    /**
     * @return кол-во
     * */
    Double getCount();

    /**
     * @param count новое кол-во
     * */
    void setCount(Double count);

    /**
     * @return единицы измерения
     * */
    Typable getCountType();

    /**
     * @param countType новая единица измерения
     * */
    void setCountType(Typable countType);

}
