package thoth_core.thoth_lite.db_lite_structure.full_structure;

public class StructureDescription {

    public enum TableTypes {
        TABLE("TABLE"),
        /**
         * Таблица-описание
         */
        DESC("DESC"),
        /**
         * Таблица-состав
         */
        COMPOSITE("COMPOSITE"),
        PROJECT_TABLE("PROJECT_TABLE"),
        /**
         * Таблица-список
         */
        GUIDE("GUIDE"),
        CONSTANTS("CONSTANTS"),
        SYSTEM_TABLE("SYSTEM_TABLE"),
        SYSTEM_GUIDE("SYSTEM_GUIDE"),
        SYSTEM_CONSTANTS("SYSTEM_CONSTANTS");
        private String type;

        TableTypes(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * Таблица информации о системе
     */
    public static class Info {
        public static final String TABLE_NAME = "info";
        public static final String ID = "key";
        public static final String VALUE = "value";
    }

    /* --- Таблицы со списочными данными --- */

    /**
     * Таблица едениц измерения
     */
    public static class CountTypes {
        public static final String TABLE_NAME = "count_type";
        public static final String ID = "id";
        public static final String COUNT_TYPE = "count_type";
    }

    /**
     * Таблица типов расходов
     */
    public static class ExpensesTypes {
        public static final String TABLE_NAME = "expenses_types";
        public static final String ID = "id";
        public static final String EXPENSES_TYPE = "expenses_type";
    }

    /**
     * Таблица категорий доходов
     */
    public static class IncomesTypes {
        public static final String TABLE_NAME = "income_types";
        public static final String ID = "id";
        public static final String INCOMES_TYPE = "income_type";
    }

    /**
     * Таблица статусов заказа
     */
    public static class OrderStatus {
        public static final String TABLE_NAME = "order_status";
        public static final String ID = "id";
        public static final String ORDER_STATUS = "order_status";
    }

    /**
     * Таблица типов продуктов
     */
    public static class ProductTypes {
        public static final String TABLE_NAME = "product_types";
        public static final String ID = "id";
        public static final String PRODUCT_TYPES = "product_types";
    }

    /**
     * Таблица типов проектов
     */
    public static class ProjectTypes {
        public static final String TABLE_NAME = "project_types";
        public static final String ID = "id";
        public static final String PROJECT_TYPE = "project_type";
    }

    /**
     * Таблица для хранения списка мест хранения продуктов
     */
    public static class Storage {
        public static final String TABLE_NAME = "storage";
        public static final String ID = "id";
        public static final String ADRESS = "adress";
//        public static final String COUNT = "count";
//        public static final String COUNT_TYPE_ID = "count_type_id";
//        public static final String PRODUCT_ID = "product_id";
    }

    /* --- Простые таблицы --- */

    /**
     * Таблица список валют
     */
    public static class Currency {
        public static final String TABLE_NAME = "currency";
        //        public static final String ID = "id";
        public static final String CURRENCY = "currency_code";
        public static final String COURSE = "course";
    }

    /**
     * Таблица учета расходов
     */
    public static class Expenses {
        public static final String TABLE_NAME = "expenses";
        public static final String ID = "id";
        public static final String EXPENSES_TYPE_ID = "expenses_type_id";
        public static final String VALUE = "value";
        public static final String DATE = "date";
        public static final String CURRENCY_ID = "currency_id";
        public static final String COURSE = "course";
        public static final String COMMENT = "comment";
    }

    /**
     * Таблица доходов
     */
    public static class Incomes {
        public static final String TABLE_NAME = "incomes";
        public static final String ID = "id";
        public static final String INCOMES_TYPE_ID = "incomes_type_id";
        public static final String VALUE = "value";
        public static final String DATE = "date";
        public static final String CURRENCY_ID = "currency_id";
        public static final String COURSE = "course";
        public static final String COMMENT = "comment";
    }

    /**
     * Таблица для хранения неиспользуемых продуктов
     */
    public static class NotUsed {
        public static final String TABLE_NAME = "not_used";
        public static final String ID = "id";
        //        public static final String ARTICLE = "article";
//        public static final String NAME = "name";
        public static final String CAUSE = "cause";
        public static final String PRODUCT_ID = "product_id";
    }

    /**
     * Таблица заказов от клиентов
     */
    public static class Orders {
        public static final String TABLE_NAME = "orders";
        public static final String ID = "order_id";
        public static final String CUSTOMER_ID = "customer_id";
        public static final String PROJECT_ID = "project_id";
        public static final String IS_MONTHLY = "is_monthly";
        public static final String DATE_START = "date_start";
        public static final String DATE_FINISH = "date_finish";
        public static final String STATUS_ID = "status_id";
        public static final String AUTOFINISH = "autofinish";
    }

    /**
     * Таблица партнеров
     */
    public static class Partners {
        public static final String TABLE_NAME = "partners";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String PHONE = "phone";
        public static final String WEB = "web";
        public static final String COMMENT = "comment";
    }

    /**
     * Таблица для хранения продуктов
     */
    public static class Products {
        public static final String TABLE_NAME = "products";
        //        public static final String ID = "id";
        public static final String ARTICLE = "article";
        public static final String NAME = "name";
        public static final String PRODUCT_TYPE_ID = "product_type_id";
        //        public static final String PRICE = "price";
//        public static final String CURRENCY_ID = "currency_id";
        public static final String ADRESS = "adress";
        public static final String COUNT = "count";
        public static final String COUNT_TYPE_ID = "count_type_id";
        public static final String NOTE = "note";
    }

    /* --- Составные таблицы --- */

    /**
     * Таблица покупок
     */
    public static class Purchase {

        public static final String NAME = "purchase";

        /**
         * Таблица-описание покупок
         */
        public static class Desc {
            public static final String TABLE_NAME = NAME + "_" + TableTypes.DESC.getType().toLowerCase();
            public static final String PURCHASE_ID = "purchase_id";
            public static final String STORE_ID = "store_id";
            public static final String DELIVERY_DATE = "delivery_date";
            public static final String IS_DELIVERED = "is_delivered";
        }

        /**
         * Таблица-состав покупок
         */
        public static class Composite {
            public static final String TABLE_NAME = NAME + "_" + TableTypes.COMPOSITE.getType().toLowerCase();
            public static final String ID = "id";
            public static final String PURCHASE_ID = Desc.PURCHASE_ID;
            public static final String PRODUCT_ID = "product_id";
            public static final String COUNT = "count";
            public static final String COUNT_TYPE_ID = "count_type_id";
            public static final String PRICE = "price";
            public static final String CURRENCY_ID = "currency_id";
            public static final String COURSE = "course";
        }
    }

    /**
     * Таблица проектов
     * */
    public static class Project {
        public static final String NAME = "project";
        /**
         * Таблица списка проектов
         */
        public static class Desc {
            public static final String TABLE_NAME = NAME + "_" + TableTypes.DESC.getType().toLowerCase();
            public static final String PROJECT_ID = "project_id";
            public static final String PROJECT_NAME = "name";
            public static final String PROJECT_TYPE_ID = "project_type_id";
            public static final String NOTE = "note";
        }

        /**
         * Таблица состава проектов
         */
        public static class Composite {
            public static final String TABLE_NAME = NAME + "_" + TableTypes.COMPOSITE.getType().toLowerCase();
            public static final String ID = "id";
            public static final String PROJECT_ID = Desc.PROJECT_ID;
            public static final String PRODUCT_ID = "product_id";
            public static final String COUNT = "count";
            public static final String COUNT_TYPE_ID = "count_type_id";
            public static final String PRICE = "price";
            public static final String CURRENCY_ID = "currency_id";
            public static final String COURSE = "course";
        }
    }

}
