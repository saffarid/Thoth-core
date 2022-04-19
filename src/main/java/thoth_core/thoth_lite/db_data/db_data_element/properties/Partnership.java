package thoth_core.thoth_lite.db_data.db_data_element.properties;

import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Commentable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Connectable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Nameable;

public interface Partnership
        extends Identifiable
        , Nameable
        , Connectable
        , Commentable {
}
