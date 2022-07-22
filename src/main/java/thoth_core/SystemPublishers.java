package thoth_core;

public enum SystemPublishers
        implements Publicator {

    /* Системы оповещения наступления событий */
    /**
     * Система оповещения доставки покупок
     */
    DeliveryNotificationSystem,
    /**
     * Система оповещения необходимости завершения заказа
     */
    OrderNotificationSystem,

}
