package edi.md.petrolcabinet.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.remote.contract.ProductsList;

/**
 * Created by Igor on 20.07.2020
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private static final String TAG = "TransactionListAdapter";

    List<ProductsList> mData;
    private Context mContext;

    public ProductListAdapter(Context context, List<ProductsList> list){
        this.mData = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ProductsList item = mData.get(position);

        if(item.getName().contains("A-92")) {
            holder.name.setText("A-92");
        }
        else if(item.getName().contains("Eurodiesel")){
            holder.name.setText("Diesel");
        }
        else if(item.getName().contains("Gaz")){
            holder.name.setText("Gaz");
        }
        else if(item.getName().contains("A-95")){
            holder.name.setText("A-95");
        }
        else{
            holder.name.setText(item.getName());
        }

        if(item.getPrice() == 0){
           holder.price.setText(item.getDiscount() + " %");
        }
        else{
            holder.price.setText(item.getPrice() + mContext.getString(R.string.lei_text));
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, price;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_produs_name);
            price = itemView.findViewById(R.id.text_price_discount_produs);
        }
    }


}