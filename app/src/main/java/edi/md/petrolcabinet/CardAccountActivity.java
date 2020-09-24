package edi.md.petrolcabinet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edi.md.petrolcabinet.bottomsheet.CardAccountMenuBottomSheetDialog;
import edi.md.petrolcabinet.remote.cardDetailInfo.CardItem;
import edi.md.petrolcabinet.utils.BaseEnum;

public class CardAccountActivity extends AppCompatActivity {

    TextView dayLimit, dayConsum, dayRemain, weekLimit, weekConsum, weekRemain, montLimit, monthConsum, monthRemain;
    TextView title,stateInfo , titleBalance, textBalance, infoBalance;
    ImageButton btnBack, btnMenu;
    ImageView imageState;

    CardItem cardItem;
    static CardAccountMenuBottomSheetDialog cardAccountMenuBottomSheetDialog;
    static Activity activity;

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
            stateInfo.setText(getString(R.string.card_account_card_is_inactive));
        }

        btnBack.setOnClickListener(view -> finish());

        int limit = cardItem.getLimitType();
        String typeLimit = "";

        if(limit == BaseEnum.LimitBani)
            typeLimit = getString(R.string.lei_text);
        else
            typeLimit = getString(R.string.litri_text);

        if(cardItem.getSeparateClientAccount()){
            titleBalance.setText(getString(R.string.card_account_sum_disponibil));
            textBalance.setText(cardItem.getBalanceAccountCard() + getString(R.string.lei_text));
            infoBalance.setText(getString(R.string.card_account_card_balance));
        }
        else{
            titleBalance.setText(getString(R.string.card_account_sum_disponibil));
            textBalance.setText(cardItem.getBalanceAccountCard() + getString(R.string.lei_text));
            infoBalance.setText(getString(R.string.card_account_balance_client));
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

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardAccountMenuBottomSheetDialog = CardAccountMenuBottomSheetDialog.newInstance();
                cardAccountMenuBottomSheetDialog.show(getSupportFragmentManager(), CardAccountMenuBottomSheetDialog.TAG);
            }
        });

    }
}