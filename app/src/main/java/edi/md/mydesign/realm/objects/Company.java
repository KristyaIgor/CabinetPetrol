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
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("address")
    @Expose
    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumberContracts() {
        return numberContracts;
    }

    public void setNumberContracts(int numberContracts) {
        this.numberContracts = numberContracts;
    }
}