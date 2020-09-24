package edi.md.petrolcabinet.adapters;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edi.md.petrolcabinet.R;

/**
 * Created by Igor on 17.09.2020
 */

public class NewsViewHolder extends RecyclerView.ViewHolder {

    TextView txtName;
    ImageView img;
    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);

        txtName = (TextView) itemView.findViewById(R.id.text_clientname_contract);
        img = (ImageView) itemView.findViewById(R.id.image_contract_item);
    }
}
