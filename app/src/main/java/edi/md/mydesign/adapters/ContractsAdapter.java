package edi.md.mydesign.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import edi.md.mydesign.R;
import edi.md.mydesign.models.ContractItem;

/**
 * Created by Igor on 30.06.2020
 */

public class ContractsAdapter extends ArrayAdapter<ContractItem> {
    Context context;
    List<ContractItem> mList;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtNr;
    }

    public ContractsAdapter(@NonNull Context context, int resource, @NonNull List<ContractItem> objects) {
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

        ContractItem dataModel = getItem(position);

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_list_contract, viewGroup, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.text_clientname_contract);
            viewHolder.txtNr = (TextView) convertView.findViewById(R.id.nr_item_contract);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtNr.setText(dataModel.getNr());

        // Return the completed view to render on screen
        return convertView;
    }
}
