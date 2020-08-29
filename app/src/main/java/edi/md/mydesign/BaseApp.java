package edi.md.mydesign;


import android.app.Application;
import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.realm.objects.Company;
import edi.md.mydesign.realm.RealmMigrations;
import edi.md.mydesign.remote.cardDetailInfo.CardItem;
import edi.md.mydesign.remote.contract.CardsList;
import edi.md.mydesign.remote.contract.Contract;
import edi.md.mydesign.remote.petrolStation.PetrolStation;
import edi.md.mydesign.remote.transaction.Transaction;
import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by Igor on 16.06.2020
 */

public class BaseApp extends Application {
    public static BaseApp instance = null;

    private Company companyClicked;
    private ClientRealm clientClicked;
    private List<Contract> listContractClickedClient;
    private List<PetrolStation> petrolStations;
    private List<Transaction> transactions;
    private CardsList clickedCard;
    private Transaction clickedTransaction;

    private String matros;

    //data from authenticate card
    String sid;
    String cardId;
    String numberCod;
    String numberPhone;
    private boolean passExpired;
    private CardItem cardAccount;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Realm.init(this);

        final RealmConfiguration configuration = new RealmConfiguration.Builder().name("cabinet.realm").schemaVersion(1).migration(new RealmMigrations()).build();
        Realm.setDefaultConfiguration(configuration);
        Realm.getInstance(configuration);

        String trsf =  getSharedPreferences("bolbusMatros",MODE_PRIVATE).getString("MatrosSischin","");

        if(trsf.equals("")){
            KeyGenerator keyGenerator;
            SecretKey myWord;
            try {
                keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(256);
                myWord = keyGenerator.generateKey();

                matros = fun(myWord.getEncoded());

                getSharedPreferences("bolbusMatros",MODE_PRIVATE).edit().putString("MatrosSischin",matros).apply();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        else{
            matros = trsf;
        }
    }

    private String fun(byte[] decval) {
        String conVal= Base64.encodeToString(decval,Base64.DEFAULT);
        return conVal;
    }
    private byte[] derfun(String enval) {
        byte[] conVal = Base64.decode(enval,Base64.DEFAULT);
        return conVal;

    }

    public static BaseApp getAppInstance() {
        return instance;
    }

    public Company getCompanyClicked() {
        return companyClicked;
    }

    public void setCompanyClicked(Company itemClicked) {
        this.companyClicked = itemClicked;
    }

    public ClientRealm getClientClicked() {
        return clientClicked;
    }

    public void setClientClicked(ClientRealm clientClicked) {
        this.clientClicked = clientClicked;
    }

    public byte[] getHuyYou() {
        return derfun(matros);
    }

    public List<PetrolStation> getPetrolStations() {
        return petrolStations;
    }

    public void setPetrolStations(List<PetrolStation> petrolStations) {
        this.petrolStations = petrolStations;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public CardsList getClickedCard() {
        return clickedCard;
    }

    public void setClickedCard(CardsList clickedCard) {
        this.clickedCard = clickedCard;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getNumberCod() {
        return numberCod;
    }

    public void setNumberCod(String numberCod) {
        this.numberCod = numberCod;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public CardItem getCardAccount() {
        return cardAccount;
    }

    public void setCardAccount(CardItem cardAccount) {
        this.cardAccount = cardAccount;
    }

    public boolean isPassExpired() {
        return passExpired;
    }

    public void setPassExpired(boolean passExpired) {
        this.passExpired = passExpired;
    }

    public List<Contract> getListContractClickedClient() {
        return listContractClickedClient;
    }

    public void setListContractClickedClient(List<Contract> listContractClickedClient) {
        this.listContractClickedClient = listContractClickedClient;
    }

    public Transaction getClickedTransaction() {
        return clickedTransaction;
    }

    public void setClickedTransaction(Transaction clickedTransaction) {
        this.clickedTransaction = clickedTransaction;
    }
}
