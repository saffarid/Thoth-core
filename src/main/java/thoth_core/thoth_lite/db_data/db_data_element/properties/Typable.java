package thoth_core.thoth_lite.db_data.db_data_element.properties;

public interface Typable
    extends Identifiable
{

    String getValue();
    void setValue(String value);

}
