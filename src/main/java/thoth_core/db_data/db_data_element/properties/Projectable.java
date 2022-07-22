package thoth_core.db_data.db_data_element.properties;

import thoth_core.db_data.db_data_element.properties.parts.Composite;
import thoth_core.db_data.db_data_element.properties.parts.Nameable;

public interface Projectable
        extends
        Nameable
        , Composite
        , Identifiable

{
    void setType(Typable type);
    Typable getType();

    void setNote(String note);
    String getNote();
}
