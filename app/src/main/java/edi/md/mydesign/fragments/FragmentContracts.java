package edi.md.mydesign.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edi.md.mydesign.ContractActivity;
import edi.md.mydesign.R;
import edi.md.mydesign.adapters.ContractsAdapter;
import edi.md.mydesign.models.ContractItem;

/**
 * Created by Igor on 30.06.2020
 */

public class FragmentContracts extends Fragment {

    ListView list_contracts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootViewAdmin = inflater.inflate(R.layout.fragment_contracts, container, false);

        list_contracts = rootViewAdmin.findViewById(R.id.list_contracts);

        List<ContractItem> list = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            ContractItem item = new ContractItem();
            item.setName("NAme " + i);
            item.setNr("nr 5949" + i);

            list.add(item);
        }

        ContractsAdapter adapter = new ContractsAdapter(getContext(),R.layout.item_list_contract,list);
        list_contracts.setAdapter(adapter);

        list_contracts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getContext(), ContractActivity.class));
            }
        });


        return rootViewAdmin;
    }
}
