
package edi.md.mydesign.remote.contract;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Contract {

    @SerializedName("Bonus")
    @Expose
    private Integer bonus;
    @SerializedName("CardsBalance")
    @Expose
    private Double cardsBalance;
    @SerializedName("CardsList")
    @Expose
    private List<CardsList> cardsList = null;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("DateValidFrom")
    @Expose
    private String dateValidFrom;
    @SerializedName("DateValidTo")
    @Expose
    private String dateValidTo;
    @SerializedName("PaymentDelay")
    @Expose
    private Integer paymentDelay;
    @SerializedName("ProductsList")
    @Expose
    private List<ProductsList> productsList = null;
    @SerializedName("Status")
    @Expose
    private Integer status;

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public Double getCardsBalance() {
        return cardsBalance;
    }

    public void setCardsBalance(Double cardsBalance) {
        this.cardsBalance = cardsBalance;
    }

    public List<CardsList> getCardsList() {
        return cardsList;
    }

    public void setCardsList(List<CardsList> cardsList) {
        this.cardsList = cardsList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDateValidFrom() {
        return dateValidFrom;
    }

    public void setDateValidFrom(String dateValidFrom) {
        this.dateValidFrom = dateValidFrom;
    }

    public String getDateValidTo() {
        return dateValidTo;
    }

    public void setDateValidTo(String dateValidTo) {
        this.dateValidTo = dateValidTo;
    }

    public Integer getPaymentDelay() {
        return paymentDelay;
    }

    public void setPaymentDelay(Integer paymentDelay) {
        this.paymentDelay = paymentDelay;
    }

    public List<ProductsList> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<ProductsList> productsList) {
        this.productsList = productsList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
