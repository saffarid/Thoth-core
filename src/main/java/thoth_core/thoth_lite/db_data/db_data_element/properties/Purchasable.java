package thoth_core.thoth_lite.db_data.db_data_element.properties;

import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Composite;
import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.HasPartner;

public interface Purchasable
    extends Identifiable
        , Composite
        , HasPartner
        , Finishable
{

    /**
     * Функция возвращает статус доставки
     * */
    boolean isDelivered();
    /**
     * Функция устанавливает покупку в доставленное состояние
     * */
    void delivered();

}
