package thoth_core.db_data.db_data_element.properties;

public interface Typable
    extends Identifiable
{

    String getValue();
    void setValue(String value);

}
