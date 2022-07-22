package thoth_core.timer;

import thoth_core.db_data.db_data_element.properties.Finishable;

public interface Closeable {
    void close(Finishable finishable);
}
