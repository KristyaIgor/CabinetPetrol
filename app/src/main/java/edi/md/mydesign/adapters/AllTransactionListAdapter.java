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

public class AllTransactionListAdapter extends RecyclerView.Adapter<AllTransactionListAdapter.ViewHolder> {

    private static final String TAG = "TransactionListAdapter";

    //vars
    private List<Transaction>  mNames = new ArrayList<>();
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

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Transaction item = mNames.get(position);

        if(item.getAmount() > 0){
            holder.type.setText("Income");
            holder.amount.setText("+" + item.getAmount());
            holder.amount.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.cardName.setVisibility(View.INVISIBLE);
            holder.assortment.setVisibility(View.INVISIBLE);
            holder.quantity.setVisibility(View.INVISIBLE);
            holder.assortmentTitle.setVisibility(View.INVISIBLE);
            holder.quantityTitle.setVisibility(View.INVISIBLE);
            holder.location.setVisibility(View.INVISIBLE);
            holder.cardNameTitle.setVisibility(View.INVISIBLE);
            holder.date.setText(item.getDocumentDate());
        }
        else{
            holder.type.setText("Spent");
            holder.amount.setText(String.valueOf(item.getAmount()));
            holder.amount.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.cardName.setText(item.getCardCode() + " " + item.getCardName());
            holder.location.setText(item.getStation());
            holder.assortment.setText(item.getAssortment());
            holder.date.setText(item.getDocumentDate());
            holder.quantity.setText(item.getQuantity() + " L");

            holder.cardName.setVisibility(View.VISIBLE);
            holder.assortment.setVisibility(View.VISIBLE);
            holder.quantity.setVisibility(View.VISIBLE);
            holder.assortmentTitle.setVisibility(View.VISIBLE);
            holder.quantityTitle.setVisibility(View.VISIBLE);
            holder.location.setVisibility(View.VISIBLE);
            holder.cardNameTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView cardName,type,amount, assortment, quantity, assortmentTitle, quantityTitle, date, location, cardNameTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.text_card_name);
            type = itemView.findViewById(R.id.text_type_tr);
            amount = itemView.findViewById(R.id.amount_transaction);
            assortment = itemView.findViewById(R.id.text_assortment);
            assortmentTitle = itemView.findViewById(R.id.name_code);
            quantity = itemView.findViewById(R.id.text_quantity);
            quantityTitle = itemView.findViewById(R.id.textView8);
            date = itemView.findViewById(R.id.text_date_transaction);
            location = itemView.findViewById(R.id.text_location);
            cardNameTitle = itemView.findViewById(R.id.textView12);
        }
    }


}