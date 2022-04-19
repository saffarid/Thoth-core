package thoth_core.thoth_lite.timer;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Finishable;

public interface Closeable {
    void close(Finishable finishable);
}
