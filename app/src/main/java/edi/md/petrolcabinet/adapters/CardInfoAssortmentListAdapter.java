package edi.md.petrolcabinet.adapters;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.remote.cardInfo.CardInfoAssortment;

/**
 * Created by Igor on 20.07.2020
 */

public class CardInfoAssortmentListAdapter extends RecyclerView.Adapter<CardInfoAssortmentListAdapter.ViewHolder> {

    List<CardInfoAssortment> mData;
    private Context mContext;

    public CardInfoAssortmentListAdapter(Context context, List<CardInfoAssortment> list){
        this.mData = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carditem_product_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        CardInfoAssortment item = mData.get(position);

        holder.name.setText(item.getName());

        String limType = "";

        if(item.getLimitType() == 0)
            limType = mContext.getString(R.string.lei_text);
        else
            limType = mContext.getString(R.string.litri_text);

        setPieChart(holder.chartDay);
        setPieChart(holder.chartWeek);
        setPieChart(holder.chartMonth);

        setDataToChard(holder.chartDay, item.getDailyLimitUsed(), item.getDailyLimitRemain(), item.getDailyLimit(), limType);
        setDataToChard(holder.chartWeek, item.getWeeklyLimitUsed(), item.getWeeklyLimitRemain(), item.getWeeklyLimit(), limType);
        setDataToChard(holder.chartMonth, item.getMonthLimitUsed(), item.getMonthLimitRemain(), item.getMonthlyLimit(), limType);

        if(item.getAditionalLimit() > 0){
            holder.additionalLimit.setText(item.getAditionalLimit() + limType);

            if(item.getAdditionalLimitDate() != null)
                holder.additionalLimitDate.setText(item.getAdditionalLimitDate());
            else
                holder.additionalLimitDate.setText("");
        }
        else{
            holder.additionalLimit.setText(0 + limType);
            holder.additionalLimitDate.setText("");
        }

        holder.limitDay.setText(item.getDailyLimit() + limType);
        holder.limitWeek.setText(item.getWeeklyLimit() + limType);
        holder.limitMont.setText(item.getMonthlyLimit() + limType);

        holder.remainDay.setText(item.getDailyLimitRemain() + limType);
        holder.remainWeek.setText(item.getWeeklyLimitRemain() + limType);
        holder.remainMonth.setText(item.getMonthLimitRemain() + limType);

    }

    private void setDataToChard(PieChart chart, Integer limitUsed, Double limitRemain, Integer limit, String limitType) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mContext.getTheme();
        theme.resolveAttribute(R.attr.textColor, typedValue, true);
        TypedArray arr = mContext.obtainStyledAttributes(typedValue.data, new int[]{R.attr.textColor});
        int primaryColor = arr.getColor(0, -1);

        if(limitUsed == 0 && limitRemain == 0){
            colors.add( Color.rgb(225, 229, 235)); //gray

            PieEntry entry = new PieEntry(limit);
            entries.add(entry);

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setDrawValues(false);
            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);

            chart.setCenterText(limit + limitType);
            chart.setCenterTextColor(primaryColor);

            chart.setData(data);
        }
        else {
            colors.add( Color.rgb(20, 122, 214)); //blue
            colors.add( Color.rgb(225, 229, 235)); //gray

            PieEntry entryUsed = new PieEntry(limitUsed);

            float remain = Float.parseFloat(String.valueOf(2460.0));
            PieEntry entryRemain = new PieEntry(remain);

            entries.add(entryUsed);
            entries.add(entryRemain);

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setDrawValues(false);
            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);

            chart.setCenterText(limitUsed + limitType);
            chart.setCenterTextColor(primaryColor);

            chart.setData(data);
        }

    }

    private void setPieChart(PieChart chart) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mContext.getTheme();
        theme.resolveAttribute(R.attr.itemsBg, typedValue, true);
        TypedArray arr = mContext.obtainStyledAttributes(typedValue.data, new int[]{R.attr.itemsBg});
        int primaryColor = arr.getColor(0, -1);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(primaryColor);
        chart.setCenterTextSize(12);
        chart.setDrawCenterText(true);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(78f);
//        chart.setDrawRoundedSlices(true);
        chart.setDrawEntryLabels(false);
        chart.setTouchEnabled(false);
        chart.highlightValues(null);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, limitDay, limitWeek, limitMont, additionalLimit, additionalLimitDate, remainDay, remainWeek, remainMonth;
        private PieChart chartDay, chartWeek, chartMonth;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_produs_name);

            chartDay = itemView.findViewById(R.id.product_chard_day_limit);
            chartWeek = itemView.findViewById(R.id.product_chard_week_limit);
            chartMonth = itemView.findViewById(R.id.product_chard_month_limit);

            limitDay = itemView.findViewById(R.id.product_limit_day);
            limitWeek = itemView.findViewById(R.id.product_limit_week);
            limitMont = itemView.findViewById(R.id.product_limit_month);

            remainDay = itemView.findViewById(R.id.text_remain_day);
            remainWeek = itemView.findViewById(R.id.text_remain_week);
            remainMonth = itemView.findViewById(R.id.text_remain_month);

            additionalLimit = itemView.findViewById(R.id.product_additional_limit_money);
            additionalLimitDate = itemView.findViewById(R.id.product_adittional_limit_date);
        }
    }
}