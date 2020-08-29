package edi.md.mydesign;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.cardDetailInfo.CardItem;
import edi.md.mydesign.remote.notification.UpdateNotificationSettingsBody;
import edi.md.mydesign.remote.response.SimpleResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    ImageButton btnBack;

    Switch supSwitch, alimSwitch, newsSwitch, newDiscSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        Window window = getWindow();
        decorView.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        decorView.setFitsSystemWindows(true);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        setContentView(R.layout.activity_notification_card);

        btnBack = findViewById(R.id.image_back_card_notification);
        supSwitch = findViewById(R.id.switch_notification_suplinire_card);
        alimSwitch = findViewById(R.id.switch_notification_alimentare_card);
        newsSwitch = findViewById(R.id.switch_notification_news);
        newDiscSwitch = findViewById(R.id.switch_notification_new_discount);

        CardItem item = BaseApp.getAppInstance().getCardAccount();

        String cardId = item.getID();

        SharedPreferences sPref = getSharedPreferences(cardId,MODE_PRIVATE);

        boolean suplinire = sPref.getBoolean("Filling", false);
        boolean alimentare = sPref.getBoolean("PayByAccount", false);
        boolean news = sPref.getBoolean("News", false);
        boolean newDiscount = sPref.getBoolean("NewDiscount", false);

        supSwitch.setChecked(suplinire);
        alimSwitch.setChecked(alimentare);
        newsSwitch.setChecked(news);
        newDiscSwitch.setChecked(newDiscount);

        supSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateNotification(supSwitch.isChecked(), alimSwitch.isChecked(), newsSwitch.isChecked(), newDiscSwitch.isChecked());
            }
        });

        alimSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateNotification(supSwitch.isChecked(), alimSwitch.isChecked(), newsSwitch.isChecked(), newDiscSwitch.isChecked());
            }
        });

        newsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateNotification(supSwitch.isChecked(), alimSwitch.isChecked(), newsSwitch.isChecked(), newDiscSwitch.isChecked());
            }
        });

        newDiscSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateNotification(supSwitch.isChecked(), alimSwitch.isChecked(), newsSwitch.isChecked(), newDiscSwitch.isChecked());
            }
        });

        btnBack.setOnClickListener(view -> finish());
    }

    private void updateNotification(boolean filling, boolean pay, boolean news, boolean newDiscount) {
        String tokenF = getSharedPreferences("firebase",MODE_PRIVATE).getString("token",null);

        UpdateNotificationSettingsBody body = new UpdateNotificationSettingsBody();
        body.setToken(tokenF);
        body.setSID(BaseApp.getAppInstance().getCardAccount().getSID());
        body.setFillingAccount(filling);
        body.setPayByAccount(pay);
        body.setNews(news);
        body.setNewDiscount(newDiscount);

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<SimpleResponse> callUpdateNotification = commandServices.updateNotificationSettings(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), body);

        callUpdateNotification.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                SimpleResponse simpleResponse = response.body();
                if (simpleResponse != null || simpleResponse.getErrorCode() == 0){

                    SharedPreferences sPref = getSharedPreferences(BaseApp.getAppInstance().getCardAccount().getID(),MODE_PRIVATE);

                    sPref.edit().putBoolean("Filling",filling).apply();
                    sPref.edit().putBoolean("PayByAccount",pay).apply();
                    sPref.edit().putBoolean("News",news).apply();
                    sPref.edit().putBoolean("NewDiscount",newDiscount).apply();
                }
                else{
                    supSwitch.setChecked(filling);
                    alimSwitch.setChecked(pay);
                    newsSwitch.setChecked(news);
                    newDiscSwitch.setChecked(newDiscount);
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                supSwitch.setChecked(filling);
                alimSwitch.setChecked(pay);
                newsSwitch.setChecked(news);
                newDiscSwitch.setChecked(newDiscount);
            }
        });
    }
}