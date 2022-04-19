package thoth_core.thoth_lite.db_data.db_data_element.implement;

import thoth_core.thoth_lite.db_data.db_data_element.properties.Partnership;

public class Partner
        implements Partnership
{

    private String id;
    private String name;
    private String phone;
    private String web;
    private String comment;

    public Partner(String id, String name, String phone, String web, String comment) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.web = web;
        this.comment = comment;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getWeb() {
        return web;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
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
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public void setWeb(String web) {
        this.web = web;
    }
}
