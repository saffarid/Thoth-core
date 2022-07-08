package thoth_core.thoth_lite;

public enum AvailablePublishers{

    /* Таблицы */
    CountTypes,
    ExpensesTypes,
    IncomesTypes,
    OrderStatus,
    ProductTypes,
    Storing,

    Currencies,
    Expenses,
    Incomes,
    Orderable,
    Partners,
    Storagable,
    Purchasable,
    Projectable,

    /* Системы оповещения наступления событий */
    /**
     * Система оповещения доставки покупок
     * */
    DeliveryNotificationSystem,
    /**
     * Система оповещения необходимости завершения заказа
     * */
    OrderNotificationSystem,

}
