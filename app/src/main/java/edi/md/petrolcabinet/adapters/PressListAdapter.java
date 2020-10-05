package edi.md.petrolcabinet.adapters;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.realm.objects.PressObjects;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


/**
 * Created by Igor on 21.09.2020
 */

public class PressListAdapter extends RealmRecyclerViewAdapter<PressObjects, PressListAdapter.MyViewHolder> {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    TimeZone timeZone = TimeZone.getTimeZone("Europe/Chisinau");

    public PressListAdapter(@Nullable OrderedRealmCollection<PressObjects> data, boolean autoUpdate) {
        super(data, autoUpdate);

        simpleDateFormat.setTimeZone(timeZone);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_news, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PressObjects item = getItem(position);

        if(item != null){
            if(item.getCompanyName() != null && item.getCompanyLogo() != null ){
                holder.companyName.setText(item.getCompanyName());

                byte[] decodedString = Base64.decode(item.getCompanyLogo(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                holder.companyLogo.setImageBitmap(decodedByte);
            }
            holder.pressTitle.setText(item.getHeader());
            String content = item.getContent();
            holder.contentView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

            holder.pressDate.setText(simpleDateFormat.format(item.getDateTime()));

            if(item.getImage() != null && item.getImage().length > 0){


                Bitmap bitmap = decodeSampledBitmapFromResource(item.getImage(), 300, 250);
                if (bitmap != null){
                    Log.d("TAG", String.format("Required size = %s, bitmap size = %sx%s, byteCount = %s", 300, bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
                    holder.pressImage.setImageBitmap(bitmap);
                }


//                Bitmap bmp = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
//                if(bmp != null){
//                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                    bmp.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
//                    byte[] imageBytes = outputStream.toByteArray();
//
//                    Bitmap bmpShow = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                    holder.pressImage.setImageBitmap(bmpShow);//Bitmap.createScaledBitmap(bmp, 350, 350, false)
//                }
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, pressDate, pressTitle;
        ImageView companyLogo, pressImage;
        WebView contentView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.text_press_companyName);
            companyLogo  = itemView.findViewById(R.id.image_logo_press_company);
            pressDate = itemView.findViewById(R.id.text_date_press);
            pressTitle = itemView.findViewById(R.id.text_title_press);
            contentView = itemView.findViewById(R.id.web_view_press_item);
            pressImage = itemView.findViewById(R.id.image_press);
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] path, int reqWidth, int reqHeight) {

        // Читаем с inJustDecodeBounds=true для определения размеров
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(path,0,path.length,options);

        // Вычисляем inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Читаем с использованием inSampleSize коэффициента
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(path,0,path.length,options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Реальные размеры изображения
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Вычисляем наибольший inSampleSize, который будет кратным двум
            // и оставит полученные размеры больше, чем требуемые
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
