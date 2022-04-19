package thoth_core.thoth_lite.config.impl;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import thoth_core.thoth_lite.CoreLogger;
import thoth_core.thoth_lite.config.*;

import java.io.*;

public class Config
        implements Configuration {

    private static Config config;

    /**
     * Путь сохранения конфигурации
     */
    private final String pathConfig = "config";
    /**
     * Наименование файла конфигурации
     */
    private final String fileConfigName = "thoth_core_config.json";

    private final File configFile = new File(
            pathConfig + File.separator + fileConfigName
    );

    /**
     * Конфигурация работы базы данных
     */
    private final Database database = new Database();

    /**
     * Конфигурация работы системы оповещения
     */
    private final Delivered delivered = new Delivered();

    private Config() {
        try {
            if (!configFile.exists()) {
                exportConfig();
            } else {
                setConfig(importConfig());
            }
        } catch (IOException | ParseException e) {
            CoreLogger.log.error(e.getMessage(), e);
        }
    }

    public void exportConfig() {
        CoreLogger.log.info("Export core-config");
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdir();
        }
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(((JSONObject) getConfig()).toJSONString());
        } catch (IOException e) {
            CoreLogger.log.error("Export core-config error ", e);
        }

    }

    @Override
    public JSONObject getConfig() {
        JSONObject config = new JSONObject();

        config.put(Keys.Section.DATABASE.getKey(), database.getConfig());
        config.put(Keys.Section.DELIVERY.getKey(), delivered.getConfig());

        return config;
    }

    @Override
    public ConfigEnums[] getConfigEnums(String key) {
        if (key.equals(Keys.Database.DELAY_AUTOUPDATE.getKey())) {
            return PeriodAutoupdateDatabase.values();
        } else if (key.equals(Keys.Delivery.DAY_BEFORE_DELIVERY.getKey())) {
            return DayBeforeDelivery.values();
        }
        return null;
    }

    public Database getDatabase() {
        return database;
    }

    public Delivered getDelivered() {
        return delivered;
    }

    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    private JSONObject importConfig()
            throws IOException, ParseException {
        CoreLogger.log.info("Read core-config file");
        FileReader reader = new FileReader(configFile);
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(reader);
    }

    @Override
    public void setConfig(JSONObject json) {
        CoreLogger.log.info("Set new core-config");
        database.setConfig((JSONObject) json.get(Keys.Section.DATABASE.getKey()));
        delivered.setConfig((JSONObject) json.get(Keys.Section.DELIVERY.getKey()));
    }

    /**
     * Конфигурация работы базы данных
     */
    public class Database
            implements Configuration {

        /* --- Ключи конфигурации --- */

        /**
         * Ключ доступа к флагу автоматического перечитывания базы
         */
        private final String KEY_AUTOUPDATE = Keys.Database.AUTOUPDATE.getKey();
        /**
         * Ключ доступа к флагу автоматического перечитывания таблицы после выполнения транзакции
         */
        private final String KEY_UPDATE_AFTER_TRANS = Keys.Database.UPDATE_AFTER_TRANS.getKey();
        /**
         * Ключ доступа к значению задержки между автоматическим считыванием базы
         */
        private final String KEY_DELAY_AUTOUPDATE = Keys.Database.DELAY_AUTOUPDATE.getKey();

        /* --- Параметры конфигурации --- */

        /**
         * Флаг автоматического считывания базы данных
         */
        private boolean isAutoupdate;
        /**
         * Флаг автоматического считывания после совершения транзакции
         */
        private boolean isUpdateAfterTrans;
        /**
         * Флаг автоматического периодического перечитывания БД
         */
        private PeriodAutoupdateDatabase period;

        public Database() {
            isAutoupdate = false;
            isUpdateAfterTrans = false;
            period = PeriodAutoupdateDatabase.NEVER;
        }

        @Override
        public JSONObject getConfig() {
            JSONObject res = new JSONObject();

            res.put(KEY_AUTOUPDATE, isAutoupdate);
            res.put(KEY_UPDATE_AFTER_TRANS, isUpdateAfterTrans);
            res.put(KEY_DELAY_AUTOUPDATE, period.getName());

            return res;
        }

        @Override
        public void setConfig(JSONObject json) {
            if (json == null) return;
            isAutoupdate = (boolean) json.get(KEY_AUTOUPDATE);
            isUpdateAfterTrans = (boolean) json.get(KEY_UPDATE_AFTER_TRANS);
            period = PeriodAutoupdateDatabase.valueOf((String) json.get(KEY_DELAY_AUTOUPDATE));
        }

        /* --- Getter --- */

        public boolean isAutoupdate() {
            return isAutoupdate;
        }

        public boolean isUpdateAfterTrans() {
            return isUpdateAfterTrans;
        }

        public PeriodAutoupdateDatabase getPeriod() {
            return period;
        }


        @Override
        public ConfigEnums[] getConfigEnums(String key) {
            return null;
        }
    }

    /**
     * Конфигурация работы с объектами доставки
     */
    public class Delivered
            implements Configuration {

        /* --- Ключи --- */

        private final String KEY_IS_CHECKDAY_BEFORE_DELIVERY = Keys.Delivery.IS_CHECKDAY_BEFORE_DELIVERY.getKey();
        private final String KEY_DAY_BEFORE_DELIVERY = Keys.Delivery.DAY_BEFORE_DELIVERY.getKey();

        /* --- Параметры конфигурации --- */

        private boolean checkDayBeforeDelivery;
        private final boolean checkDayBeforeDeliveryDefault = true;
        /**
         * За сколько дней перед доставкой система должна начать оповещять пользователя
         */
        private DayBeforeDelivery dayBeforeDelivery;
        private final DayBeforeDelivery dayBeforeDeliveryDefault = DayBeforeDelivery.FIVE;

        public Delivered() {
            checkDayBeforeDelivery = checkDayBeforeDeliveryDefault;
            dayBeforeDelivery = dayBeforeDeliveryDefault;
        }

        @Override
        public JSONObject getConfig() {
            JSONObject res = new JSONObject();

            res.put(KEY_IS_CHECKDAY_BEFORE_DELIVERY, checkDayBeforeDelivery);
            res.put(KEY_DAY_BEFORE_DELIVERY, dayBeforeDelivery.getName());

            return res;
        }

        @Override
        public void setConfig(JSONObject json) {
            if (json == null) return;
            checkDayBeforeDelivery = (boolean) json.get(KEY_IS_CHECKDAY_BEFORE_DELIVERY);
            dayBeforeDelivery = DayBeforeDelivery.valueOf((String) json.get(KEY_DAY_BEFORE_DELIVERY));
        }

        @Override
        public ConfigEnums[] getConfigEnums(String key) {
            return null;
        }

        /* --- Getter --- */

        public boolean isCheckDayBeforeDelivery() {
            return checkDayBeforeDelivery;
        }

        public DayBeforeDelivery getDayBeforeDelivery() {
            return dayBeforeDelivery;
        }

        /* --- Setter --- */

        public void setCheckDayBeforeDelivery(boolean checkDayBeforeDelivery) {
            this.checkDayBeforeDelivery = checkDayBeforeDelivery;
        }

        public void setDayBeforeDelivery(DayBeforeDelivery dayBeforeDelivery) {
            this.dayBeforeDelivery = dayBeforeDelivery;
        }
    }
}
