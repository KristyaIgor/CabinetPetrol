package edi.md.mydesign.fragments.slidePage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import edi.md.mydesign.R;


/**
 * Created by Igor on 21.07.2020
 */

public class FragmentTotalBalance extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private double mParam1;
    private String mParam2;

    public FragmentTotalBalance() {
        // Required empty public constructor
    }

    public static FragmentTotalBalance newInstance(String param1,double param2) {
        FragmentTotalBalance fragment = new FragmentTotalBalance();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putDouble(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getDouble(ARG_PARAM2);
            mParam2 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_total_balance, container, false);

        TextView totalBAlance = rootView.findViewById(R.id.text_total_balance);
        TextView titleBalance = rootView.findViewById(R.id.title_balance);
        totalBAlance.setText(String.valueOf(mParam1) + " MDL");
        titleBalance.setText(mParam2);

        return rootView;
    }
}
