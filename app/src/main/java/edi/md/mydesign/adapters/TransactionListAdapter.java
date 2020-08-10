package edi.md.mydesign.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edi.md.mydesign.R;
import edi.md.mydesign.remote.transaction.Transaction;

/**
 * Created by Igor on 20.07.2020
 */

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {

    private static final String TAG = "TransactionListAdapter";

    //vars
    private List<Transaction>  mNames = new ArrayList<>();
    private Context mContext;

    public TransactionListAdapter(Context context, List<Transaction> names){
        this.mNames = names;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_last_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Transaction item = mNames.get(position);

        if(item.getAmount() > 0){
            holder.type.setText("Income");
            holder.amount.setText("+" + item.getAmount());
            holder.amount.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.name.setText("");
        }
        else{
            holder.type.setText("Spent");
            holder.amount.setText(String.valueOf(item.getAmount()));
            holder.amount.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.name.setText(item.getCardCode() + " " + item.getCardName());
        }
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,type,amount;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.text_type_tr);
            amount = itemView.findViewById(R.id.amount_transaction);
        }
    }


}