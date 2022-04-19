package thoth_core.thoth_lite.db_data.db_data_element.implement;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Partnership;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Purchasable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Storing;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Purchase
        implements Purchasable {

    public static final int DELIVERED = 1;

    private String orderNumber;
    private Partnership store;
    private LocalDate deliveryDate;
    private boolean isDelivered;
    private List<Storing> purchasedProducts;

    public Purchase(
            String orderNumber,
            Partnership store,
            LocalDate deliveryDate,
            boolean isDelivered
    ) {
        this.orderNumber = orderNumber;
        this.store = store;
        this.deliveryDate = deliveryDate;
        this.isDelivered = isDelivered;
        purchasedProducts = new LinkedList<>();
    }

    @Override
    public String getId() {
        return orderNumber;
    }

    @Override
    public void finish() {
        isDelivered = true;
    }

    @Override
    public LocalDate finishDate() {
        return deliveryDate;
    }

    @Override
    public String message() {
        return null;
    }

    @Override
    public boolean isFinish() {
        return isDelivered;
    }

    @Override
    public void setFinishDate(LocalDate finishDate) {
        this.deliveryDate = finishDate;
    }

    @Override
    public void setId(String id) {
        this.orderNumber = id;
    }

    @Override
    public List<Storing> getComposition() {
        return purchasedProducts;
    }

    @Override
    public void addStoring(Storing storing) {
        purchasedProducts.add(storing);
    }

    @Override
    public boolean containsStoring(Storing storing) {
        return purchasedProducts.contains(storing);
    }

    @Override
    public Storing getStoringByStoragableId(String id) {
        for(Storing storing : purchasedProducts){
            if(storing.getStoragable().getId().equals(id))
                return storing;
        }
        return null;
    }

    @Override
    public Storing getStoringByStoringId(String id) {
        for(Storing storing : purchasedProducts){
            if(storing.getId().equals(id))
                return storing;
        }
        return null;
    }

    @Override
    public boolean removeStoring(Storing storing) {
        return purchasedProducts.remove(storing);
    }

    @Override
    public Partnership getPartner() {
        return store;
    }

    @Override
    public void setPartner(Partnership partner) {
        this.store = partner;
    }

    @Override
    public boolean isDelivered() {
        return isDelivered;
    }

    @Override
    public void delivered() {
        isDelivered = true;
    }
}
