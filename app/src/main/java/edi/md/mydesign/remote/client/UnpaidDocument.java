
package edi.md.mydesign.remote.client;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UnpaidDocument extends RealmObject {

    @SerializedName("AreDaysLeftUntilDebtIsRepaid")
    @Expose
    private Integer areDaysLeftUntilDebtIsRepaid;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("DocumentDate")
    @Expose
    private String documentDate;
    @SerializedName("DocumentSum")
    @Expose
    private Double documentSum;
    @SerializedName("OverdueDebtDays")
    @Expose
    private Integer overdueDebtDays;
    @SerializedName("OverdueDebtSum")
    @Expose
    private Double overdueDebtSum;
    @SerializedName("PaySum")
    @Expose
    private Double paySum;
    @SerializedName("PaymentDelay")
    @Expose
    private Integer paymentDelay;
    @SerializedName("TheLastPaymentDate")
    @Expose
    private String theLastPaymentDate;
    @SerializedName("UnpaidSum")
    @Expose
    private Double unpaidSum;

    public Integer getAreDaysLeftUntilDebtIsRepaid() {
        return areDaysLeftUntilDebtIsRepaid;
    }

    public void setAreDaysLeftUntilDebtIsRepaid(Integer areDaysLeftUntilDebtIsRepaid) {
        this.areDaysLeftUntilDebtIsRepaid = areDaysLeftUntilDebtIsRepaid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public Double getDocumentSum() {
        return documentSum;
    }

    public void setDocumentSum(Double documentSum) {
        this.documentSum = documentSum;
    }

    public Integer getOverdueDebtDays() {
        return overdueDebtDays;
    }

    public void setOverdueDebtDays(Integer overdueDebtDays) {
        this.overdueDebtDays = overdueDebtDays;
    }

    public Double getOverdueDebtSum() {
        return overdueDebtSum;
    }

    public void setOverdueDebtSum(Double overdueDebtSum) {
        this.overdueDebtSum = overdueDebtSum;
    }

    public Double getPaySum() {
        return paySum;
    }

    public void setPaySum(Double paySum) {
        this.paySum = paySum;
    }

    public Integer getPaymentDelay() {
        return paymentDelay;
    }

    public void setPaymentDelay(Integer paymentDelay) {
        this.paymentDelay = paymentDelay;
    }

    public String getTheLastPaymentDate() {
        return theLastPaymentDate;
    }

    public void setTheLastPaymentDate(String theLastPaymentDate) {
        this.theLastPaymentDate = theLastPaymentDate;
    }

    public Double getUnpaidSum() {
        return unpaidSum;
    }

    public void setUnpaidSum(Double unpaidSum) {
        this.unpaidSum = unpaidSum;
    }

}
