package thoth_core.thoth_lite.db_data.db_data_element.implement;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Finance;
import thoth_core.thoth_lite.db_data.db_data_element.properties.FinancialAccounting;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Typable;

import java.time.LocalDate;

public class FinancialOperation
        implements FinancialAccounting {

    /**
     * Идентификатор операции
     * */
    private String id;
    /**
     * Тип покупки
     * */
    private Typable type;
    /**
     * Сумма покупки
     * */
    private Double value;
    /**
     * Дата покупки
     * */
    private LocalDate date;
    /**
     * Валюта покупки
     * */
    private Finance currency;
    /**
     * Курс валюты на момент покупки
     * */
    private Double course;
    /**
     * Комментарий к покупке
     * */
    private String comment;

    public FinancialOperation(String id, Typable type, Double value, LocalDate date, Finance currency, Double course, String comment) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.date = date;
        this.currency = currency;
        this.course = course;
        this.comment = comment;
    }

    @Override
    public Typable getCategory() {
        return type;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public Double getCourse() {
        return course;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public Finance getFinance() {
        return currency;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void setCategory(Typable typable) {
        this.type = typable;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void setCourse(Double course) {
        this.course = course;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public void setFinance(Finance finance) {
        this.currency = finance;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }
}
