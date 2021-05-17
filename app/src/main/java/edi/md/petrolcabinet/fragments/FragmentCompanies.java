package edi.md.petrolcabinet.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edi.md.petrolcabinet.BaseApp;
import edi.md.petrolcabinet.CompanyActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.adapters.CompaniesAdapter;
import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.realm.objects.Company;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Igor on 06.07.2020
 */

public class FragmentCompanies extends Fragment {

    static ListView listCompanies;

    static Realm mRealm;
    static CompaniesAdapter adapter;
    static Context context;
    static Activity activity;

    private static FragmentCompanies sameInstanceFragment;

    public static FragmentCompanies getInstance() {
        sameInstanceFragment = sameInstanceFragment == null ? new FragmentCompanies() : sameInstanceFragment;
        return sameInstanceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootViewAdmin = inflater.inflate(R.layout.fragment_companies, container, false);

        listCompanies = rootViewAdmin.findViewById(R.id.list_companies);

        mRealm = Realm.getDefaultInstance();
        context = getContext();
        activity = getActivity();

        listCompanies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Company item = adapter.getItem(i);

                if(item.isActive()){
                    Company fromRealm = mRealm.copyFromRealm(item);
                    BaseApp.getAppInstance().setCompanyClicked(fromRealm);

                    Intent detail = new Intent(getContext(), CompanyActivity.class);
                    startActivity(detail);
                }
                else{
                    Toast.makeText(getContext(), getString(R.string.msg_this_company_inactive), Toast.LENGTH_SHORT).show();
                }
            }
        });
        updateListCompanies();



        return rootViewAdmin;
    }

    public static void updateListCompanies() {
        RealmResults<Company> result = mRealm.where(Company.class).findAll();
        if(!result.isEmpty()){
            for(Company item: result){
                RealmResults<Accounts> clientRealms = mRealm.where(Accounts.class).equalTo("companyId", item.getId()).findAll();
                if(!clientRealms.isEmpty()){
                    mRealm.executeTransaction(realm -> {
                        item.setExistContracts(true);
                        item.setNumberContracts(clientRealms.size());
                    });
                }
                else{
                    mRealm.executeTransaction(realm -> {
                        item.setExistContracts(false);
                        item.setNumberContracts(0);
                    });
                }
            }
            result = result.sort("name", Sort.ASCENDING);
            result = result.sort("numberContracts", Sort.DESCENDING);
            result = result.sort("active", Sort.DESCENDING);
            adapter = new CompaniesAdapter(result);
            listCompanies.setAdapter(adapter);
        }
    }
}
