
package edi.md.petrolcabinet.remote.client;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Client {

    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("Balance")
    @Expose
    private Double balance;
    @SerializedName("CardsBalance")
    @Expose
    private Double cardsBalance;
    @SerializedName("Contracts")
    @Expose
    private List<ContractInClient> contracts = null;
    @SerializedName("Credit")
    @Expose
    private Double credit;
    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("IDNP")
    @Expose
    private String iDNP;
    @SerializedName("MasterBalance")
    @Expose
    private Double masterBalance;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("NonInvoicedConsumptionAmount")
    @Expose
    private Double nonInvoicedConsumptionAmount;
    @SerializedName("Overdraft")
    @Expose
    private Double overdraft;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("TotalDebtSum")
    @Expose
    private Double totalDebtSum;
    @SerializedName("UnpaidDocuments")
    @Expose
    private List<UnpaidDocument> unpaidDocuments = null;
    @SerializedName("UnpaidInvoiceConsumptionAmount")
    @Expose
    private Double unpaidInvoiceConsumptionAmount;

    private byte[] phone;

    private byte[] password;

    private String sid;

    private int typeClient;

    private byte[] userName;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getCardsBalance() {
        return cardsBalance;
    }

    public void setCardsBalance(Double cardsBalance) {
        this.cardsBalance = cardsBalance;
    }

    public List<ContractInClient> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractInClient> contracts) {
        this.contracts = contracts;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIDNP() {
        return iDNP;
    }

    public void setIDNP(String iDNP) {
        this.iDNP = iDNP;
    }

    public Double getMasterBalance() {
        return masterBalance;
    }

    public void setMasterBalance(Double masterBalance) {
        this.masterBalance = masterBalance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getNonInvoicedConsumptionAmount() {
        return nonInvoicedConsumptionAmount;
    }

    public void setNonInvoicedConsumptionAmount(Double nonInvoicedConsumptionAmount) {
        this.nonInvoicedConsumptionAmount = nonInvoicedConsumptionAmount;
    }

    public Double getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(Double overdraft) {
        this.overdraft = overdraft;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getTotalDebtSum() {
        return totalDebtSum;
    }

    public void setTotalDebtSum(Double totalDebtSum) {
        this.totalDebtSum = totalDebtSum;
    }

    public List<UnpaidDocument> getUnpaidDocuments() {
        return unpaidDocuments;
    }

    public void setUnpaidDocuments(List<UnpaidDocument> unpaidDocuments) {
        this.unpaidDocuments = unpaidDocuments;
    }

    public Double getUnpaidInvoiceConsumptionAmount() {
        return unpaidInvoiceConsumptionAmount;
    }

    public void setUnpaidInvoiceConsumptionAmount(Double unpaidInvoiceConsumptionAmount) {
        this.unpaidInvoiceConsumptionAmount = unpaidInvoiceConsumptionAmount;
    }

    public byte[] getPhone() {
        return phone;
    }

    public void setPhone(byte[] phone) {
        this.phone = phone;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(int typeClient) {
        this.typeClient = typeClient;
    }

    public byte[] getUserName() {
        return userName;
    }

    public void setUserName(byte[] userName) {
        this.userName = userName;
    }
}
