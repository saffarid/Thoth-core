package thoth_core.thoth_lite;

public enum SystemPublishers
        implements EnumPublicator {

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
