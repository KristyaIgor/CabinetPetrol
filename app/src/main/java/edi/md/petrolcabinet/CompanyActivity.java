package edi.md.petrolcabinet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;

import edi.md.petrolcabinet.fragments.FragmentCabinetsAndCards;
import edi.md.petrolcabinet.fragments.FragmentMaps;
import edi.md.petrolcabinet.fragments.FragmentPrices;
import edi.md.petrolcabinet.realm.objects.Company;
import edi.md.petrolcabinet.remote.petrolStation.GetPetrolStationResult;
import edi.md.petrolcabinet.remoteSettings.ApiUtils;
import edi.md.petrolcabinet.remoteSettings.CommandServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyActivity extends LocalizationActivity {

    ViewPager viewPager;
    ConstraintLayout layoutContract, layoutPrices, layoutMaps;
    TextView titleContracts, titlePrices, titleMaps;
    ImageView imgContracts, imgPrices, imgMaps;
    TextView titleCompany;
    ImageButton backButton;
    Company companySelected;

    int selectedItem = 0;

    public static DisplayMetrics displayMetrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        layoutContract = findViewById(R.id.layout_contracts);
        layoutPrices = findViewById(R.id.layout_prices);
        layoutMaps = findViewById(R.id.layout_maps);
        titleContracts = findViewById(R.id.textView_contracts);
        titlePrices = findViewById(R.id.textView_prices);
        titleMaps = findViewById(R.id.textView_maps);
        imgContracts = findViewById(R.id.imageView_contracts);
        imgPrices = findViewById(R.id.imageView_prices);
        imgMaps = findViewById(R.id.imageView_maps);
        titleCompany = findViewById(R.id.title_company);
        backButton = findViewById(R.id.btn_back);
        viewPager = findViewById(R.id.container_company);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        companySelected = BaseApp.getAppInstance().getCompanyClicked();

        titleCompany.setText(companySelected.getName());

        backButton.setOnClickListener(view -> finish());

        createViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0 && positionOffsetPixels == 0){
                    setLayoutActive(0);
                }
                if(position == 1 && positionOffsetPixels == 0){
                    setLayoutActive(1);
                }
                if(position == 2 && positionOffsetPixels == 0){
                    setLayoutActive(2);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        layoutContract.setOnClickListener(view -> {
            viewPager.setCurrentItem(0);
        });
        layoutPrices.setOnClickListener(view -> {
            viewPager.setCurrentItem(1);
        });
        layoutMaps.setOnClickListener(view -> {
            viewPager.setCurrentItem(2);
        });

        CommandServices commandServices = ApiUtils.getCommandServices(companySelected.getIp());
        Call<GetPetrolStationResult> call = commandServices.getPetrolStation(companySelected.getServiceName());

        enqueueCall(call);
    }

    private void setLayoutActive(int position) {
        if(position == 0){
            if(selectedItem == 1){
                layoutPrices.setSelected(false);
                titlePrices.setTypeface(Typeface.DEFAULT);
                imgPrices.setColorFilter(getColor(R.color.green));
            }
            else if(selectedItem == 2){
                layoutMaps.setSelected(false);
                titleMaps.setTypeface(Typeface.DEFAULT);
                imgMaps.setColorFilter(getColor(R.color.green));
            }

            layoutContract.setSelected(true);
            titleContracts.setTypeface(Typeface.DEFAULT_BOLD);
            imgContracts.setColorFilter(Color.rgb(255, 255, 255));
        }
        else if (position == 1){
            if(selectedItem == 0){
                layoutContract.setSelected(false);
                titleContracts.setTypeface(Typeface.DEFAULT);
                imgContracts.setColorFilter(getColor(R.color.green));
            }
            else if(selectedItem == 2){
                layoutMaps.setSelected(false);
                titleMaps.setTypeface(Typeface.DEFAULT);
                imgMaps.setColorFilter(getColor(R.color.green));
            }

            layoutPrices.setSelected(true);
            titlePrices.setTypeface(Typeface.DEFAULT_BOLD);
            imgPrices.setColorFilter(Color.rgb(255, 255, 255));
        }
        else if (position == 2){
            if(selectedItem == 0){
                layoutContract.setSelected(false);
                titleContracts.setTypeface(Typeface.DEFAULT);
                imgContracts.setColorFilter(getColor(R.color.green));
            }
            else if(selectedItem == 1){
                layoutPrices.setSelected(false);
                titlePrices.setTypeface(Typeface.DEFAULT);
                imgPrices.setColorFilter(getColor(R.color.green));
            }

            layoutMaps.setSelected(true);
            titleMaps.setTypeface(Typeface.DEFAULT_BOLD);
            imgMaps.setColorFilter(Color.rgb(255, 255, 255));
        }
        selectedItem = position;
    }

    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0 )
                return new FragmentCabinetsAndCards();
            else if (position == 1)
                return new FragmentPrices();
            else
                return new FragmentMaps();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private void enqueueCall(Call<GetPetrolStationResult> call) {
        call.enqueue(new Callback<GetPetrolStationResult>() {
            @Override
            public void onResponse(Call<GetPetrolStationResult> call, Response<GetPetrolStationResult> response) {
                if(response.isSuccessful()){
                    GetPetrolStationResult stationResult = response.body();

                    if(stationResult != null && stationResult.getErrorCode() == 0 ){
                        BaseApp.getAppInstance().setPetrolStations(stationResult.getPetrolStations());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPetrolStationResult> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }
}