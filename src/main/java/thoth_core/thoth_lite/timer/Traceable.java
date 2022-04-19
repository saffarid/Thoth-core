package thoth_core.thoth_lite.timer;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Finishable;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Flow;

public interface Traceable
        extends
        Flow.Publisher<HashMap<WhatDo, Finishable>>
{
    void notificationPlanning(List<Finishable> finishables);
}
