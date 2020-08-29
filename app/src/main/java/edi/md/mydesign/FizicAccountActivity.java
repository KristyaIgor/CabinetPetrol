package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import edi.md.mydesign.customindicator.MyPageIndicator;
import edi.md.mydesign.fragments.slidePage.FragmentBalanceInfo;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.contract.Contract;
import edi.md.mydesign.remote.notification.GetNotificationSettings;
import edi.md.mydesign.remote.notification.NotificationSettings;
import edi.md.mydesign.remote.transaction.GetTransactionList;
import edi.md.mydesign.remote.transaction.Transaction;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FizicAccountActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_fizic_account);

        btnBack = findViewById(R.id.image_back);
        titleContract = findViewById(R.id.title_fizic_name);
        viewPager = findViewById(R.id.screen_slide_fizic_contracts);
        mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainer_fizic);
        btnLeft = findViewById(R.id.btn_left_swipe_fizic);
        btnRight = findViewById(R.id.btn_right_swipe_fizic);
        textState = findViewById(R.id.text_state_inactive);
        imageState = findViewById(R.id.image_state_account);
        openCardList = findViewById(R.id.btn_fizic_open_card_list);
        openProdusList = findViewById(R.id.btn_fizic_open_assortment_list);
        openHistoryList = findViewById(R.id.btn_fizic_open_history_list);
        btnSuply = findViewById(R.id.btn_fizicsuplinire);
        openNotification = findViewById(R.id.btn_fizic_notification);
        openInformation = findViewById(R.id.btn_fizic_information);

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
                Intent hist = new Intent(FizicAccountActivity.this, CardListActivity.class);
                hist.putExtra("CardAccount",false);
                startActivity(hist);
            }
        });

        openHistoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hist = new Intent(FizicAccountActivity.this,HistoryActivity.class);
                hist.putExtra("CardAccount", false);
                startActivity(hist);
            }
        });

        openProdusList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent product = new Intent(FizicAccountActivity.this, AssortmentActivity.class);
                product.putExtra("CardAccount",false);
                startActivityForResult(product, 897);
            }
        });

        btnSuply.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(this,R.style.MaterialAlertDialogCustom)
                    .setTitle("Oops!")
                    .setMessage("La moment aceasta optiune nu este disponibila.\nCurind va fi disponibila!")
                    .setCancelable(false)
                    .setPositiveButton("Am înţeles", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        });
    }

    private void enqeueCallNotification(Call<GetNotificationSettings> call) {
        call.enqueue(new Callback<GetNotificationSettings>() {
            @Override
            public void onResponse(Call<GetNotificationSettings> call, Response<GetNotificationSettings> response) {
                GetNotificationSettings simpleResponse = response.body();
                if(simpleResponse != null && simpleResponse.getErrorCode() == 0){
                    NotificationSettings notification = simpleResponse.getNotificationSettings();
                    mRealm.executeTransaction(realm -> {
                        ClientRealm clientRealm = realm.where(ClientRealm.class).equalTo("iDNP",client.getIDNP())
                                .and()
                                .equalTo("userName",client.getUserName())
                                .and()
                                .equalTo("password",client.getPassword())
                                .and()
                                .equalTo("companyId",BaseApp.getAppInstance().getCompanyClicked().getId())
                                .findFirst();

                        if(clientRealm != null){
                            clientRealm.setNews(notification.getNews());
                            clientRealm.setNewDiscount(notification.getNewDiscount());
                            clientRealm.setPayAccount(notification.getPayByAccount());
                            clientRealm.setFillingAccount(notification.getFillingAccount());

                            client = realm.copyFromRealm(clientRealm);
                            BaseApp.getAppInstance().setClientClicked(client);
                        }
                    });
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