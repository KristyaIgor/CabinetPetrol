package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edi.md.mydesign.adapters.ContractsInClientAdapter;
import edi.md.mydesign.adapters.RecyclerViewAdapter;
import edi.md.mydesign.customindicator.MyPageIndicator;
import edi.md.mydesign.fragments.slidePage.FragmentTotalBalance;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.remote.contract.Contract;

public class ContractActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 5;
    ClientRealm client;
    Contract contract;
    TextView titleContract, infoBalance, infoCardBalance, infoCredit, infoAvailableFunds;
    ImageButton btnBack;
    private ArrayList<String> mNames = new ArrayList<>();


    private ViewPager viewPager;

    LinearLayout mLinearLayout;
    CustomPagerAdapter2 mAdapter;
    MyPageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        btnBack = findViewById(R.id.image_back);
        titleContract = findViewById(R.id.title_contract_name);
        viewPager = findViewById(R.id.screen_slide_contracts);
        mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainer);

        client = BaseApp.getAppInstance().getClientClicked();

        double TotalBalance = client.getBalance();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentTotalBalance.newInstance("Total balance", client.getBalance()));
        fragments.add(FragmentTotalBalance.newInstance("Master balance", client.getMasterBalance()));
        fragments.add(FragmentTotalBalance.newInstance("Cards balance", client.getCardsBalance()));
        fragments.add(FragmentTotalBalance.newInstance("Amount", client.getAmount()));
        fragments.add(FragmentTotalBalance.newInstance("Credit", client.getCredit()));

        mAdapter = new CustomPagerAdapter2(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mAdapter);
        mIndicator = new MyPageIndicator(this, mLinearLayout, viewPager, R.drawable.indicator);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();

//        contract = BaseApp.getAppInstance().getClickedClientContract();
//
//        infoBalance.setText(String.valueOf(client.getBalance()));
//        infoCredit.setText(String.valueOf(client.getCredit()));
//        titleContract.setText(client.getName());
//        infoCardBalance.setText(String.valueOf(contract.getCardsBalance()));
//
//

        List<ContractInClient> contracts = client.getContracts();
        ContractsInClientAdapter adapter1 = new ContractsInClientAdapter(this, contracts);

        btnBack.setOnClickListener(view -> {
            finish();
        });

        getImages();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView_contracts);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManagerV);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames);
        recyclerView.setAdapter(adapter);
        recyclerView2.setAdapter(adapter1);
    }

    private void getImages() {

        mNames.add("Havasu Falls");

        mNames.add("Trondheim");

        mNames.add("Portugal");

        mNames.add("Rocky Mountain National Park");


        mNames.add("Mahahual");

        mNames.add("Frozen Lake");

        mNames.add("White Sands Desert");

        mNames.add("Austrailia");
        mNames.add("Washington");

    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new FragmentTotalBalance();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

    }

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    };

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
            return mFrags.get(index);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

    }
}