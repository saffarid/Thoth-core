package thoth_core.thoth_lite.db_data.db_data_element.properties.parts;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Partnership;

public interface HasPartner {

    Partnership getPartner();
    void setPartner(Partnership partner);

}
