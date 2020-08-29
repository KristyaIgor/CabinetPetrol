package edi.md.mydesign;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import edi.md.mydesign.customindicator.MyPageIndicator;
import edi.md.mydesign.fragments.slidePage.FragmentBalanceInfo;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.remote.contract.Contract;
import edi.md.mydesign.remote.contract.GetContractInfoResponse;
import edi.md.mydesign.remote.notification.GetNotificationSettings;
import edi.md.mydesign.remote.notification.NotificationSettings;
import edi.md.mydesign.remote.notification.UpdateNotificationSettingsBody;
import edi.md.mydesign.remote.response.SimpleResponse;
import edi.md.mydesign.remote.transaction.GetTransactionList;
import edi.md.mydesign.remote.transaction.Transaction;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JuridicAccountActivity extends AppCompatActivity {

    public static int NUM_PAGES = 0;
    ClientRealm client;
    TextView titleContract, facturInfo, nefacturInfo, delayInfo,textState;
    ImageButton btnBack, btnRight, btnLeft;
    MaterialButton openCardList, openProdusList, openHistoryList, btnSuply, openNotification, openInformation;
    ImageView imageState;

    private ViewPager viewPager;

    LinearLayout mLinearLayout;
    CustomPagerAdapter2 mAdapter;
    MyPageIndicator mIndicator;

    static FragmentManager fragmentManager;

    List<Contract> contractListClient = new ArrayList<>();

    CommandServices commandServices;

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
        commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        String tokenF = getSharedPreferences("firebase",MODE_PRIVATE).getString("token",null);

        titleContract.setText(client.getName());

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

        Call<GetTransactionList> call = commandServices.getTransactionList(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), client.getSid());
        Call<GetNotificationSettings> callUpdateNotification = commandServices.getNotificationSettings(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), client.getSid(), tokenF);

        enqeueCallTransaction(call);
        enqeueCallNotification(callUpdateNotification);

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
                    .setTitle("Oops!")
                    .setMessage("La moment aceasta optiune nu este disponibila.\nCurind va fi disponibila")
                    .setCancelable(false)
                    .setPositiveButton("Am înţeles", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        });
        openInformation.setOnClickListener(view -> {
            Intent hist = new Intent(JuridicAccountActivity.this, InformationActivity.class);
            hist.putExtra("JuridicAccount", true);
            startActivityForResult(hist,1564);
        });

        openNotification.setOnClickListener(view -> {
            Intent hist = new Intent(JuridicAccountActivity.this, NotificationCardActivity.class);
            hist.putExtra("JuridicAccount",true);
            startActivity(hist);
        });

    }

    private void enqeueCallNotification(Call<GetNotificationSettings> call) {
        call.enqueue(new Callback<GetNotificationSettings>() {
            @Override
            public void onResponse(Call<GetNotificationSettings> call, Response<GetNotificationSettings> response) {
                GetNotificationSettings simpleResponse = response.body();
                if(simpleResponse != null && simpleResponse.getErrorCode() == 0){
                    NotificationSettings notification = simpleResponse.getNotificationSettings();
                    SharedPreferences sPref = getSharedPreferences(client.getIDNP(),MODE_PRIVATE);

                    sPref.edit().putBoolean("Filling",notification.getFillingAccount()).apply();
                    sPref.edit().putBoolean("PayByAccount",notification.getPayByAccount()).apply();
                    sPref.edit().putBoolean("News",notification.getNews()).apply();
                    sPref.edit().putBoolean("NewDiscount",notification.getNewDiscount()).apply();
                }
            }

            @Override
            public void onFailure(Call<GetNotificationSettings> call, Throwable t) {
                Log.d("TAG", "onFailure update notifications: " + t.getMessage());
            }
        });
    }

    private void enqeueCallTransaction(Call<GetTransactionList> call) {
        call.enqueue(new Callback<GetTransactionList>() {
            @Override
            public void onResponse(Call<GetTransactionList> call, Response<GetTransactionList> response) {
                if(response.isSuccessful()){
                    GetTransactionList transactionList = response.body();
                    if(transactionList != null && transactionList.getErrorCode() == 0){

                        List<Transaction> listOfTransactionR = transactionList.getTransactions();
                        BaseApp.getAppInstance().setTransactions(listOfTransactionR);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetTransactionList> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mIndicator != null)
            mIndicator.cleanup();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1564){
            if(resultCode == 159753){
                finish();
            }
        }
        else if(requestCode == 897){
            if(resultCode == RESULT_CANCELED){
                finish();
            }
        }
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