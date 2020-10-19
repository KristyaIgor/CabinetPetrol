package edi.md.petrolcabinet.fragments.slidePage;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import edi.md.petrolcabinet.R;


/**
 * Created by Igor on 21.07.2020
 */

public class FragmentCardLimitsInfoWeek extends Fragment {

    private static final String ARG_LIMIT = "param1";
    private static final String ARG_USED = "param2";
    private static final String ARG_REMAIN = "param3";
    private static final String ARG_TYPE = "param4";

    PieChart chartCard;
    TextView consum, remain;

    private static int mLimit;  // limita
    private static int mUsed; // consum
    private static int mRemain; //remain
    private static int mTypeLimit; //limit type

    private static FragmentCardLimitsInfoWeek sameInstanceFragment;

    public FragmentCardLimitsInfoWeek() {
        // Required empty public constructor
    }

    public static FragmentCardLimitsInfoWeek newInstance(int limit, int used, int remain, int limitType) {
        sameInstanceFragment = new FragmentCardLimitsInfoWeek();
        Bundle args = new Bundle();
        args.putInt(ARG_LIMIT, limit);
        args.putInt(ARG_USED, used);
        args.putInt(ARG_REMAIN, remain);
        args.putInt(ARG_TYPE, limitType);
        sameInstanceFragment.setArguments(args);

        return sameInstanceFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_card_chart_info, container, false);

        chartCard = rootView.findViewById(R.id.card_chard);
        consum = rootView.findViewById(R.id.text_consum_card_chart);
        remain = rootView.findViewById(R.id.text_remain_card_chart);

        if (getArguments() != null) {
            mLimit = getArguments().getInt(ARG_LIMIT);
            mUsed = getArguments().getInt(ARG_USED);
            mRemain = getArguments().getInt(ARG_REMAIN);
            mTypeLimit = getArguments().getInt(ARG_TYPE);
        }

        String typeLimit = "";

        if(mTypeLimit == 0)
            typeLimit = getString(R.string.lei_text);
        else
            typeLimit = getString(R.string.litri_text);

        consum.setText(mUsed + typeLimit);
        remain.setText(mRemain + typeLimit);

        setPieChart(chartCard);
        setDataToChard(chartCard, mUsed, mRemain, mLimit, typeLimit);

        return rootView;
    }

    private void setPieChart(PieChart chart) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.itemsBg, typedValue, true);
        TypedArray arr = getContext().obtainStyledAttributes(typedValue.data, new int[]{R.attr.itemsBg});
        int primaryColor = arr.getColor(0, -1);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(primaryColor);
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

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.textColor, typedValue, true);
        TypedArray arr = getContext().obtainStyledAttributes(typedValue.data, new int[]{R.attr.textColor});
        int primaryColor = arr.getColor(0, -1);

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
        chart.setCenterText(generateCenterSpannableText(limit ,limitType, primaryColor));
        chart.setCenterTextColor(primaryColor);

        chart.setData(data);

    }

    private SpannableString generateCenterSpannableText(int limit, String limitType, int color) {

        SpannableString s = new SpannableString(getString(R.string.limit_text) + "\n" + limit + limitType);
        s.setSpan(new RelativeSizeSpan(.7f), 0, 6, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 6, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(color), 6, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.2f), 6, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(color), s.length(), s.length(), 0);
        return s;
    }
}
