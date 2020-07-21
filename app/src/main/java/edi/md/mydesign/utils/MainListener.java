package edi.md.mydesign.utils;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edi.md.mydesign.MainActivity;
import edi.md.mydesign.R;
import edi.md.mydesign.fragments.FragmentCompanies;
import edi.md.mydesign.fragments.FragmentContracts;
import edi.md.mydesign.fragments.FragmentNews;


/**
 * Created by Igor on 24.06.2020
 */

public class MainListener implements View.OnClickListener {
    Context context;
    List<View> viewList;

    static TextView title;

    final int idNews = R.id.textView_news;
    static final int idContracts = R.id.textView_contracts;

    final int idNewsImg = R.id.imageView_news;
    static final int idContractsImg = R.id.imageView_contracts;

    final int id_News = R.id.layout_news;
    final int id_Contracts = R.id.layout_companies;

    public MainListener(Context context, List<View> views, View title) {
        this.context = context;
        this.viewList = views;
        this.title = (TextView) title;

    }

    @Override
    public void onClick(View view) {
        setSelectedView(view);
    }

    private void setSelectedView(View view) {
        for(View view1 : viewList){
            if(view1 == view){ // set active item
                view1.setSelected(true);
                switch (view1.getId()){
                    case id_News : {
                        setActiveLayout(view1,idNews, idNewsImg, "News");
                        MainActivity.replaceFragment(new FragmentNews());
                    }break;
                    case id_Contracts: {
                        setActiveLayout(view1,idContracts, idContractsImg, "Companies");
                        MainActivity.replaceFragment(new FragmentCompanies());
                    }break;
                }
            }
            else { // set inactive view
                view1.setSelected(false);
                switch (view1.getId()){
                    case id_News : {
                        setInActiveLayout(view1,idNews, idNewsImg);
                    }break;
                    case id_Contracts: {
                        setInActiveLayout(view1,idContracts, idContractsImg);
                    }break;
                }
            }
        }
    }


    private void setActiveLayout(View layout, int idText, int idImg, String msg){
        TextView text = layout.findViewById(idText);
        ImageView image = layout.findViewById(idImg);

        text.setTypeface(Typeface.DEFAULT_BOLD);
        image.setColorFilter(Color.rgb(255, 255, 255));
        title.setText(msg);
    }

    private void setInActiveLayout(View layout, int idText, int idImg){
        TextView text = layout.findViewById(idText);
        ImageView image = layout.findViewById(idImg);

        text.setTypeface(Typeface.DEFAULT);
        image.setColorFilter(Color.rgb(0, 197, 105));
    }

    public static void setActiveMainLayout(View layout){
        layout.setSelected(true);
        TextView text = layout.findViewById(idContracts);
        ImageView image = layout.findViewById(idContractsImg);

        text.setTypeface(Typeface.DEFAULT_BOLD);
        image.setColorFilter(Color.rgb(255, 255, 255));
        title.setText("Companies");

        MainActivity.replaceFragment(new FragmentCompanies());
    }
}
