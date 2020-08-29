package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.remote.client.UnpaidDocument;
import io.realm.Realm;
import io.realm.RealmList;

public class InformationActivity extends AppCompatActivity {

    Button logOut;
    ClientRealm client;

    ImageButton btnBack;

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        Window window = getWindow();
        decorView.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        decorView.setFitsSystemWindows(true);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        setContentView(R.layout.activity_information);

        logOut = findViewById(R.id.button_logOut);
        btnBack = findViewById(R.id.image_back_info);

        mRealm = Realm.getDefaultInstance();

        Intent intent = getIntent();

        boolean juridic = intent.getBooleanExtra("JuridicAccount", false);

        logOut.setOnClickListener(view -> {
            if(juridic){
                client = BaseApp.getAppInstance().getClientClicked();
                mRealm.executeTransaction(realm -> {
                    ClientRealm clients = realm.where(ClientRealm.class).equalTo("iDNP", client.getIDNP())
                            .and()
                            .equalTo("companyId", BaseApp.getAppInstance().getCompanyClicked().getId()).findFirst();

                    if(clients != null){
                        RealmList<UnpaidDocument> unpaidDocuments = clients.getUnpaidDocuments();
                        if(!unpaidDocuments.isEmpty())
                            unpaidDocuments.deleteAllFromRealm();

                        RealmList<ContractInClient> contracts = clients.getContracts();
                        if(!contracts.isEmpty())
                            contracts.deleteAllFromRealm();

                        clients.deleteFromRealm();
                    }
                });

                BaseApp.getAppInstance().setClientClicked(null);
                setResult(159753);
                finish();
            }
        });

        btnBack.setOnClickListener(view -> {
            setResult(RESULT_OK);
            finish();
        });
    }
}