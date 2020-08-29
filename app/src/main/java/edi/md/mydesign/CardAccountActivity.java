package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edi.md.mydesign.bottomsheet.CardAccountMenuBottomSheetDialog;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.cardDetailInfo.CardItem;
import edi.md.mydesign.remote.notification.GetNotificationSettings;
import edi.md.mydesign.remote.notification.NotificationSettings;
import edi.md.mydesign.utils.BaseEnum;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardAccountActivity extends AppCompatActivity {

    TextView dayLimit, dayConsum, dayRemain, weekLimit, weekConsum, weekRemain, montLimit, monthConsum, monthRemain;
    TextView title,stateInfo , titleBalance, textBalance, infoBalance;

    ImageButton btnBack, btnMenu;
    ImageView imageState;

    CardItem cardItem;


    static CardAccountMenuBottomSheetDialog cardAccountMenuBottomSheetDialog;

    static Activity activity;

    CommandServices commandServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_account);

        titleBalance = findViewById(R.id.text_title_balance_card);
        textBalance = findViewById(R.id.text_balance_card);
        infoBalance = findViewById(R.id.text_info_balance_card);

        btnBack = findViewById(R.id.image_back_card);
        btnMenu = findViewById(R.id.btn_menu_bottom);
        title = findViewById(R.id.title_card_name);
        imageState = findViewById(R.id.image_state_card_account);
        stateInfo = findViewById(R.id.text_state_inactive_card);

        dayLimit = findViewById(R.id.text_day_limit);
        dayConsum = findViewById(R.id.text_day_consum);
        dayRemain = findViewById(R.id.text_day_remain);

        weekLimit = findViewById(R.id.text_week_limit);
        weekConsum = findViewById(R.id.text_week_consum);
        weekRemain = findViewById(R.id.text_week_remain);

        montLimit = findViewById(R.id.text_month_limit);
        monthConsum = findViewById(R.id.text_month_consum);
        monthRemain = findViewById(R.id.text_month_remain);

        cardItem = BaseApp.getAppInstance().getCardAccount();

        title.setText(cardItem.getName() + "/" + cardItem.getCode());

        activity = this;

        boolean isActive = cardItem.getIsActive();

        if(!isActive){
            imageState.setImageResource(R.drawable.ic_state_contract_inactive);
            stateInfo.setVisibility(View.VISIBLE);
            stateInfo.setText("Cardul este inactive");
        }

        btnBack.setOnClickListener(view -> finish());

        int limit = cardItem.getLimitType();
        String typeLimit = "";

        if(limit == BaseEnum.LimitBani)
            typeLimit = " MDL";
        else
            typeLimit = " L";

        if(cardItem.getSeparateClientAccount()){
            titleBalance.setText("Suma disponibilă");
            textBalance.setText(cardItem.getBalanceAccountCard() + " MDL");
            infoBalance.setText("Sold pe card individual");
        }
        else{
            titleBalance.setText("Suma disponibilă");
            textBalance.setText(cardItem.getBalanceAccountCard() + " MDL");
            infoBalance.setText("Suma disponibilă a clientului");
        }

        dayLimit.setText(cardItem.getDailyLimit() + typeLimit);
        dayConsum.setText(cardItem.getDailyLimitUsed() + typeLimit);
        dayRemain.setText(cardItem.getDailyLimitRemain() + typeLimit);

        weekLimit.setText(cardItem.getWeeklyLimit() + typeLimit);
        weekConsum.setText(cardItem.getWeeklyLimitUsed() + typeLimit);
        weekRemain.setText(cardItem.getWeeklyLimitRemain() + typeLimit);

        montLimit.setText(cardItem.getMonthlyLimit() + typeLimit);
        monthConsum.setText(cardItem.getMonthlyLimitUsed() + typeLimit);
        monthRemain.setText(cardItem.getMonthlyLimitRemain() + typeLimit);

        if(cardItem.getDailyLimitRemain() == 0)
            dayRemain.setTextColor(getColor(R.color.red));
        if(cardItem.getWeeklyLimitRemain() == 0)
            weekRemain.setTextColor(getColor(R.color.red));
        if(cardItem.getMonthlyLimitRemain() == 0)
            monthRemain.setTextColor(getColor(R.color.red));

        if(!cardItem.getIsActive())
            imageState.setImageResource(R.drawable.ic_state_contract_inactive);

        commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        String tokenF = getSharedPreferences("firebase",MODE_PRIVATE).getString("token",null);

        Call<GetNotificationSettings> callUpdateNotification = commandServices.getNotificationSettings(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), cardItem.getSID(), tokenF);
        enqeueCall(callUpdateNotification);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardAccountMenuBottomSheetDialog = CardAccountMenuBottomSheetDialog.newInstance();
                cardAccountMenuBottomSheetDialog.show(getSupportFragmentManager(), CardAccountMenuBottomSheetDialog.TAG);
            }
        });

    }

    private void enqeueCall(Call<GetNotificationSettings> callUpdateNotification) {
        callUpdateNotification.enqueue(new Callback<GetNotificationSettings>() {
            @Override
            public void onResponse(Call<GetNotificationSettings> call, Response<GetNotificationSettings> response) {
                GetNotificationSettings simpleResponse = response.body();
                if(simpleResponse != null && simpleResponse.getErrorCode() == 0){
                    NotificationSettings notification = simpleResponse.getNotificationSettings();

                    SharedPreferences sPref = getSharedPreferences(cardItem.getID(),MODE_PRIVATE);

                    sPref.edit().putBoolean("Filling",notification.getFillingAccount()).apply();
                    sPref.edit().putBoolean("PayByAccount",notification.getPayByAccount()).apply();
                    sPref.edit().putBoolean("News",notification.getNews()).apply();
                    sPref.edit().putBoolean("NewDiscount",notification.getNewDiscount()).apply();
                }
            }

            @Override
            public void onFailure(Call<GetNotificationSettings> call, Throwable t) {

            }
        });

    }
}