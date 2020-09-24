
package edi.md.petrolcabinet.remote.cardInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardInfoAssortment {

    @SerializedName("AdditionalLimitDate")
    @Expose
    private String additionalLimitDate;
    @SerializedName("AditionalLimit")
    @Expose
    private Integer aditionalLimit;
    @SerializedName("AssortmentCode")
    @Expose
    private String assortmentCode;
    @SerializedName("AssortmentID")
    @Expose
    private String assortmentID;
    @SerializedName("DailyLimit")
    @Expose
    private Integer dailyLimit;
    @SerializedName("DailyLimitRemain")
    @Expose
    private double dailyLimitRemain;
    @SerializedName("DailyLimitUsed")
    @Expose
    private Integer dailyLimitUsed;
    @SerializedName("Discount")
    @Expose
    private Double discount;
    @SerializedName("Group")
    @Expose
    private String group;
    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("LimitType")
    @Expose
    private Integer limitType;
    @SerializedName("MonthLimitRemain")
    @Expose
    private double monthLimitRemain;
    @SerializedName("MonthLimitUsed")
    @Expose
    private Integer monthLimitUsed;
    @SerializedName("MonthlyLimit")
    @Expose
    private Integer monthlyLimit;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Price")
    @Expose
    private Double price;
    @SerializedName("WeeklyLimit")
    @Expose
    private Integer weeklyLimit;
    @SerializedName("WeeklyLimitRemain")
    @Expose
    private double weeklyLimitRemain;
    @SerializedName("WeeklyLimitUsed")
    @Expose
    private Integer weeklyLimitUsed;

    public String getAdditionalLimitDate() {
        return additionalLimitDate;
    }

    public void setAdditionalLimitDate(String additionalLimitDate) {
        this.additionalLimitDate = additionalLimitDate;
    }

    public Integer getAditionalLimit() {
        return aditionalLimit;
    }

    public void setAditionalLimit(Integer aditionalLimit) {
        this.aditionalLimit = aditionalLimit;
    }

    public String getAssortmentCode() {
        return assortmentCode;
    }

    public void setAssortmentCode(String assortmentCode) {
        this.assortmentCode = assortmentCode;
    }

    public String getAssortmentID() {
        return assortmentID;
    }

    public void setAssortmentID(String assortmentID) {
        this.assortmentID = assortmentID;
    }

    public Integer getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Integer dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public double getDailyLimitRemain() {
        return dailyLimitRemain;
    }

    public void setDailyLimitRemain(double dailyLimitRemain) {
        this.dailyLimitRemain = dailyLimitRemain;
    }

    public Integer getDailyLimitUsed() {
        return dailyLimitUsed;
    }

    public void setDailyLimitUsed(Integer dailyLimitUsed) {
        this.dailyLimitUsed = dailyLimitUsed;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public double getMonthLimitRemain() {
        return monthLimitRemain;
    }

    public void setMonthLimitRemain(double monthLimitRemain) {
        this.monthLimitRemain = monthLimitRemain;
    }

    public Integer getMonthLimitUsed() {
        return monthLimitUsed;
    }

    public void setMonthLimitUsed(Integer monthLimitUsed) {
        this.monthLimitUsed = monthLimitUsed;
    }

    public Integer getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(Integer monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getWeeklyLimit() {
        return weeklyLimit;
    }

    public void setWeeklyLimit(Integer weeklyLimit) {
        this.weeklyLimit = weeklyLimit;
    }

    public double getWeeklyLimitRemain() {
        return weeklyLimitRemain;
    }

    public void setWeeklyLimitRemain(double weeklyLimitRemain) {
        this.weeklyLimitRemain = weeklyLimitRemain;
    }

    public Integer getWeeklyLimitUsed() {
        return weeklyLimitUsed;
    }

    public void setWeeklyLimitUsed(Integer weeklyLimitUsed) {
        this.weeklyLimitUsed = weeklyLimitUsed;
    }

}
