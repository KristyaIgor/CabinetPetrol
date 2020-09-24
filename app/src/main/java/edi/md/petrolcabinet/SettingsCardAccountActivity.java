package edi.md.petrolcabinet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import edi.md.petrolcabinet.realm.objects.Accounts;
import io.realm.Realm;

public class SettingsCardAccountActivity extends AppCompatActivity {
    ImageButton btnBack;
    ConstraintLayout layoutInfo, layoutNotification, layoutLogout;
    Accounts clientRealm;
    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        Window window = getWindow();
        decorView.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        decorView.setFitsSystemWindows(true);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        setContentView(R.layout.activity_settings_card_account);
        layoutInfo = findViewById(R.id.layout_legal_information);
        layoutNotification = findViewById(R.id.layout_notification);
        layoutLogout = findViewById(R.id.layout_iesire);
        btnBack = findViewById(R.id.image_back_info);

        mRealm = Realm.getDefaultInstance();
        clientRealm = BaseApp.getAppInstance().getClientClicked();

        layoutInfo.setOnClickListener(view -> {
            Intent hist = new Intent(this, InformationCardAccountActivity.class);
            startActivity(hist);
        });
        layoutNotification.setOnClickListener(view -> {
            Intent hist = new Intent(this, NotificationActivity.class);
            startActivity(hist);
        });
        layoutLogout.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(this,R.style.MaterialAlertDialogCustom)
                    .setTitle(getString(R.string.title_exit_msg_rapid))
                    .setMessage(getString(R.string.msg_exit_dialog))
                    .setCancelable(false)
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
        mRealm.executeTransaction(realm -> {
            Accounts clients = realm.where(Accounts.class).equalTo("clientId", clientRealm.getId())
                    .and()
                    .equalTo("companyId", BaseApp.getAppInstance().getCompanyClicked().getId())
                    .findFirst();

            if(clients != null){
                clients.deleteFromRealm();
            }
        });
        finish();
        CardAccountActivity.activity.finish();
    }
}