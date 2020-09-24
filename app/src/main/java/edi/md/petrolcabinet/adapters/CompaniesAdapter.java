package edi.md.petrolcabinet.adapters;


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

import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.realm.objects.Company;
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
        ImageView isActive = root.findViewById(R.id.imageView_state_company);


        if(item.isExistContracts()){
            state.setImageResource(R.drawable.ic_state_contract_active);
        }
        else{
            state.setImageResource(R.drawable.ic_radio_button_company_not_connected);
        }

        name.setText(item.getName());
        info.setText(viewGroup.getContext().getString(R.string.accounts_added_in_company) + item.getNumberContracts());

       try {
           byte[] decodedString = Base64.decode(item.getLogo(), Base64.DEFAULT);
           Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
           image.setImageBitmap(decodedByte);
       }catch (Exception e0){
           name.setText(item.getName());
       }

       if(item.isActive()){
           isActive.setVisibility(View.GONE);
       }
       else{
           isActive.setVisibility(View.VISIBLE);
       }

        return root;
    }
}
