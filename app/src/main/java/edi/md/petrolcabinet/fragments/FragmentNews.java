package edi.md.petrolcabinet.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.adapters.PressListAdapter;
import edi.md.petrolcabinet.realm.objects.Company;
import edi.md.petrolcabinet.realm.objects.PressObjects;
import edi.md.petrolcabinet.remoteSettings.ApiUtils;
import edi.md.petrolcabinet.remoteSettings.CommandServices;
import edi.md.petrolcabinet.remoteSettings.RemoteException;
import edi.md.petrolcabinet.remote.press.PressList;
import edi.md.petrolcabinet.remote.press.PressResponse;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Igor on 30.06.2020
 */

public class FragmentNews extends Fragment {

    RecyclerView recyclerView;
    Spinner companySpinner;
    List<Company> companyList = new ArrayList<>();
    Realm mRealm;

    RealmResults<PressObjects> results;

    PressListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootViewAdmin = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView = rootViewAdmin.findViewById(R.id.list_news);
        companySpinner = rootViewAdmin.findViewById(R.id.spinner_news);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRealm = Realm.getDefaultInstance();



        RealmResults<Company> companyRealmResults = mRealm.where(Company.class).equalTo("active",true).findAll();
        if(!companyRealmResults.isEmpty()){
            companyList.addAll(mRealm.copyFromRealm(companyRealmResults));

            List<String> companyName = new ArrayList<>();
            companyName.add(getString(R.string.all_company_news));

            for(Company company : companyList){
                companyName.add(company.getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_custom, R.id.text_spiner_item, companyName);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_custom);

            companySpinner.setAdapter(adapter);
            companySpinner.setSelection(0);
        }

        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    if(!companyList.isEmpty()){

                        results = mRealm.where(PressObjects.class).sort("dateTime", Sort.DESCENDING).findAll();
                        adapter = new PressListAdapter(results,true);
                        recyclerView.setAdapter(adapter);


//                        for(Company company: companyList){
//
//                            CommandServices commandServices = ApiUtils.getCommandServices(company.getIp());
//                            Call<PressResponse> call = commandServices.getPress(company.getServiceName(),company.getIdPress(),0);
//
//                            enqueueCall(call, company);
//                        }
                    }
                }
                else{
                    int position = i - 1;

                    Company company =  companyList.get(position);

                    results = mRealm.where(PressObjects.class).sort("dateTime", Sort.DESCENDING).equalTo("companyId", company.getId()).findAll();
                    adapter = new PressListAdapter(results,true);
                    recyclerView.setAdapter(adapter);

                    CommandServices commandServices = ApiUtils.getCommandServices(company.getIp());
                    Call<PressResponse> call = commandServices.getPress(company.getServiceName(),company.getIdPress(),0);

                    enqueueCall(call, company);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootViewAdmin;
    }

//    private void fetchData(String serviceName) {
//        compositeDisposable.add(commandServices.getPressRx(serviceName, -1, 0)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<PressResponse>() {
//                    @Override
//                    public void accept(PressResponse posts) throws OnErrorNotImplementedException {
//                        displayData(posts.getPressList());
//                    }
//
//
//                }));
//    }

//    private void displayData(List<PressList> posts) {
//        NewsViewAdapter adapter = new NewsViewAdapter(getContext(),posts);
//        recyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    public void onStop() {
//        compositeDisposable.clear();
//        super.onStop();
//    }

    private void enqueueCall(Call<PressResponse> call, Company company) {
        call.enqueue(new Callback<PressResponse>() {
            @Override
            public void onResponse(Call<PressResponse> call, Response<PressResponse> response) {
                PressResponse pressResponse = response.body();
                if(pressResponse != null){
                    if(pressResponse.getErrorCode() == 0){
                        List<PressList> pressList = pressResponse.getPressList();

                        if(pressList.size() > 0){
                            int idPress = company.getIdPress();
                            for(PressList press: pressList){
                                int idReceivPress = press.getID();
                                if(idReceivPress > idPress)
                                    idPress = idReceivPress;

                                PressObjects object = new PressObjects();

                                object.setCompanyId(company.getId());
                                object.setCompanyName(company.getName());
                                object.setCompanyLogo(company.getLogo());
                                object.setContent(press.getContent());
                                object.setHeader(press.getHeader());
                                object.setImage(press.getPicture());
                                object.setId(press.getID());
                                object.setType(press.getPressType());
                                String time = press.getDateTime();
                                time = time.replace("/Date(","");
                                time = time.replace("+0300)/","");
                                time = time.replace("+0200)/","");

                                long timeLong = Long.parseLong(time);
                                object.setDateTime(timeLong);

                                mRealm.executeTransaction(realm -> {
                                    realm.insert(object);
                                });
                            }
                            int finalIdPress = idPress;
                            mRealm.executeTransaction(realm -> {
                                Company com = realm.where(Company.class).equalTo("id",company.getId()).findFirst();
                                if(com != null)
                                    com.setIdPress(finalIdPress);
                            });


//                            PressList item = pressList.get(0);
//                            for (int i = 0; i < 10; i++){
//                                pressList.add(item);
//                            }
                        }
//                        NewsViewAdapter adapter = new NewsViewAdapter(getContext(),pressList);
//                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        String msg = RemoteException.getServiceException(pressResponse.getErrorCode());

                        new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                                .setTitle(getString(R.string.oops_text)      )
                                .setMessage(msg)
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                })
                                .show();
                    }
                }
                else{

                }
            }

            @Override
            public void onFailure(Call<PressResponse> call, Throwable t) {
                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                        .setTitle(getString(R.string.oops_text)      )
                        .setMessage(getString(R.string.dialog_failure_service) + t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .show();
            }
        });
    }
}
