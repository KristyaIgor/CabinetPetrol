package edi.md.petrolcabinet;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.petrolcabinet.adapters.CardInfoAssortmentListAdapter;
import edi.md.petrolcabinet.bottomsheet.EditCardLimitBottomSheetDialog;
import edi.md.petrolcabinet.customindicator.MyPageChartIndicator;
import edi.md.petrolcabinet.fragments.slidePage.FragmentCardLimitsInfo;
import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.remoteSettings.ApiUtils;
import edi.md.petrolcabinet.remoteSettings.CommandServices;
import edi.md.petrolcabinet.remoteSettings.RemoteException;
import edi.md.petrolcabinet.remote.authenticate.AuthenticateUserBody;
import edi.md.petrolcabinet.remote.cardInfo.CardInfo;
import edi.md.petrolcabinet.remote.cardInfo.CardInfoAssortment;
import edi.md.petrolcabinet.remote.cardInfo.GetCardInfo;
import edi.md.petrolcabinet.remote.contract.CardsList;
import edi.md.petrolcabinet.remote.response.SIDResponse;
import edi.md.petrolcabinet.utils.BaseEnum;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardDetailActivity extends AppCompatActivity {
    public static int NUM_PAGES = 0;
    TextView title;
    ImageButton btnBack;
    ImageButton editCard;
    static ImageButton btnLeftSwipe;
    static ImageButton btnRightSwipe;

    static ViewPager viewPager;

    static LinearLayout mLinearLayout;
    static CustomPagerAdapter2 mAdapter;
    static MyPageChartIndicator mIndicator;
    static FragmentManager fragmentManager;

    static CardsList card;
    static Accounts client;

    static ProgressBar progressBar;

    static List<Fragment> fragments = new ArrayList<>();
    static List<String> titles = new ArrayList<>();

    static Context context;

    static Realm mRealm;

    static EditCardLimitBottomSheetDialog editForm;

    static RecyclerView recyclerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getWindow().getDecorView();
        Window window = getWindow();
        view.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        view.setFitsSystemWindows(true);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        setContentView(R.layout.activity_card_detail);

        title = findViewById(R.id.title_card_name);
        btnBack = findViewById(R.id.image_back_card_list);
        btnLeftSwipe = findViewById(R.id.btn_left_swipe_chart);
        btnRightSwipe = findViewById(R.id.btn_right_swipe_chart);
        editCard = findViewById(R.id.image_edit_card);
        viewPager = findViewById(R.id.screen_slide_chart);
        mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainerChart);
        progressBar = findViewById(R.id.progressBar_card_detail);
        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView2 = findViewById(R.id.recyclerView_produse_card);
        recyclerView2.setLayoutManager(layoutManagerV);

        fragmentManager = getSupportFragmentManager();
        context = this;
        mRealm = Realm.getDefaultInstance();

        card = BaseApp.getAppInstance().getClickedCard();
        client = BaseApp.getAppInstance().getClientClicked();

        title.setText(card.getName() + " / " + card.getCode());

        titles.add(getString(R.string.today_limit_time));
        titles.add(getString(R.string.week_limit_time));
        titles.add(getString(R.string.month_limit_type));

        getCardInfo();

        btnBack.setOnClickListener(view1 -> finish());

        btnLeftSwipe.setOnClickListener(view1 -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        });

        btnRightSwipe.setOnClickListener(view1 -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        });

        editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editForm = EditCardLimitBottomSheetDialog.newInstance();
                editForm.show(getSupportFragmentManager(), EditCardLimitBottomSheetDialog.TAG);
            }
        });
    }

    private static void getCardInfo() {
        progressBar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        mLinearLayout.setVisibility(View.GONE);

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<GetCardInfo> call = commandServices.getCardInfo(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), client.getSid(),card.getID());

        call.enqueue(new Callback<GetCardInfo>() {
            @Override
            public void onResponse(Call<GetCardInfo> call, Response<GetCardInfo> response) {
                GetCardInfo cardInfo = response.body();
                if(cardInfo != null){
                    if(cardInfo.getErrorCode() == 0){
                        CardInfo card = cardInfo.getCard();

                        fragments.add(FragmentCardLimitsInfo.newInstance(card.getDailyLimit(), card.getDailyLimitUsed(),card.getDailyLimitRemain(),card.getLimitType()));
                        fragments.add(FragmentCardLimitsInfo.newInstance(card.getWeeklyLimit(), card.getWeeklyLimitUsed(),card.getWeeklyLimitRemain(),card.getLimitType()));
                        fragments.add(FragmentCardLimitsInfo.newInstance(card.getMonthlyLimit(), card.getMonthlyLimitUsed(),card.getMonthlyLimitRemain(),card.getLimitType()));

                        mAdapter = new CustomPagerAdapter2(fragmentManager, fragments);
                        viewPager.setAdapter(mAdapter);
                        mIndicator = new MyPageChartIndicator(context, mLinearLayout, viewPager);
                        mIndicator.setPageTitles(titles);
//                        mIndicator.setInitialPage(1);
//                        viewPager.setCurrentItem(1);
                        mIndicator.onPageSelected(1);
                        mIndicator.show();

                        progressBar.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        mLinearLayout.setVisibility(View.VISIBLE);

                        List<CardInfoAssortment>  cardInfoAssortmentsList = card.getCardAssortments();
                        CardInfoAssortmentListAdapter adapter = new CardInfoAssortmentListAdapter(context, cardInfoAssortmentsList);
                        recyclerView2.setAdapter(adapter);

                    }
                    else if (cardInfo.getErrorCode() == 5){
                        reAuthClient(client);
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                        String msg = RemoteException.getServiceException(cardInfo.getErrorCode());
                        Toast.makeText(context, "Error!Message: " + msg, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Failure! Response is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCardInfo> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Failure! Message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void reAuthClient(Accounts client) {
        AuthenticateUserBody user = new AuthenticateUserBody();

        if (client.getTypeClient() == BaseEnum.PersoanaFizica) {
            user.setPassword(decrypt(client.getPassword(), BaseApp.getAppInstance().getHuyYou()));
            user.setPhone(decrypt(client.getPhone(), BaseApp.getAppInstance().getHuyYou()));
            user.setAuthType(0);
        } else if (client.getTypeClient() == BaseEnum.PersoanaJuridica) {
            user.setPassword(decrypt(client.getPassword(), BaseApp.getAppInstance().getHuyYou()));
            user.setUser(decrypt(client.getUserName(), BaseApp.getAppInstance().getHuyYou()));
            user.setIDNO(client.getIDNP());
            user.setAuthType(1);
        }

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<SIDResponse> call = commandServices.authenticateUser(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), user);

        call.enqueue(new Callback<SIDResponse>() {
            @Override
            public void onResponse(Call<SIDResponse> call, Response<SIDResponse> response) {
                SIDResponse sidResponse = response.body();
                if (sidResponse != null && sidResponse.getErrorCode() == 0) {
                    String sid = sidResponse.getSID();
                    mRealm.executeTransaction(realm -> {
                        client.setSid(sid);
                    });

                    getCardInfo();
                } else {
                    //TOOD aruncat la lista de conturi
                    Toast.makeText(context, "Error!Message response: " , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SIDResponse> call, Throwable t) {
                //TOOD aruncat la lista de conturi
                Toast.makeText(context, "Error!Message response: " + t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mIndicator != null)
            mIndicator.cleanup();

        if(fragments!= null)
            fragments.clear();
    }

    public static void onDismissDialog (){
        if(editForm != null && editForm.getShowsDialog()){
            editForm.dismiss();
        }

        getCardInfo();
    }

    static class CustomPagerAdapter2 extends FragmentStatePagerAdapter {

        List<Fragment> mFrags = new ArrayList<>();

        public CustomPagerAdapter2(FragmentManager fm, List<Fragment> frags) {
            super(fm);
            mFrags = frags;
        }

        @Override
        public Fragment getItem(int position) {
            return mFrags.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static void setButtonsTint(int index){
        if(index == 2)
            btnRightSwipe.setColorFilter(ContextCompat.getColor(context, R.color.indicator_unselected), android.graphics.PorterDuff.Mode.MULTIPLY);
        else if(index == 0)
            btnLeftSwipe.setColorFilter(ContextCompat.getColor(context, R.color.indicator_unselected), android.graphics.PorterDuff.Mode.MULTIPLY);
        else{
            btnRightSwipe.setColorFilter(ContextCompat.getColor(context, R.color.indicator_selected), android.graphics.PorterDuff.Mode.MULTIPLY);
            btnLeftSwipe.setColorFilter(ContextCompat.getColor(context, R.color.indicator_selected), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    public static String decrypt(byte[] cipherText, byte[] balabolDoi) {
        byte[] IV = new byte[16];
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(balabolDoi, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}