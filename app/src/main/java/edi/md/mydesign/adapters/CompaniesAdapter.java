package edi.md.mydesign.adapters;


import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

import edi.md.mydesign.R;
import edi.md.mydesign.realm.objects.Company;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by Igor on 06.07.2020
 */

public class CompaniesAdapter  extends RealmBaseAdapter<Company> implements ListAdapter {

    public CompaniesAdapter(@Nullable OrderedRealmCollection<Company> data) {
        super(data);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_companies,null);

        Company item = getItem(position);

        TextView name = root.findViewById(R.id.text_company_name);
        TextView info = root.findViewById(R.id.text_company_info);
        ImageView image = root.findViewById(R.id.image_company);
        ImageView state = root.findViewById(R.id.image_company_state);

        if(item.isExistContracts()){
            state.setImageResource(R.drawable.ic_state_contract_active);
        }
        else{
            state.setImageResource(R.drawable.ic_radio_button_company_not_connected);
        }

        name.setText(item.getName());
        info.setText("Added user: " + item.getNumberContracts());

       try {
           byte[] decodedString = Base64.decode(item.getLogo(), Base64.DEFAULT);
           Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
           image.setImageBitmap(decodedByte);
       }catch (Exception e0){
           name.setText(item.getName());
       }

        return root;
    }
}
