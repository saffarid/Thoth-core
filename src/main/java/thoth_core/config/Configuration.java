package thoth_core.config;

import org.json.simple.JSONObject;

import java.util.concurrent.Flow;

public interface Configuration {

    /**
     * @return Конфигурация системы в формате json.
     */
    JSONObject getConfig();

    /**
     * @param json новая конфигурация системы.
     */
    void setConfig(JSONObject json);

    /**
     * @param key ключ доступа к перечислению
     *
     * @return Массив допустимых параметров
     */
    ConfigEnums[] getConfigEnums(String key);

}
