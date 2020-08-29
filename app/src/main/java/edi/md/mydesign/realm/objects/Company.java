package edi.md.mydesign.realm.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


/**
 * Created by Igor on 18.06.2020
 */
public class Company extends RealmObject {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("serviceName")
    @Expose
    private String serviceName;
    @SerializedName("logo")
    @Expose
    private String logo;

    boolean preparingToDelete;

    boolean existContracts = false;

    private int numberContracts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isPreparingToDelete() {
        return preparingToDelete;
    }

    public void setPreparingToDelete(boolean preparingToDelete) {
        this.preparingToDelete = preparingToDelete;
    }

    public boolean isExistContracts() {
        return existContracts;
    }

    public void setExistContracts(boolean existContracts) {
        this.existContracts = existContracts;
    }

    public int getNumberContracts() {
        return numberContracts;
    }

    public void setNumberContracts(int numberContracts) {
        this.numberContracts = numberContracts;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}