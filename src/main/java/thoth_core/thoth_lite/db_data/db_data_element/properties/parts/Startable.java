package thoth_core.thoth_lite.db_data.db_data_element.properties.parts;

import java.time.LocalDate;

public interface Startable {

    /**
     * @return  дата начала
     * */
    LocalDate startDate();

    /**
     * @param startDate дата начала
     * */
    void setStartDate(String startDate);

}
