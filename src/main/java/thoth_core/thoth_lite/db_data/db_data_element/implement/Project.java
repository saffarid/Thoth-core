package thoth_core.thoth_lite.db_data.db_data_element.implement;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Typable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Projectable;
import thoth_core.thoth_lite.db_data.db_data_element.properties.Storing;

import java.util.LinkedList;
import java.util.List;

public class Project
        implements Projectable {

    private String id;
    private String name;
    private Typable type;
    private String note;
    private List<Storing> composite;

    public Project(
            String id,
            String name,
            Typable type,
            String note
    ) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.note = note;
        this.composite = new LinkedList<>();
    }

    @Override
    public List<Storing> getComposition() {
        return composite;
    }

    @Override
    public void addStoring(Storing storing) {
        composite.add(storing);
    }

    @Override
    public boolean containsStoring(Storing storing) {
        return composite.contains(storing);
    }

    @Override
    public Storing getStoringByStoragableId(String id) {
        for(Storing storing : composite){
            if(storing.getStoragable().getId().equals(id))
                return storing;
        }
        return null;
    }

    @Override
    public Storing getStoringByStoringId(String id) {
        for(Storing storing : composite){
            if(storing.getId().equals(id))
                return storing;
        }
        return null;
    }

    @Override
    public boolean removeStoring(Storing storing) {
        return composite.remove(storing);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public Typable getType() {
        return type;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public void setType(Typable type) {
        this.type = type;
    }
}
