package edi.md.mydesign.realm.objects;


import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.remote.client.UnpaidDocument;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Igor on 09.07.2020
 */

public class ClientRealm extends RealmObject {

    private Double amount;
    private Double balance;
    private Double cardsBalance;
    private RealmList<ContractInClient> contracts = null;
    private Double credit;
    private String iDNP;
    private Double masterBalance;
    private String name;
    private Double nonInvoicedConsumptionAmount;
    private Double overdraft;
    private Integer status;
    private Double totalDebtSum;
    private RealmList<UnpaidDocument> unpaidDocuments = null;
    private Double unpaidInvoiceConsumptionAmount;
    private String companyId;
    private byte[] phone;
    private byte[] password;
    private byte[] userName;
    private int typeClient;
    private String sid;

    //forCard
    private byte[] pin;
    private String code;
    private String cardId;

    //notification
    private boolean fillingAccount;
    private boolean newDiscount;
    private boolean news;
    private boolean payAccount;

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

    public RealmList<ContractInClient> getContracts() {
        return contracts;
    }

    public void setContracts(RealmList<ContractInClient> contracts) {
        this.contracts = contracts;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
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

    public RealmList<UnpaidDocument> getUnpaidDocuments() {
        return unpaidDocuments;
    }

    public void setUnpaidDocuments(RealmList<UnpaidDocument> unpaidDocuments) {
        this.unpaidDocuments = unpaidDocuments;
    }

    public Double getUnpaidInvoiceConsumptionAmount() {
        return unpaidInvoiceConsumptionAmount;
    }

    public void setUnpaidInvoiceConsumptionAmount(Double unpaidInvoiceConsumptionAmount) {
        this.unpaidInvoiceConsumptionAmount = unpaidInvoiceConsumptionAmount;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public byte[] getUserName() {
        return userName;
    }

    public void setUserName(byte[] userName) {
        this.userName = userName;
    }

    public int getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(int typeClient) {
        this.typeClient = typeClient;
    }

    public byte[] getPin() {
        return pin;
    }

    public void setPin(byte[] pin) {
        this.pin = pin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public boolean isFillingAccount() {
        return fillingAccount;
    }

    public void setFillingAccount(boolean fillingAccount) {
        this.fillingAccount = fillingAccount;
    }

    public boolean isNewDiscount() {
        return newDiscount;
    }

    public void setNewDiscount(boolean newDiscount) {
        this.newDiscount = newDiscount;
    }

    public boolean isNews() {
        return news;
    }

    public void setNews(boolean news) {
        this.news = news;
    }

    public boolean isPayAccount() {
        return payAccount;
    }

    public void setPayAccount(boolean payAccount) {
        this.payAccount = payAccount;
    }
}
