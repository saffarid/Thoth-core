module thoth {
    exports database;
    exports thoth_core;
    exports thoth_core.db_data.tables;
    exports thoth_core.db_data.db_data_element.properties;
    exports thoth_core.db_data.db_data_element.implement;
    exports thoth_core.exceptions;
    exports thoth_core.config;
    exports thoth_core.timer;
    exports thoth_core.info;

    requires java.sql;
    requires json.simple;
    requires slf4j.api;
}