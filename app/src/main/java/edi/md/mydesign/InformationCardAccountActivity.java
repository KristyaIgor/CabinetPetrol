package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.cardDetailInfo.CardItem;
import io.realm.Realm;

public class InformationCardAccountActivity extends AppCompatActivity {

    TextView text;

    ImageButton btnBack;

    Button logOut;

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        Window window = getWindow();
        decorView.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        decorView.setFitsSystemWindows(true);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        setContentView(R.layout.activity_information_card_account);

        text = findViewById(R.id.text_information_card);
        btnBack = findViewById(R.id.image_back_info);
        logOut = findViewById(R.id.btn_logOut_card_account);

        mRealm = Realm.getDefaultInstance();

        CardItem card = BaseApp.getAppInstance().getCardAccount();

        String cardName = card.getName();
        String client = card.getClient();
        String contract = card.getContract();
        String company = BaseApp.getAppInstance().getCompanyClicked().getName();
        String validFrom = card.getContractValidFrom();
        validFrom = validFrom.replace("/Date(","");
        validFrom = validFrom.replace("+0200)/","");
        validFrom = validFrom.replace("+0300)/","");

        String validTo = card.getContractValidTo();
        validTo = validTo.replace("/Date(","");
        validTo = validTo.replace("+0200)/","");
        validTo = validTo.replace("+0300)/","");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Chisinau");
        simpleDateFormat.setTimeZone(timeZone);

        long validFromLong = Long.parseLong(validFrom);
        validFrom = simpleDateFormat.format(validFromLong);
        long validFromTo = Long.parseLong(validTo);
        validTo = simpleDateFormat.format(validFromTo);

        String validate = validFrom + " - " + validTo;

        text.setText(getResources().getString(R.string.info_card_detail_account, client, contract, validate, cardName,company));

        btnBack.setOnClickListener(view -> {
            setResult(RESULT_OK);
            finish();
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.executeTransaction(realm -> {
                    ClientRealm clients = realm.where(ClientRealm.class).equalTo("cardId", card.getID())
                            .and()
                            .equalTo("companyId", BaseApp.getAppInstance().getCompanyClicked().getId())
                            .findFirst();

                    if(clients != null){
                        clients.deleteFromRealm();
                    }
                });

                getSharedPreferences(card.getID(),MODE_PRIVATE).edit().clear().apply();
                finish();
                CardAccountActivity.activity.finish();
            }
        });
    }
}