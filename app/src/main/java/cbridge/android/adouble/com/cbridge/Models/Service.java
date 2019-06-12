package cbridge.android.adouble.com.cbridge.Models;

public class Service {
    private String id;
    private String title;
    private String description;
    private String value;
    private String iduser;

    public Service(){

    }

    public Service(String title, String description, String value, String iduser) {
        this.title = title;
        this.description = description;
        this.value = value;
        this.iduser = iduser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }
}
