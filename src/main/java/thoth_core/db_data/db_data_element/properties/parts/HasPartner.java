package thoth_core.db_data.db_data_element.properties.parts;

import thoth_core.db_data.db_data_element.properties.Partnership;

public interface HasPartner {

    Partnership getPartner();
    void setPartner(Partnership partner);

}
