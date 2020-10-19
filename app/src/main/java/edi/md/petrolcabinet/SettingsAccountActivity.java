package edi.md.petrolcabinet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.remote.client.ContractInClient;
import edi.md.petrolcabinet.remote.client.UnpaidDocument;
import io.realm.Realm;
import io.realm.RealmList;

public class SettingsAccountActivity extends AppCompatActivity {
    ImageButton btnBack;
    ConstraintLayout layoutNotification, layoutPassword, layoutLogout;
    Accounts clientRealm;
    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings_account);
//        layoutInfo = findViewById(R.id.layout_legal_information);
        layoutNotification = findViewById(R.id.layout_notification);
        layoutPassword = findViewById(R.id.layout_password);
        layoutLogout = findViewById(R.id.layout_iesire);
        btnBack = findViewById(R.id.image_back_info);

        mRealm = Realm.getDefaultInstance();
        clientRealm = BaseApp.getAppInstance().getClientClicked();


//        layoutInfo.setOnClickListener(view -> {
//            Intent hist = new Intent(this, InfoAccountActivity.class);
//            startActivity(hist);
//        });
        layoutNotification.setOnClickListener(view -> {
            Intent hist = new Intent(this, NotificationActivity.class);
            startActivity(hist);
        });
        layoutPassword.setOnClickListener(view -> {
            Intent hist = new Intent(this, ChangePasswordActivity.class);
            startActivity(hist);
        });
        layoutLogout.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(this,R.style.MaterialAlertDialogCustom)
//                    .setTitle(getString(R.string.title_exit_msg_rapid))
                    .setMessage(getString(R.string.msg_exit_dialog))
                    .setPositiveButton(getString(R.string.btn_yes_dialog), (dialogInterface, i) -> {
                       logOutClient();
                    })
                    .setNegativeButton(getString(R.string.btn_no_dialog),(dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        });

        btnBack.setOnClickListener(view -> {
            setResult(RESULT_OK);
            finish();
        });
    }

    private void logOutClient(){
        if(clientRealm.getTypeClient() == 1){
            mRealm.executeTransaction(realm -> {
                Accounts clients = realm.where(Accounts.class).equalTo("iDNP", clientRealm.getIDNP())
                        .and()
                        .equalTo("userName",clientRealm.getUserName())
                        .and()
                        .equalTo("password",clientRealm.getPassword())
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
        }
        else {
            mRealm.executeTransaction(realm -> {
                Accounts clients = realm.where(Accounts.class).equalTo("phone", clientRealm.getPhone())
                        .and()
                        .equalTo("password",clientRealm.getPassword())
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
        }
        BaseApp.getAppInstance().setClientClicked(null);
        setResult(1593); //1593
        finish();
    }
}