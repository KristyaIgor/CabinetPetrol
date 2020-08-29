package edi.md.mydesign.adapters;


import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;
import java.util.Random;

import edi.md.mydesign.R;
import edi.md.mydesign.remote.prices.Price;

/**
 * Created by Igor on 06.07.2020
 */

public class PricesAdapter implements ListAdapter {

    List<Price> mData;
    LayoutInflater inflater;

    public PricesAdapter(LayoutInflater inflater, List<Price> list) {
        this.mData = list;
        this.inflater = inflater;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Price getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View root = inflater.inflate(R.layout.item_list_prices,null);

        Price item = mData.get(position);

        ConstraintLayout parent = root.findViewById(R.id.item_layout);
        TextView name = root.findViewById(R.id.textView_NameGas);
        TextView price = root.findViewById(R.id.textView_PriceGas);

        name.setText(item.getName());
        price.setText(item.getPrice() + " lei");

//        if(item.getName().contains("A-92")){
//            name.setText("A-92");
//            price.setText(item.getPrice() + " lei");
//
//            Drawable background = parent.getBackground();
//
//            if (background instanceof ShapeDrawable) {
//                Random rnd = new Random();
//                int color = Color.rgb(0,204,92);
//                ((ShapeDrawable)background).getPaint().setColor(color);
//                parent.setBackground(background);
//            }
//            else if (background instanceof GradientDrawable) {
//                Random rnd = new Random();
//                int color = Color.rgb(0,204,92);
//                ((GradientDrawable)background).setColor(color);
//                parent.setBackground(background);
//            }
//            else if (background instanceof ColorDrawable) {
//                Random rnd = new Random();
////                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                int color =  Color.rgb(0, 197, 105);
//                ((ColorDrawable)background).setColor(color);
//                parent.setBackground(background);
//            }
//
//        }
//        if(item.getName().contains("Eurodiesel")){
//            name.setText("Diesel");
//            price.setText(item.getPrice() + " lei");
//
//            Drawable background = parent.getBackground();
//
//            if (background instanceof ShapeDrawable) {
//                Random rnd = new Random();
//                int color = Color.rgb(253, 216, 53);
//                ((ShapeDrawable)background).getPaint().setColor(color);
//                parent.setBackground(background);
//            }
//            else if (background instanceof GradientDrawable) {
//                Random rnd = new Random();
//                int color = Color.rgb(253, 216, 53);
//                ((GradientDrawable)background).setColor(color);
//                parent.setBackground(background);
//            }
//            else if (background instanceof ColorDrawable) {
//                Random rnd = new Random();
////                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                int color = Color.rgb(253, 216, 53);
//                ((ColorDrawable)background).setColor(color);
//                parent.setBackground(background);
//            }
//        }
//        if(item.getName().contains("Gaz")){
//            name.setText("Gaz");
//            price.setText(item.getPrice() + " lei");
//
//            Drawable background = parent.getBackground();
//
//            if (background instanceof ShapeDrawable) {
//                Random rnd = new Random();
//                int color = Color.rgb(229, 115, 115);
//                ((ShapeDrawable)background).getPaint().setColor(color);
//                parent.setBackground(background);
//            }
//            else if (background instanceof GradientDrawable) {
//                Random rnd = new Random();
//                int color = Color.rgb(229, 115, 115);
//                ((GradientDrawable)background).setColor(color);
//                parent.setBackground(background);
//            }
//            else if (background instanceof ColorDrawable) {
//                Random rnd = new Random();
////                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                int color = Color.rgb(229, 115, 115);
//                ((ColorDrawable)background).setColor(color);
//                parent.setBackground(background);
//            }
//        }
//        if(item.getName().contains("A-95")){
//            name.setText("A-95");
//            price.setText(item.getPrice() + " lei");
//
//            Drawable background = parent.getBackground();
//
//            if (background instanceof ShapeDrawable) {
//                Random rnd = new Random();
//                int color = Color.rgb(0,204,92);
//                ((ShapeDrawable)background).getPaint().setColor(color);
//                parent.setBackground(background);
//            }
//            else if (background instanceof GradientDrawable) {
//                Random rnd = new Random();
//                int color = Color.rgb(0,204,92);
//                ((GradientDrawable)background).setColor(color);
//                parent.setBackground(background);
//            }
//            else if (background instanceof ColorDrawable) {
//                Random rnd = new Random();
////                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                int color =  Color.rgb(0, 197, 105);
//                ((ColorDrawable)background).setColor(color);
//                parent.setBackground(background);
//            }
//        }

        return root;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return mData.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
       return true;
    }


}
