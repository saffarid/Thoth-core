package thoth_core;

import thoth_core.db_data.db_data_element.properties.Finance;

public class Accountant {

    /**
     * Баланс
     * */
    private Double balance;
    /**
     * Валюта системы
     * */
    private Finance systemFinance;

    public Accountant() {
        CoreLogger.log.info("Init Accountant start");
        balance = 0.;

    }

    public Double getBalance() {
        return balance;
    }

    public Finance getSystemFinance() {
        return systemFinance;
    }
}
