package thoth_core.thoth_lite.db_data.db_data_element.properties;

import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Countable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Pricing;

/**
 * Объект реализует условную ячейку в которой хранится некоторое кол-во хранимого объекта.
 * */
public interface Storing
        extends Countable
        , Identifiable
        , Pricing
{

    Storagable getStoragable();
    void setStorageable(Storagable storageable);

    Double getCourse();
    void setCourse(Double course);

}
