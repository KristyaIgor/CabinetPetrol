package edi.md.mydesign.fragments.slidePage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import edi.md.mydesign.R;


/**
 * Created by Igor on 21.07.2020
 */

public class FragmentBalanceInfo extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mParam1;
    private double mParam2;
    private String mParam3;

    public FragmentBalanceInfo() {
        // Required empty public constructor
    }

    public static FragmentBalanceInfo newInstance(String param1, double param2, String param3) {
        FragmentBalanceInfo fragment = new FragmentBalanceInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putDouble(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getDouble(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_total_balance, container, false);

        TextView totalBAlance = rootView.findViewById(R.id.text_total_balance);
        TextView titleBalance = rootView.findViewById(R.id.title_balance);
        TextView info = rootView.findViewById(R.id.text_info);

        totalBAlance.setText(String.valueOf(mParam2) + " MDL");
        titleBalance.setText(mParam1);
        info.setText(mParam3);

        return rootView;
    }
}
