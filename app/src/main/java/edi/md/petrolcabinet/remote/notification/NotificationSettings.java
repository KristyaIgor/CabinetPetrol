package edi.md.petrolcabinet.remote.notification;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor on 21.08.2020
 */

public class NotificationSettings {
    @SerializedName("FillingAccount")
    @Expose
    private Boolean fillingAccount;
    @SerializedName("NewDiscount")
    @Expose
    private Boolean newDiscount;
    @SerializedName("News")
    @Expose
    private Boolean news;
    @SerializedName("PayByAccount")
    @Expose
    private Boolean payByAccount;
    @SerializedName("SID")
    @Expose
    private String sID;
    @SerializedName("Token")
    @Expose
    private String token;

    public Boolean getFillingAccount() {
        return fillingAccount;
    }

    public void setFillingAccount(Boolean fillingAccount) {
        this.fillingAccount = fillingAccount;
    }

    public Boolean getNewDiscount() {
        return newDiscount;
    }

    public void setNewDiscount(Boolean newDiscount) {
        this.newDiscount = newDiscount;
    }

    public Boolean getNews() {
        return news;
    }

    public void setNews(Boolean news) {
        this.news = news;
    }

    public Boolean getPayByAccount() {
        return payByAccount;
    }

    public void setPayByAccount(Boolean payByAccount) {
        this.payByAccount = payByAccount;
    }

    public String getSID() {
        return sID;
    }

    public void setSID(String sID) {
        this.sID = sID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
