package thoth_core.thoth_lite.db_data.db_data_element.properties;

import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Countable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Nameable;

/**
 * Объект, который возможно хранить, использовать в личных проектах.
 */
public interface Storagable
        extends Identifiable
        , Nameable
        , thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Typable
        , Countable
{

    void setAdress(Typable adress);
    Typable getAdress();

    void setNote(String note);
    String getNote();

}
