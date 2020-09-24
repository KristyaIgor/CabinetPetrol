package edi.md.petrolcabinet.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.realm.objects.NotificationRealm;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

/**
 * Created by Igor on 30.06.2020
 */

public class NotificationsAdapter extends RealmBaseAdapter<NotificationRealm> implements ListAdapter {
    Context context;
    Realm mRealm;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    TimeZone timeZone = TimeZone.getTimeZone("Europe/Chisinau");


    private static class ViewHolder {
        TextView txtTitle;
        TextView txtBody;
        TextView txtDate, txtCategory;
        ImageView icon;
    }

    public NotificationsAdapter(Context context, @Nullable OrderedRealmCollection<NotificationRealm> data) {
        super(data);
        this.context = context;
        mRealm = Realm.getDefaultInstance();
        simpleDateFormat.setTimeZone(timeZone);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        NotificationRealm dataModel = getItem(position);

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_list_notification, parent, false);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.text_notification_title);
            viewHolder.txtBody = (TextView) convertView.findViewById(R.id.text_notification_body);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.text_date_notification);
            viewHolder.txtCategory = (TextView) convertView.findViewById(R.id.text_category);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.image_contract_item);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTitle.setText(dataModel.getTitle());
        viewHolder.txtBody.setText(dataModel.getBody());
        viewHolder.txtDate.setText(simpleDateFormat.format(dataModel.getCreateDate()));
        int dataType = dataModel.getCategory();
        Bitmap icon = null;
        String notificationChanel = "";
        switch (dataType){
            case 0:{
                notificationChanel = context.getString(R.string.type_notification_received_personal);
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notifications_icon);
            }break;
            case 1:{
                notificationChanel = context.getString(R.string.type_notification_received_news); //news
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notifications_icon);
            }break;
            case 2:{
                notificationChanel = context.getString(R.string.type_notification_received_promo); // promotion
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_promotion);
            }break;
            case 3:{
                notificationChanel = context.getString(R.string.type_notification_received_refueling); // pay by account
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_refueling);
            }break;
            case 4:{
                notificationChanel = context.getString(R.string.type_notification_received_filling); // suplinirea contului
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_supply);
            }break;
        }

        if(icon != null)
            viewHolder.icon.setImageBitmap(icon);

        viewHolder.txtCategory.setText(notificationChanel);


        return convertView;
    }
}
