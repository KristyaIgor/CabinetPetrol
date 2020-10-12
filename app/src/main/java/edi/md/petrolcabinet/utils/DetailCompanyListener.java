package edi.md.petrolcabinet.utils;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edi.md.petrolcabinet.DetailCompanyActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.fragments.FragmentCabinetsAndCards;
import edi.md.petrolcabinet.fragments.FragmentPrices;
import edi.md.petrolcabinet.fragments.FragmentMaps;


/**
 * Created by Igor on 24.06.2020
 */

public class DetailCompanyListener implements View.OnClickListener {
    Context context;
    List<View> viewList;

    static final int idTextContracts= R.id.textView_contracts;
    static final int idTextPrices = R.id.textView_prices;
    static final int idTextMaps = R.id.textView_maps;

    static final int idImgContracts = R.id.imageView_contracts;
    static final int idImgPrices = R.id.imageView_prices;
    static final int idImgMaps= R.id.imageView_maps;

    final static int idLayoutContracts = R.id.layout_contracts;
    final static int idLayoutPrices = R.id.layout_prices;
    final static int idLayoutMaps = R.id.layout_maps;


    public DetailCompanyListener(Context context, List<View> views) {
        this.context = context;
        this.viewList = views;

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
                    case idLayoutContracts : {
                        setActiveLayout(view1,idTextContracts, idImgContracts);
                        DetailCompanyActivity.replaceFragment(new FragmentCabinetsAndCards());
                    }break;
                    case idLayoutPrices: {
                        setActiveLayout(view1,idTextPrices, idImgPrices);
                        DetailCompanyActivity.replaceFragment(new FragmentPrices());
                    }break;
                    case idLayoutMaps: {
                        setActiveLayout(view1,idTextMaps, idImgMaps);
                        DetailCompanyActivity.replaceFragment(new FragmentMaps());
                    }break;
                }
            }
            else { // set inactive view
                view1.setSelected(false);
                switch (view1.getId()){
                    case idLayoutContracts : {
                        setInActiveLayout(view1,idTextContracts, idImgContracts);
                    }break;
                    case idLayoutPrices: {
                        setInActiveLayout(view1,idTextPrices, idImgPrices);
                    }break;
                    case idLayoutMaps: {
                        setInActiveLayout(view1,idTextMaps, idImgMaps);
                    }break;
                }
            }
        }
    }


    private void setActiveLayout(View layout, int idText, int idImg){
        TextView text = layout.findViewById(idText);
        ImageView image = layout.findViewById(idImg);

        text.setTypeface(Typeface.DEFAULT_BOLD);
        image.setColorFilter(Color.rgb(255, 255, 255));
    }

    private void setInActiveLayout(View layout, int idText, int idImg){
        TextView text = layout.findViewById(idText);
        ImageView image = layout.findViewById(idImg);

        text.setTypeface(Typeface.DEFAULT);
        image.setColorFilter(context.getColor(R.color.green));
    }

    public static void setActiveContractLayout(View layout){
        layout.setSelected(true);
        TextView text = layout.findViewById(idTextContracts);
        ImageView image = layout.findViewById(idImgContracts);

        text.setTypeface(Typeface.DEFAULT_BOLD);
        image.setColorFilter(Color.rgb(255, 255, 255));

        DetailCompanyActivity.replaceFragment(new FragmentCabinetsAndCards());
    }
}
