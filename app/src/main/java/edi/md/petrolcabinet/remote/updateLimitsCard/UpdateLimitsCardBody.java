package edi.md.petrolcabinet.remote.updateLimitsCard;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor on 28.08.2020
 */

public class UpdateLimitsCardBody {

    @SerializedName("AdditionalLimit")
    @Expose
    private Integer additionalLimit;
    @SerializedName("AdditionalLimitDate")
    @Expose
    private String additionalLimitDate;
    @SerializedName("DailyLimit")
    @Expose
    private Integer dailyLimit;
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
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("SeparateClientAccount")
    @Expose
    private Boolean separateClientAccount;
    @SerializedName("WeeklyLimit")
    @Expose
    private Integer weeklyLimit;

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

    public Integer getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Integer dailyLimit) {
        this.dailyLimit = dailyLimit;
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

}
