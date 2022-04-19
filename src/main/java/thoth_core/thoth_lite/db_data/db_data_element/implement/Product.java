package thoth_core.thoth_lite.db_data.db_data_element.implement;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Typable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Storagable;

public class Product
        implements Storagable {

    private String article;
    private String name;
    private Typable type;
    private Double count;
    private Typable countType;
    private Typable adress;
    private String note;

    public Product() {
        this.article = "";
        this.name = "";
        this.type = null;
        this.note = "";
    }

    public Product(String article, String name, Typable type, Double count, Typable countType, Typable adress) {
        this.article = article;
        this.name = name;
        this.type = type;
        this.count = count;
        this.countType = countType;
        this.adress = adress;
    }

    public Product(String article, String name, Typable type, Double count, Typable countType, Typable adress , String note) {
        this.article = article;
        this.name = name;
        this.type = type;
        this.count = count;
        this.countType = countType;
        this.adress = adress;
        this.note = note;
    }

    public boolean equals(Product obj) {
        if( (this.article != null && !this.article.equals(""))
                && (obj.getId() != null && !obj.getId().equals("")) ) {
            return this.article.equals(obj.getId());
        }else{
            return this.name.equals(obj.getName());
        }
    }

    @Override
    public String getId() {
        return article;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Typable getType() {
        return type;
    }

    @Override
    public void setId(String id) {
        this.article = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setType(Typable type) {

    }

    @Override
    public String toString() {
        return "Product{" +
                "article='" + article + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", note='" + note + '\'' +
                '}';
    }

    @Override
    public Double getCount() {
        return count;
    }

    @Override
    public void setCount(Double count) {
        this.count = count;
    }

    @Override
    public Typable getCountType() {
        return countType;
    }

    @Override
    public void setCountType(Typable countType) {
        this.countType = countType;
    }

    @Override
    public void setAdress(Typable adress) {
        this.adress = adress;
    }

    @Override
    public Typable getAdress() {
        return adress;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String getNote() {
        return note;
    }

}
