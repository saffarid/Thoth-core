package thoth_core.thoth_lite.db_data.db_data_element.implement;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Finance;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Typable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Storing;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Storagable;

public class StorageCell
        implements Storing

{

    private String id;
    private Storagable product;
    private Double count;
    private Typable countType;
    private Double price;
    private Finance currency;
    private Double course;

    public StorageCell(
            String id
            , Storagable product
            , Double count
            , Typable countType
            , Double price
            , Finance currency
            , Double course
    ) {
        this.id = id;
        this.product = product;
        this.count = count;
        this.countType = countType;
        this.price = price;
        this.currency = currency;
        this.course = course;
    }

    @Override
    public Double getCount() {
        return count;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Typable getCountType() {
        return countType;
    }

    @Override
    public Storagable getStoragable() {
        return product;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setCount(Double count) {
        this.count = count;
    }

    @Override
    public void setCountType(Typable countType) {
        this.countType = countType;
    }

    @Override
    public void setStorageable(Storagable storageable) {
        this.product = storageable;
    }

    @Override
    public Double getCourse() {
        return course;
    }

    @Override
    public void setCourse(Double course) {
        this.course = course;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public Finance getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(Finance currency) {
        this.currency = currency;
    }

}
