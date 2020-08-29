package edi.md.mydesign.fragments.slidePage;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import edi.md.mydesign.R;


/**
 * Created by Igor on 21.07.2020
 */

public class FragmentCardLimitsInfo extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    PieChart chartCard;
    TextView consum, remain;

    private static int mParam1;  // limita
    private static int mParam2; // consum
    private static int mParam3; //remain
    private static int mParam4; //limit type

    public FragmentCardLimitsInfo() {
        // Required empty public constructor
    }

    public static FragmentCardLimitsInfo newInstance(int param1, int param2, int param3, int param4) {
        FragmentCardLimitsInfo fragment = new FragmentCardLimitsInfo();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        args.putInt(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            mParam3 = getArguments().getInt(ARG_PARAM3);
            mParam4 = getArguments().getInt(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_card_chart_info, container, false);

        chartCard = rootView.findViewById(R.id.card_chard);
        consum = rootView.findViewById(R.id.text_consum_card_chart);
        remain = rootView.findViewById(R.id.text_remain_card_chart);

        String typeLimit = "";

        if(mParam4 == 0)
            typeLimit = " MDL";
        else
            typeLimit = " L";

        consum.setText(mParam2 + typeLimit);
        remain.setText(mParam3 + typeLimit);

        setPieChart(chartCard);
        setDataToChard(chartCard, mParam2, mParam3, mParam1, typeLimit);

        return rootView;
    }

    private void setPieChart(PieChart chart) {
        chart.setCenterTextSize(18);
        chart.setDrawCenterText(true);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setTransparentCircleAlpha(180);
        chart.setHoleRadius(68f);
//        chart.setDrawRoundedSlices(true);
        chart.setDrawEntryLabels(false);
        chart.setTouchEnabled(false);
        chart.highlightValues(null);
    }

    private void setDataToChard(PieChart chart, int limitUsed, int limitRemain, int limit, String limitType) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();



        colors.add(getResources().getColor(R.color.consum_chart)); //gray
        colors.add(getResources().getColor(R.color.remain_chart)); //blue

        PieEntry entryUsed = new PieEntry(limitUsed);

        float remain = Float.parseFloat(String.valueOf(limitRemain));
        PieEntry entryRemain = new PieEntry(remain);

        entries.add(entryUsed);
        entries.add(entryRemain);

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        chart.setCenterText(generateCenterSpannableText(limit ,limitType));

        chart.setData(data);

    }

    private SpannableString generateCenterSpannableText(int limit, String limitType) {

        SpannableString s = new SpannableString("Limita" + "\n" + limit + limitType);
        s.setSpan(new RelativeSizeSpan(.7f), 0, 6, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 6, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.indicator_selected)), 6, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.2f), 6, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.indicator_unselected)), s.length(), s.length(), 0);
        return s;
    }
}
