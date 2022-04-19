package thoth_core.thoth_lite;

import thoth_core.thoth_lite.db_data.DBData;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Finance;
import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription;
import thoth_core.thoth_lite.exceptions.NotContainsException;

import java.util.HashMap;

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
