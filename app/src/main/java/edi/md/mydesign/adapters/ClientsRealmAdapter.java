package edi.md.mydesign.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import edi.md.mydesign.R;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.client.Client;
import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.utils.BaseEnum;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Igor on 30.06.2020
 */

public class ClientsRealmAdapter extends RealmBaseAdapter<ClientRealm> implements ListAdapter {

    Context context;

    private static class ViewHolder {
        TextView txtName;
        TextView txtNr;
        ImageView icon;
    }

    public ClientsRealmAdapter(Context context, @Nullable OrderedRealmCollection<ClientRealm> data) {
        super(data);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ClientRealm dataModel = getItem(position);

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_list_contract, parent, false);
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
        viewHolder.txtNr.setText("Disponibil " + dataModel.getBalance());

        return convertView;
    }
}
