package thoth_core.thoth_lite.db_data.db_data_element.properties.parts;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Finance;

/**
 * Ценовое свойство объекта
 * */
public interface Pricing {

    /**
     * @return цена объекта
     * */
    Double getPrice();

    /**
     * Функция устанавливает цену
     * @param price цена объекта
     * */
    void setPrice(Double price);

    /**
     * @return валюта
     * */
    Finance getCurrency();

    /**
     * Функция устанавливает валюту.
     * @param currency валюта
     * */
    void setCurrency(Finance currency);

}
