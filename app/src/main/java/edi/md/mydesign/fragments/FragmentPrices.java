package edi.md.mydesign.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import edi.md.mydesign.BaseApp;
import edi.md.mydesign.R;
import edi.md.mydesign.adapters.PricesAdapter;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.prices.GetPriceResult;
import edi.md.mydesign.remote.prices.Price;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Igor on 06.07.2020
 */

public class FragmentPrices extends Fragment {

    ListView listView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootViewAdmin = inflater.inflate(R.layout.fragment_prices, container, false);

        listView = rootViewAdmin.findViewById(R.id.list_prices);
        swipeRefreshLayout = rootViewAdmin.findViewById(R.id.swiperefresh);
        progressBar = rootViewAdmin.findViewById(R.id.progressBar_prices);

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getAddress());

        getPrices(commandServices,false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getAddress());

                getPrices(commandServices,true);
            }
        });

        return rootViewAdmin;
    }

    private void getPrices(CommandServices commandServices,boolean onRefresh){
        Call<GetPriceResult> call = commandServices.getPrices();
        call.enqueue(new Callback<GetPriceResult>() {
            @Override
            public void onResponse(Call<GetPriceResult> call, Response<GetPriceResult> response) {
                if(response.isSuccessful()){

                    GetPriceResult priceResult = response.body();
                    if (priceResult != null && priceResult.getErrorCode() == 0) {
                        List<Price> prices = priceResult.getPrices();
                        PricesAdapter adapter = new PricesAdapter(getLayoutInflater(), prices);

                        listView.setAdapter(adapter);
                        if(onRefresh)
                            swipeRefreshLayout.setRefreshing(false);
                        else{
                            progressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            call.cancel();
                        }
                    }
                    else{
                        progressBar.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onFailure(Call<GetPriceResult> call, Throwable t) {
                Log.w("TAG retrofit", "Call is canceled: " + call.isCanceled());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
