package edi.md.mydesign;


import android.app.Application;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.realm.objects.Company;
import edi.md.mydesign.realm.RealmMigrations;
import edi.md.mydesign.remote.client.Client;
import edi.md.mydesign.remote.contract.Contract;
import edi.md.mydesign.remote.petrolStation.PetrolStation;
import edi.md.mydesign.remote.transaction.Transaction;
import edi.md.mydesign.utils.CompaniesHelper;
import edi.md.mydesign.utils.RemoteCompanies;
import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by Igor on 16.06.2020
 */

public class BaseApp extends Application {
    public static BaseApp instance = null;

    private Company companyClicked;
    private ClientRealm clientClicked;
    private Contract clickedClientContract;
    private List<PetrolStation> petrolStations;
    private List<Transaction> transactions;

    private String matros;

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

    public Contract getClickedClientContract() {
        return clickedClientContract;
    }

    public void setClickedClientContract(Contract clickedClientContract) {
        this.clickedClientContract = clickedClientContract;
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
}
