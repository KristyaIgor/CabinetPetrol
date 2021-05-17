package edi.md.petrolcabinet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import edi.md.petrolcabinet.customindicator.MyPageIndicator;
import edi.md.petrolcabinet.fragments.slidePage.FragmentBalanceInfo;
import edi.md.petrolcabinet.realm.objects.Accounts;
import io.realm.Realm;

public class JuridicAccountActivity extends AppCompatActivity {

    public static int NUM_PAGES = 0;
    Accounts client;
    TextView titleContract, facturInfo, nefacturInfo, delayInfo,textState;
    ImageButton btnBack, btnRight, btnLeft;
    MaterialButton openCardList, openProdusList, openHistoryList, btnSuply, openNotification, openInformation;
    ImageView imageState;

    private ViewPager viewPager;
    LinearLayout mLinearLayout;
    CustomPagerAdapter2 mAdapter;
    MyPageIndicator mIndicator;

    static FragmentManager fragmentManager;

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juridic_account);

        btnBack = findViewById(R.id.image_back);
        titleContract = findViewById(R.id.title_contract_name);
        viewPager = findViewById(R.id.screen_slide_contracts);
        mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainer);
        facturInfo = findViewById(R.id.text_factur_info);
        nefacturInfo = findViewById(R.id.text_nefactur_info);
        delayInfo = findViewById(R.id.text_delay_info);
        btnLeft = findViewById(R.id.btn_left_swipe);
        btnRight = findViewById(R.id.btn_right_swipe);
        textState = findViewById(R.id.text_state_inactive);
        imageState = findViewById(R.id.image_state_account);
        openCardList = findViewById(R.id.btn_juridic_open_card_list);
        openProdusList = findViewById(R.id.btn_juridic_open_assortment_list);
        openHistoryList = findViewById(R.id.btn_juridic_open_history_list);
        btnSuply = findViewById(R.id.btn_juridic_suplinire);
        openNotification = findViewById(R.id.btn_juridic_notification);
        openInformation = findViewById(R.id.btn_juridic_information);

        fragmentManager = getSupportFragmentManager();

        mRealm = Realm.getDefaultInstance();

        client = BaseApp.getAppInstance().getClientClicked();

        titleContract.setText(client.getName() != null ? client.getName() : "-");

        if(client.getStatus() != 0){
            textState.setVisibility(View.VISIBLE);
            textState.setText(getString(R.string.msg_account_is_inactive));

            imageState.setImageResource(R.drawable.ic_state_contract_inactive);
        }

        facturInfo.setText(String.valueOf(client.getUnpaidInvoiceConsumptionAmount()));
        nefacturInfo.setText(String.valueOf(client.getNonInvoicedConsumptionAmount()));
        delayInfo.setText(String.valueOf(client.getTotalDebtSum()));


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentBalanceInfo.newInstance(getString(R.string.account_sum_disponibil), client.getBalance(), getString(R.string.account_credit) + " + "
                + getString(R.string.account_sum_curent) + " + "
                + getString(R.string.account_balance_to_card)));
        fragments.add(FragmentBalanceInfo.newInstance(getString(R.string.account_sum_curent), client.getAmount(),getString(R.string.account_sum_client_info_sum_curent)));
        fragments.add(FragmentBalanceInfo.newInstance(getString(R.string.account_balance_to_card), client.getCardsBalance(),getString(R.string.fizic_account_balance_to_card_info)));
        fragments.add(FragmentBalanceInfo.newInstance(getString(R.string.account_credit) , client.getCredit(),getString(R.string.account_credit)  + " + " + getString(R.string.info_fizic_account_credit_total_overdraft)));

        mAdapter = new CustomPagerAdapter2(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mAdapter);
        mIndicator = new MyPageIndicator(this, mLinearLayout, viewPager, R.drawable.indicator);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();

        btnBack.setOnClickListener(view -> {
            finish();
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

        openCardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hist = new Intent(JuridicAccountActivity.this, CardListActivity.class);
                hist.putExtra("CardAccount",false);
                startActivity(hist);
            }
        });

        openHistoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hist = new Intent(JuridicAccountActivity.this,HistoryActivity.class);
                hist.putExtra("CardAccount", false);
                startActivity(hist);
            }
        });

        openProdusList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent product = new Intent(JuridicAccountActivity.this, AssortmentActivity.class);
                product.putExtra("CardAccount",false);
                startActivityForResult(product, 897);
            }
        });

        btnSuply.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(JuridicAccountActivity.this,R.style.MaterialAlertDialogCustom)
                    .setTitle(getString(R.string.oops_text))
                    .setMessage(getString(R.string.msg_supply_action_button))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.dialog_button_understand), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        });
        openInformation.setOnClickListener(view -> {
            Intent hist = new Intent(JuridicAccountActivity.this, SettingsAccountActivity.class);
            startActivityForResult(hist,164);
        });

        openNotification.setOnClickListener(view -> {
            Intent hist = new Intent(JuridicAccountActivity.this, NotificationListActivity.class);
            startActivity(hist);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 164){
            if(resultCode == 1593){
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mIndicator != null)
            mIndicator.cleanup();
    }

    static class CustomPagerAdapter2 extends FragmentStatePagerAdapter {

        List<Fragment> mFrags = new ArrayList<>();

        public CustomPagerAdapter2(FragmentManager fm, List<Fragment> frags) {
            super(fm);
            mFrags = frags;
        }

        @Override
        public Fragment getItem(int position) {
            int index = position % mFrags.size();
            NUM_PAGES = index;
            return mFrags.get(index);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
    }
}