package edi.md.petrolcabinet.remote.transaction;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor on 07.07.2020
 */

public class Transaction {
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("Assortment")
    @Expose
    private String assortment;
    @SerializedName("CardCode")
    @Expose
    private String cardCode;
    @SerializedName("CardName")
    @Expose
    private String cardName;
    @SerializedName("DocumentDate")
    @Expose
    private String documentDate;
    @SerializedName("DocumentNr")
    @Expose
    private String documentNr;
    @SerializedName("DocumentTime")
    @Expose
    private String documentTime;
    @SerializedName("Quantity")
    @Expose
    private Double quantity;
    @SerializedName("Station")
    @Expose
    private String station;
    @SerializedName("TransactionID")
    @Expose
    private Integer transactionID;
    @SerializedName("TransactionTypes")
    @Expose
    private String transactionTypes;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAssortment() {
        return assortment;
    }

    public void setAssortment(String assortment) {
        this.assortment = assortment;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentNr() {
        return documentNr;
    }

    public void setDocumentNr(String documentNr) {
        this.documentNr = documentNr;
    }

    public String getDocumentTime() {
        return documentTime;
    }

    public void setDocumentTime(String documentTime) {
        this.documentTime = documentTime;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Integer getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionTypes() {
        return transactionTypes;
    }

    public void setTransactionTypes(String transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

}