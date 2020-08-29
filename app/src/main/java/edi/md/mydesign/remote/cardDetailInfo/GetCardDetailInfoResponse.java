package edi.md.mydesign.remote.cardDetailInfo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import edi.md.mydesign.remote.contract.ProductsList;
import edi.md.mydesign.remote.transaction.Transaction;

/**
 * Created by Igor on 12.08.2020
 */

public class GetCardDetailInfoResponse {
    @SerializedName("ErrorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("BalanceAccount")
    @Expose
    private Double balanceAccount;
    @SerializedName("Card")
    @Expose
    private CardItem card;
    @SerializedName("Client")
    @Expose
    private String client;
    @SerializedName("Contract")
    @Expose
    private String contract;
    @SerializedName("ContractValidFrom")
    @Expose
    private String contractValidFrom;
    @SerializedName("ContractValidTo")
    @Expose
    private String contractValidTo;
    @SerializedName("Transactions")
    @Expose
    private List<Transaction> transactions = null;
    @SerializedName("ProductsList")
    @Expose
    private List<ProductsList> productsList = null;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Double getBalanceAccount() {
        return balanceAccount;
    }

    public void setBalanceAccount(Double balanceAccount) {
        this.balanceAccount = balanceAccount;
    }

    public CardItem getCard() {
        return card;
    }

    public void setCard(CardItem card) {
        this.card = card;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<ProductsList> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<ProductsList> productsList) {
        this.productsList = productsList;
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
