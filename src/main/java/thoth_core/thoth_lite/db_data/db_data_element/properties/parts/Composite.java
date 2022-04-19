package thoth_core.thoth_lite.db_data.db_data_element.properties.parts;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Storing;

import java.util.List;

public interface Composite {

    List<Storing> getComposition();

    void addStoring(Storing storing);
    boolean containsStoring(Storing storing);
    Storing getStoringByStoragableId(String id);
    Storing getStoringByStoringId(String id);
    boolean removeStoring(Storing storing);

}
