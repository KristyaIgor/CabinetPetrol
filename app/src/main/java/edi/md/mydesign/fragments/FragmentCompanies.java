package edi.md.mydesign.fragments;


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

import java.util.ArrayList;
import java.util.List;

import edi.md.mydesign.BaseApp;
import edi.md.mydesign.DetailCompanyActivity;
import edi.md.mydesign.R;
import edi.md.mydesign.adapters.CompaniesAdapter;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.realm.objects.Company;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Igor on 06.07.2020
 */

public class FragmentCompanies extends Fragment {

    ListView listCompanies;

    Realm mRealm;
    CompaniesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootViewAdmin = inflater.inflate(R.layout.fragment_companies, container, false);

        listCompanies = rootViewAdmin.findViewById(R.id.list_companies);

        mRealm = Realm.getDefaultInstance();

        RealmResults<Company> result = mRealm.where(Company.class).findAll();
        if(!result.isEmpty()){

            for(Company item: result){
                RealmResults<ClientRealm> clientRealms = mRealm.where(ClientRealm.class).equalTo("companyId", item.getId()).findAll();
                if(!clientRealms.isEmpty()){
                    mRealm.executeTransaction(realm -> {
                        item.setExistContracts(true);
                        item.setNumberContracts(clientRealms.size());
                    });
                }
            }
            result = result.sort("numberContracts", Sort.DESCENDING);
            adapter = new CompaniesAdapter(result);
            listCompanies.setAdapter(adapter);
        }

        listCompanies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Company item = adapter.getItem(i);

                BaseApp.getAppInstance().setCompanyClicked(item);

                Intent detail = new Intent(getContext(), DetailCompanyActivity.class);
                startActivity(detail);
            }
        });

        return rootViewAdmin;
    }
}
