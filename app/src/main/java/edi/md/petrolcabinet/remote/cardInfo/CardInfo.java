
package edi.md.petrolcabinet.remote.cardInfo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardInfo {

    @SerializedName("AdditionalLimit")
    @Expose
    private Integer additionalLimit;
    @SerializedName("AdditionalLimitDate")
    @Expose
    private Object additionalLimitDate;
    @SerializedName("BalanceAccountCard")
    @Expose
    private Integer balanceAccountCard;
    @SerializedName("CardAssortments")
    @Expose
    private List<CardInfoAssortment> cardAssortments = null;
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

    public Object getAdditionalLimitDate() {
        return additionalLimitDate;
    }

    public void setAdditionalLimitDate(Object additionalLimitDate) {
        this.additionalLimitDate = additionalLimitDate;
    }

    public Integer getBalanceAccountCard() {
        return balanceAccountCard;
    }

    public void setBalanceAccountCard(Integer balanceAccountCard) {
        this.balanceAccountCard = balanceAccountCard;
    }

    public List<CardInfoAssortment> getCardAssortments() {
        return cardAssortments;
    }

    public void setCardAssortments(List<CardInfoAssortment> cardAssortments) {
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

}
