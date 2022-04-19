package thoth_core.thoth_lite.db_data.db_data_element.properties;

import java.util.Currency;

public interface Finance
    extends Identifiable
{
    Currency getCurrency();
    Double getCourse();
    void setCourse(Double course);

}
