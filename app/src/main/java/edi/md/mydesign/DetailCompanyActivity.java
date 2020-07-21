package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edi.md.mydesign.realm.objects.Company;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.petrolStation.GetPetrolStationResult;
import edi.md.mydesign.utils.DetailCompanyListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCompanyActivity extends AppCompatActivity {
    ConstraintLayout layoutContract, layoutPrices, layoutMaps;
    TextView titleCompany;
    ImageButton backButton;

    static FragmentManager fgManager;
    Context context;

    Company companySelected;

    public static DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_company);

        layoutContract = findViewById(R.id.layout_contracts);
        layoutPrices = findViewById(R.id.layout_prices);
        layoutMaps = findViewById(R.id.layout_maps);
        titleCompany = findViewById(R.id.title_company);
        backButton = findViewById(R.id.btn_back);

        fgManager = getSupportFragmentManager();
        context = this;

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        companySelected = BaseApp.getAppInstance().getCompanyClicked();

        titleCompany.setText(companySelected.getName());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        List<View> list = new ArrayList<>();
        list.add(layoutContract);
        list.add(layoutPrices);
        list.add(layoutMaps);

        DetailCompanyListener listener = new DetailCompanyListener(this, list);

        layoutContract.setOnClickListener(listener);
        layoutPrices.setOnClickListener(listener);
        layoutMaps.setOnClickListener(listener);

        DetailCompanyListener.setActiveContractLayout(layoutContract);

        CommandServices commandServices = ApiUtils.getCommandServices(companySelected.getAddress());
        Call<GetPetrolStationResult> call = commandServices.getPetrolStation();

        call.enqueue(new Callback<GetPetrolStationResult>() {
            @Override
            public void onResponse(Call<GetPetrolStationResult> call, Response<GetPetrolStationResult> response) {
                if(response.isSuccessful()){
                    GetPetrolStationResult stationResult = response.body();

                    if(stationResult.getErrorCode() == 0 ){
                        BaseApp.getAppInstance().setPetrolStations(stationResult.getPetrolStations());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPetrolStationResult> call, Throwable t) {

            }
        });
    }

    public static void replaceFragment(Fragment fragment){
        fgManager.beginTransaction().replace(R.id.container_company,fragment).commit();
    }
}