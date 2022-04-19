package thoth_core.thoth_lite.config;

public class Keys {

    /**
     * Ключи для доступа к разделам конфигурации
     * */
    public enum Section {
        /**
         * Ключ раздела конфигурации базы данных
         * */
        DATABASE("database"),
        /**
         * Ключ раздела конфигурации системы оповещения доставки
         * */
        DELIVERY("delivery"),
        ;
        private String key;
        Section(String key) {
            this.key = key;
        }
        public String getKey() {
            return key;
        }
    }

    /**
     * Ключи конфигурации раздела базы данных
     * */
    public enum Database{
        /**
         * Ключ доступа к флагу автоматического перечитывания базы
         */
        AUTOUPDATE("autoupdate"),
        /**
         * Ключ доступа к флагу автоматического перечитывания таблицы после выполнения транзакции
         */
        UPDATE_AFTER_TRANS("update_after_trans"),
        /**
         * Ключ доступа к значению задержки между автоматическим считыванием базы
         */
        DELAY_AUTOUPDATE("delay_autoupdate")
        ;
        private String key;
        Database(String key) {
            this.key = key;
        }
        public String getKey() {
            return key;
        }
    }

    /**
     * Ключи конфигурации раздела системы оповещения доставки
     * */
    public enum Delivery{
        /**
         * Ключ активности системы оповещения
         * */
        IS_CHECKDAY_BEFORE_DELIVERY("check_day_before_delivery"),
        /**
         * Кол-во дней до доставки для начала оповещений
         * */
        DAY_BEFORE_DELIVERY("day_before_delivery"),
        ;
        private String key;
        Delivery(String key) {
            this.key = key;
        }
        public String getKey() {
            return key;
        }
    }

}
