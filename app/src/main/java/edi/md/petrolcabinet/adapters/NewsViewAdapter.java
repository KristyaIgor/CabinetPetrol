package edi.md.petrolcabinet.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.remote.press.PressList;

/**
 * Created by Igor on 17.09.2020
 */

public class NewsViewAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    Context context;
    List<PressList> pressList;

    public NewsViewAdapter(Context context, List<PressList> pressList) {
        this.context = context;
        this.pressList = pressList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_news,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        PressList dataModel = pressList.get(position);

        if (dataModel.getHeader() == null)
            holder.txtName.setText("Header");
        else
            holder.txtName.setText(dataModel.getHeader());

        Bitmap bmp = BitmapFactory.decodeByteArray(dataModel.getPicture(), 0, dataModel.getPicture().length);

        holder.img.setImageBitmap(Bitmap.createScaledBitmap(bmp, 350, 350, false));

    }

    @Override
    public int getItemCount() {
        return pressList.size();
    }
}
