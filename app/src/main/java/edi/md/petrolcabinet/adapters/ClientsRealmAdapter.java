package edi.md.petrolcabinet.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.utils.BaseEnum;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

/**
 * Created by Igor on 30.06.2020
 */

public class ClientsRealmAdapter extends RealmBaseAdapter<Accounts> implements ListAdapter {
    Context context;
    Realm mRealm;

    private static class ViewHolder {
        TextView txtName;
        TextView txtNr;
        ImageView icon;
    }

    public ClientsRealmAdapter(Context context, @Nullable OrderedRealmCollection<Accounts> data) {
        super(data);
        this.context = context;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Accounts dataModel = getItem(position);

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_list_user, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.text_clientname_contract);
            viewHolder.txtNr = (TextView) convertView.findViewById(R.id.nr_item_contract);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.image_contract_item);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (dataModel.getTypeClient() == BaseEnum.PerCard){
            viewHolder.txtName.setText(dataModel.getCode() + " / " + dataModel.getName());
            viewHolder.icon.setImageResource(R.drawable.ic_credit_card);
        }else if (dataModel.getTypeClient() == BaseEnum.PersoanaFizica){
            viewHolder.txtName.setText(dataModel.getName());
            viewHolder.icon.setImageResource(R.drawable.persoana_fizica);
        }
        else{
            viewHolder.txtName.setText(dataModel.getName());
            viewHolder.icon.setImageResource(R.drawable.persoana_juridica);
        }
        viewHolder.txtNr.setText(context.getString(R.string.disponibil_client_in_list) + dataModel.getBalance());

//        viewHolder.btnSettings.setOnClickListener(view -> {
//            BaseApp.getAppInstance().setClientClicked(mRealm.copyFromRealm(dataModel));
//            Intent settings = new Intent(context, SettingsApplicationActivity.class);
//            context.startActivity(settings);
//        });

        return convertView;
    }
}
