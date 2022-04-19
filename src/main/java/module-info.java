module thoth {
    exports database;
    exports thoth_core.thoth_lite;
    exports thoth_core.thoth_lite.db_lite_structure;
    exports thoth_core.thoth_lite.db_data.tables;
    exports thoth_core.thoth_lite.db_data.db_data_element.properties;
    exports thoth_core.thoth_lite.db_data.db_data_element.implement;
    exports thoth_core.thoth_lite.exceptions;
    exports thoth_core.thoth_lite.config;
    exports thoth_core.thoth_lite.timer;
    exports thoth_core.thoth_lite.info;


    requires java.sql;
    requires json.simple;
    requires slf4j.api;
}