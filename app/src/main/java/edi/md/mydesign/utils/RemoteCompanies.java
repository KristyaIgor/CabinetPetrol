package edi.md.mydesign.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import edi.md.mydesign.realm.objects.Company;

/**
 * Created by Igor on 18.06.2020
 */


public class RemoteCompanies {

    @SerializedName("companies")
    @Expose
    private List<Company> companies = null;

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

}