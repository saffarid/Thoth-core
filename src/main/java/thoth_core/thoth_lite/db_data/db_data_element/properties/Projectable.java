package thoth_core.thoth_lite.db_data.db_data_element.properties;

import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Composite;
import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Nameable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Startable;

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
