package thoth_core.thoth_lite.db_data.db_data_element.implement;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Identifiable;

public class NotUse
        implements Identifiable {

    private String id;
    private Product product;
    private String cause;

    public NotUse(String id, Product product, String cause) {
        this.id = id;
        this.product = product;
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }

    @Override
    public String getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
