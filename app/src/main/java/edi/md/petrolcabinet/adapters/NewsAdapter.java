package edi.md.petrolcabinet.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.remote.press.PressList;

/**
 * Created by Igor on 30.06.2020
 */

public class NewsAdapter extends ArrayAdapter<PressList> {
    Context context;
    List<PressList> mList;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ImageView img;
    }

    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<PressList> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mList = objects;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder; // view lookup cache stored in tag

        PressList dataModel = getItem(position);

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_list_news, viewGroup, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.text_clientname_contract);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.image_contract_item);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        if (dataModel.getHeader() == null)
//            viewHolder.txtName.setText("Header");
//        else
//            viewHolder.txtName.setText(dataModel.getHeader());
//
//        Bitmap bmp = BitmapFactory.decodeByteArray(dataModel.getPicture(), 0, dataModel.getPicture().length);
//
//        viewHolder.img.setImageBitmap(Bitmap.createScaledBitmap(bmp, 350, 350, false));

        return convertView;
    }
}
