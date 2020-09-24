package edi.md.petrolcabinet.adapters;


import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.remote.prices.Price;

/**
 * Created by Igor on 06.07.2020
 */

public class PricesAdapter implements ListAdapter {

    List<Price> mData;
    Context context;

    public PricesAdapter(Context context, List<Price> list) {
        this.mData = list;
        this.context = context;
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
        View root = LayoutInflater.from(context).inflate(R.layout.item_list_product,null);

        Price item = mData.get(position);
        TextView name = root.findViewById(R.id.text_produs_name);
        TextView price = root.findViewById(R.id.text_price_discount_produs);

        name.setText(item.getName());
        price.setText(item.getPrice() + context.getString(R.string.price_lei));

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
