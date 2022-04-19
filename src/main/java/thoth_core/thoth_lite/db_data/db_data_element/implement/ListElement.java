package thoth_core.thoth_lite.db_data.db_data_element.implement;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Typable;

public class ListElement
        implements Typable {

    private String id;
    private String value;

    public ListElement(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public boolean equals(ListElement obj) {
        return (this.id.equals(obj.getId()) &&
                this.value.equals(obj.getValue()));
    }

    @Override
    public String getId() {
        return id;
    }
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
