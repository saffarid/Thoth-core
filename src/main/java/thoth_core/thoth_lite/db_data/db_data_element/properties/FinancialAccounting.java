package thoth_core.thoth_lite.db_data.db_data_element.properties;

import thoth_core.thoth_lite.db_data.db_data_element.properties.parts.Commentable;

import java.time.LocalDate;
import java.util.HashMap;

public interface FinancialAccounting
extends Identifiable,
        Commentable
{

    /**
     * @return тип финансового учёта
     * */
    Typable getCategory();
    /**
     * Установка типа финансового учёта
     * */
    void setCategory(Typable typable);

    /**
     * @return Дата совершения финансовой операции
     * */
    LocalDate getDate();
    /**
     * Установка финансовой операции
     * */
    void setDate(LocalDate date);

    /**
     * @return валюта совершённой операции
     * */
    Finance getFinance();
    /**
     * Установка валюты совершённой операции
     * */
    void setFinance(Finance finance);

    /**
     * @return курс валюты на момент совершения операции
     * */
    Double getCourse();
    /**
     * Установка курса валют на момент совершения операции
     * */
    void setCourse(Double course);

    /**
     * @return сумма финансовой операции
     * */
    Double getValue();
    /**
     * Установка суммы финансовой операции
     * */
    void setValue(Double value);

}
