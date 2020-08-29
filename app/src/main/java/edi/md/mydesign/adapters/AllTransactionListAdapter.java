package edi.md.mydesign.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edi.md.mydesign.R;
import edi.md.mydesign.remote.transaction.Transaction;

/**
 * Created by Igor on 20.07.2020
 */

public class AllTransactionListAdapter extends RecyclerView.Adapter<AllTransactionListAdapter.ViewHolder> {

    private static final String TAG = "TransactionListAdapter";

    //vars
    private static List<Transaction>  mNames = new ArrayList<>();
    private Context mContext;


    public AllTransactionListAdapter(Context context, List<Transaction> names){
        this.mNames = names;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_transaction, parent, false);
        return new ViewHolder(view);
    }

    public Transaction getItem (int position){
        return mNames.get(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Transaction item = mNames.get(position);

        holder.date.setText(item.getDocumentDate());

        if(item.getAmount() > 0){
            holder.amount.setText("+" + item.getAmount() + " MDL");
            holder.amount.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.cardName.setText("Suplinire");

            if(item.getStation() != null){
                holder.location.setVisibility(View.VISIBLE);
                holder.location.setText(item.getStation());
            }
            else{
                holder.location.setVisibility(View.GONE);
            }
        }
        else{
            holder.amount.setText(item.getAmount() + " MDL");
            holder.amount.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.cardName.setText(item.getCardCode() + " / " + item.getCardName());

            if(item.getStation() != null){
                holder.location.setVisibility(View.VISIBLE);
                holder.location.setText(item.getStation());
            }
            else{
                holder.location.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView cardName,amount, location, date;

        ImageView imageTrans;

        public ViewHolder(View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.text_card_name);
            imageTrans = itemView.findViewById(R.id.image_type_transaction);
            amount = itemView.findViewById(R.id.amount_transaction);
            location = itemView.findViewById(R.id.text_location);
            date = itemView.findViewById(R.id.text_date_transaction);
        }
    }


}