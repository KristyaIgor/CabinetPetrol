package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import edi.md.mydesign.customindicator.MyPageIndicator;
import edi.md.mydesign.fragments.slidePage.FragmentBalanceInfo;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.contract.Contract;
import edi.md.mydesign.remote.transaction.GetTransactionList;
import edi.md.mydesign.remote.transaction.Transaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JuridicAccountActivity extends AppCompatActivity {

    public static int NUM_PAGES = 0;
    ClientRealm client;
    Contract contract;
    TextView titleContract, facturInfo, nefacturInfo, delayInfo,textState, paymentDelay;
    ImageButton btnBack, btnRight, btnLeft;
    MaterialButton openCardList, openProdusList, openHistoryList, btnSuply, openNotification, openInformation;
    ImageView imageState;

    private ViewPager viewPager;

    LinearLayout mLinearLayout;
    CustomPagerAdapter2 mAdapter;
    MyPageIndicator mIndicator;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    TimeZone timeZone = TimeZone.getTimeZone("Europe/Chisinau");

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
        paymentDelay = findViewById(R.id.text_payment_delay);

        client = BaseApp.getAppInstance().getClientClicked();
        contract = BaseApp.getAppInstance().getClickedClientContract();

        simpleDateFormat.setTimeZone(timeZone);

        titleContract.setText(client.getName());

        String fr = contract.getDateValidFrom();
        fr = fr.replace("/Date(", "");
        fr = fr.replace("+0300)/", "");
        fr = fr.replace("+0200)/", "");
        fr = fr.replace(")/", "");

        long timeFr = Long.parseLong(fr);

        String validFrom = simpleDateFormat.format(timeFr);
        String to = contract.getDateValidTo();
        to = to.replace("/Date(", "");
        to = to.replace("+0300)/", "");
        to = to.replace("+0200)/", "");
        to = to.replace(")/", "");

        long timeTo = Long.parseLong(to);

        String validTo = simpleDateFormat.format(timeTo);

        paymentDelay.setText("Întârziere de plată permis(zile): " + contract.getPaymentDelay());

        if(client.getStatus() != 0){
            textState.setVisibility(View.VISIBLE);
            textState.setText("Din cauza datoriilor restante, contul a fost dezactivat");

            imageState.setImageResource(R.drawable.ic_state_contract_inactive);
        }

        facturInfo.setText(String.valueOf(client.getUnpaidInvoiceConsumptionAmount()));
        nefacturInfo.setText(String.valueOf(client.getNonInvoicedConsumptionAmount()));
        delayInfo.setText(String.valueOf(client.getTotalDebtSum()));


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentBalanceInfo.newInstance("Suma disponibilă", client.getBalance(),"Credit + Suma curentă + Sold pe carduri"));
        fragments.add(FragmentBalanceInfo.newInstance("Suma curentă", client.getAmount(),"Suma clientului"));
        fragments.add(FragmentBalanceInfo.newInstance("Soldul pe carduri", client.getCardsBalance(),"Suma disponibilă pe carduri"));
        fragments.add(FragmentBalanceInfo.newInstance("Credit", client.getCredit(),"Credit + Overdraft"));

        mAdapter = new CustomPagerAdapter2(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mAdapter);
        mIndicator = new MyPageIndicator(this, mLinearLayout, viewPager, R.drawable.indicator);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getAddress());
        Call<GetTransactionList> call = commandServices.getTransactionList(client.getSid());

        call.enqueue(new Callback<GetTransactionList>() {
            @Override
            public void onResponse(Call<GetTransactionList> call, Response<GetTransactionList> response) {
                if(response.isSuccessful()){
                    GetTransactionList transactionList = response.body();
                    if(transactionList != null && transactionList.getErrorCode() == 0){
                        List<Transaction> listOfTransaction = new ArrayList<>();

                        List<Transaction> listOfTransactionR = transactionList.getTransactions();
                        BaseApp.getAppInstance().setTransactions(listOfTransactionR);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetTransactionList> call, Throwable t) {

            }
        });

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
                startActivity(new Intent(JuridicAccountActivity.this,CardListActivity.class));
            }
        });

        openHistoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JuridicAccountActivity.this,HistoryActivity.class));
            }
        });

        openProdusList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JuridicAccountActivity.this,AssortmentActivity.class));
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
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