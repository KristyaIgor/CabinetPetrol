package edi.md.mydesign.remote.cardDetailInfo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import edi.md.mydesign.remote.contract.ProductsList;
import edi.md.mydesign.remote.transaction.Transaction;

/**
 * Created by Igor on 12.08.2020
 */

public class CardItem {
    private byte[] phone;
    private byte[] pin;
    private String sId;
    private Double balanceAccount;
    private List<Transaction> transactions = null;
    private List<ProductsList> productsLists = null;
    private String client;
    private String contract;
    private String contractValidFrom;
    private String contractValidTo;

    @SerializedName("AdditionalLimit")
    @Expose
    private Integer additionalLimit;
    @SerializedName("AdditionalLimitDate")
    @Expose
    private String additionalLimitDate;
    @SerializedName("BalanceAccountCard")
    @Expose
    private Double balanceAccountCard;
    @SerializedName("CardAssortments")
    @Expose
    private List<CardItemAssortment> cardAssortments = null;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("DailyLimit")
    @Expose
    private Integer dailyLimit;
    @SerializedName("DailyLimitRemain")
    @Expose
    private Integer dailyLimitRemain;
    @SerializedName("DailyLimitUsed")
    @Expose
    private Integer dailyLimitUsed;
    @SerializedName("ForbidsChangesAssortmentsFromWeb")
    @Expose
    private Boolean forbidsChangesAssortmentsFromWeb;
    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("LimitType")
    @Expose
    private Integer limitType;
    @SerializedName("MonthlyLimit")
    @Expose
    private Integer monthlyLimit;
    @SerializedName("MonthlyLimitRemain")
    @Expose
    private Integer monthlyLimitRemain;
    @SerializedName("MonthlyLimitUsed")
    @Expose
    private Integer monthlyLimitUsed;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("SeparateClientAccount")
    @Expose
    private Boolean separateClientAccount;
    @SerializedName("WeeklyLimit")
    @Expose
    private Integer weeklyLimit;
    @SerializedName("WeeklyLimitRemain")
    @Expose
    private Integer weeklyLimitRemain;
    @SerializedName("WeeklyLimitUsed")
    @Expose
    private Integer weeklyLimitUsed;

    public Integer getAdditionalLimit() {
        return additionalLimit;
    }

    public void setAdditionalLimit(Integer additionalLimit) {
        this.additionalLimit = additionalLimit;
    }

    public String getAdditionalLimitDate() {
        return additionalLimitDate;
    }

    public void setAdditionalLimitDate(String additionalLimitDate) {
        this.additionalLimitDate = additionalLimitDate;
    }

    public Double getBalanceAccountCard() {
        return balanceAccountCard;
    }

    public void setBalanceAccountCard(Double balanceAccountCard) {
        this.balanceAccountCard = balanceAccountCard;
    }

    public List<CardItemAssortment> getCardAssortments() {
        return cardAssortments;
    }

    public void setCardAssortments(List<CardItemAssortment> cardAssortments) {
        this.cardAssortments = cardAssortments;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Integer dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public Integer getDailyLimitRemain() {
        return dailyLimitRemain;
    }

    public void setDailyLimitRemain(Integer dailyLimitRemain) {
        this.dailyLimitRemain = dailyLimitRemain;
    }

    public Integer getDailyLimitUsed() {
        return dailyLimitUsed;
    }

    public void setDailyLimitUsed(Integer dailyLimitUsed) {
        this.dailyLimitUsed = dailyLimitUsed;
    }

    public Boolean getForbidsChangesAssortmentsFromWeb() {
        return forbidsChangesAssortmentsFromWeb;
    }

    public void setForbidsChangesAssortmentsFromWeb(Boolean forbidsChangesAssortmentsFromWeb) {
        this.forbidsChangesAssortmentsFromWeb = forbidsChangesAssortmentsFromWeb;
    }

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getLimitType() {
        return limitType;
    }

    public void setLimitType(Integer limitType) {
        this.limitType = limitType;
    }

    public Integer getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(Integer monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public Integer getMonthlyLimitRemain() {
        return monthlyLimitRemain;
    }

    public void setMonthlyLimitRemain(Integer monthlyLimitRemain) {
        this.monthlyLimitRemain = monthlyLimitRemain;
    }

    public Integer getMonthlyLimitUsed() {
        return monthlyLimitUsed;
    }

    public void setMonthlyLimitUsed(Integer monthlyLimitUsed) {
        this.monthlyLimitUsed = monthlyLimitUsed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSeparateClientAccount() {
        return separateClientAccount;
    }

    public void setSeparateClientAccount(Boolean separateClientAccount) {
        this.separateClientAccount = separateClientAccount;
    }

    public Integer getWeeklyLimit() {
        return weeklyLimit;
    }

    public void setWeeklyLimit(Integer weeklyLimit) {
        this.weeklyLimit = weeklyLimit;
    }

    public Integer getWeeklyLimitRemain() {
        return weeklyLimitRemain;
    }

    public void setWeeklyLimitRemain(Integer weeklyLimitRemain) {
        this.weeklyLimitRemain = weeklyLimitRemain;
    }

    public Integer getWeeklyLimitUsed() {
        return weeklyLimitUsed;
    }

    public void setWeeklyLimitUsed(Integer weeklyLimitUsed) {
        this.weeklyLimitUsed = weeklyLimitUsed;
    }

    public byte[] getPhone() {
        return phone;
    }

    public void setPhone(byte[] phone) {
        this.phone = phone;
    }

    public byte[] getPin() {
        return pin;
    }

    public void setPin(byte[] pin) {
        this.pin = pin;
    }

    public String getSID() {
        return sId;
    }

    public void setSID(String sId) {
        this.sId = sId;
    }

    public Double getBalanceAccount() {
        return balanceAccount;
    }

    public void setBalanceAccount(Double balanceAccount) {
        this.balanceAccount = balanceAccount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<ProductsList> getProductsLists() {
        return productsLists;
    }

    public void setProductsLists(List<ProductsList> productsLists) {
        this.productsLists = productsLists;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getContractValidFrom() {
        return contractValidFrom;
    }

    public void setContractValidFrom(String contractValidFrom) {
        this.contractValidFrom = contractValidFrom;
    }

    public String getContractValidTo() {
        return contractValidTo;
    }

    public void setContractValidTo(String contractValidTo) {
        this.contractValidTo = contractValidTo;
    }
}
