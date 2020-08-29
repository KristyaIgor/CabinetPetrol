package edi.md.mydesign.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edi.md.mydesign.R;
import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.remote.contract.CardsList;
import edi.md.mydesign.utils.BaseEnum;

/**
 * Created by Igor on 20.07.2020
 */

public class CardsInContractClientAdapter extends RecyclerView.Adapter<CardsInContractClientAdapter.ViewHolder> {

    List<CardsList> mData;

    public CardsInContractClientAdapter(Context context, List<CardsList> list){
        this.mData = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        CardsList item = mData.get(position);

        holder.code.setText(item.getCode());
        holder.name.setText(item.getName());
        if(item.getSeparateClientAccount()){
            holder.balance.setVisibility(View.VISIBLE);
            holder.balance.setText("Balanța: " + item.getBalanceAccountCard() + " MDL");
        }
        else{
            holder.balance.setVisibility(View.GONE);
        }

        if(item.getLimitType() == BaseEnum.LimitBani){
            holder.limitDay.setText(item.getDailyLimit() + " MDL");
            holder.limitWeek.setText(item.getWeeklyLimit() + " MDL");
            holder.limitMonth.setText(item.getMonthlyLimit() + " MDL");
        }
        else{
            holder.limitDay.setText(item.getDailyLimit() + " L");
            holder.limitWeek.setText(item.getWeeklyLimit() + " L");
            holder.limitMonth.setText(item.getMonthlyLimit() + " L");
        }

        boolean state = item.getIsActive();

        if(state){
            holder.cardState.setImageResource(R.drawable.ic_state_contract_active);
        }
        else{
            holder.cardState.setImageResource(R.drawable.ic_state_contract_inactive);
        }

    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public CardsList getItem (int position) {
        return mData.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,code, balance, limitDay, limitWeek, limitMonth;
        ImageView cardState;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_card_name);
            code = itemView.findViewById(R.id.text_card_code);
            balance = itemView.findViewById(R.id.text_balance_card);
            limitDay = itemView.findViewById(R.id.text_card_limit_day);
            limitWeek = itemView.findViewById(R.id.text_card_limit_week);
            limitMonth = itemView.findViewById(R.id.text_card_limit_month);
            cardState = itemView.findViewById(R.id.image_card_state);
        }
    }


}