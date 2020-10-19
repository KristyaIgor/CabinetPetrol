package edi.md.petrolcabinet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.remote.notification.GetNotificationSettings;
import edi.md.petrolcabinet.remote.notification.NotificationSettings;
import edi.md.petrolcabinet.remote.notification.UpdateNotificationSettingsBody;
import edi.md.petrolcabinet.remote.response.SimpleResponse;
import edi.md.petrolcabinet.remoteSettings.ApiUtils;
import edi.md.petrolcabinet.remoteSettings.CommandServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    ImageButton btnBack;

    SwitchCompat supSwitch, alimSwitch, newsSwitch, newDiscSwitch;

    boolean startSettings = false;
    Accounts clientRealm;
    String sid;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        btnBack = findViewById(R.id.image_back_notification);
        supSwitch = findViewById(R.id.switch_notification_suplinire);
        alimSwitch = findViewById(R.id.switch_notification_alimentare);
        newsSwitch = findViewById(R.id.switch_notification_news_person);
        newDiscSwitch = findViewById(R.id.switch_notification_new_discount_person);
        progressDialog = new ProgressDialog(this);

        String tokenF = getSharedPreferences("firebase",MODE_PRIVATE).getString("token",null);
        clientRealm = BaseApp.getAppInstance().getClientClicked();
        sid = clientRealm.getSid();

        progressDialog.setMessage(getString(R.string.progres_msg_loading_notification_settings));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<GetNotificationSettings> callUpdateNotification = commandServices.getNotificationSettings(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), sid, tokenF);
        enqeueCallNotification(callUpdateNotification);

        supSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(!startSettings)updateNotification(b,alimSwitch.isChecked(), newsSwitch.isChecked(), newDiscSwitch.isChecked(),1);
        });
        alimSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(!startSettings)updateNotification(supSwitch.isChecked(), b, newsSwitch.isChecked(), newDiscSwitch.isChecked(),2);
        });
        newsSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(!startSettings)updateNotification(supSwitch.isChecked(), alimSwitch.isChecked(), b, newDiscSwitch.isChecked(),3);
        });
        newDiscSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(!startSettings)updateNotification(supSwitch.isChecked(), alimSwitch.isChecked(), newsSwitch.isChecked(), b,4);
        });

        btnBack.setOnClickListener(view -> finish());
    }

    private void updateNotification(boolean filling, boolean pay, boolean news, boolean newDiscount,int type) {
        String tokenF = getSharedPreferences("firebase",MODE_PRIVATE).getString("token",null);

        UpdateNotificationSettingsBody body = new UpdateNotificationSettingsBody();
        body.setClientId(clientRealm.getId());
        body.setToken(tokenF);
        body.setSID(sid);
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
                if (simpleResponse != null && simpleResponse.getErrorCode() == 0){
                    startSettings = true;
                    switch (type){
                        case 1: {
                            if(filling)
                                Toast.makeText(NotificationActivity.this, getString(R.string.msg_notification_enable_filling), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(NotificationActivity.this, getString(R.string.msg_notification_disable_filling), Toast.LENGTH_SHORT).show();
                        }break;
                        case 2: {
                            if(pay)
                                Toast.makeText(NotificationActivity.this, getString(R.string.msg_notification_enable_pay_account), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(NotificationActivity.this, getString(R.string.msg_notification_disable_pay_account), Toast.LENGTH_SHORT).show();
                        }break;
                        case 3: {
                            if(news)
                                Toast.makeText(NotificationActivity.this, getString(R.string.msg_notification_enable_news), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(NotificationActivity.this, getString(R.string.msg_notification_disable_news), Toast.LENGTH_SHORT).show();
                        }break;
                        case 4: {
                            if(newDiscount)
                                Toast.makeText(NotificationActivity.this, getString(R.string.msg_notification_enable_promo), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(NotificationActivity.this, getString(R.string.msg_notification_disable_promo), Toast.LENGTH_SHORT).show();
                        }break;
                    }
                    startSettings = false;
                }
                else{
                    switch (type){
                        case 1:supSwitch.setChecked(!filling); break;
                        case 2:alimSwitch.setChecked(!pay); break;
                        case 3:newsSwitch.setChecked(!news); break;
                        case 4:newDiscSwitch.setChecked(!newDiscount);break;
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                switch (type){
                    case 1:supSwitch.setChecked(!filling); break;
                    case 2:alimSwitch.setChecked(!pay); break;
                    case 3:newsSwitch.setChecked(!news); break;
                    case 4:newDiscSwitch.setChecked(!newDiscount);break;
                }
            }
        });
    }

    private void enqeueCallNotification(Call<GetNotificationSettings> call) {
        call.enqueue(new Callback<GetNotificationSettings>() {
            @Override
            public void onResponse(Call<GetNotificationSettings> call, Response<GetNotificationSettings> response) {
                GetNotificationSettings simpleResponse = response.body();
                if(simpleResponse != null && simpleResponse.getErrorCode() == 0){

                    NotificationSettings notification = simpleResponse.getNotificationSettings();
                    startSettings = true;
                    supSwitch.setChecked(notification.getFillingAccount());
                    alimSwitch.setChecked(notification.getPayByAccount());
                    newsSwitch.setChecked(notification.getNews());
                    newDiscSwitch.setChecked(notification.getNewDiscount());
                    progressDialog.dismiss();
                    startSettings = false;
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(NotificationActivity.this, getString(R.string.notification_settings_not_load), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetNotificationSettings> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(NotificationActivity.this, getString(R.string.notification_settings_not_load), Toast.LENGTH_SHORT).show();
            }
        });
    }
}