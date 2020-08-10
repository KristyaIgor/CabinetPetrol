package edi.md.mydesign.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edi.md.mydesign.R;
import edi.md.mydesign.remote.client.ContractInClient;

/**
 * Created by Igor on 20.07.2020
 */

public class ContractsInClientAdapter extends RecyclerView.Adapter<ContractsInClientAdapter.ViewHolder> {

    private static final String TAG = "TransactionListAdapter";

    List<ContractInClient> mData;
    private Context mContext;

    public ContractsInClientAdapter(Context context,  List<ContractInClient> list){
        this.mData = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contract, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ContractInClient item = mData.get(position);

        holder.nrCntracts.setText("Code: " + item.getCode());
        holder.name.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,nrCntracts;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_clientname_contract);
            nrCntracts = itemView.findViewById(R.id.text_nrcontract_contract);
        }
    }


}