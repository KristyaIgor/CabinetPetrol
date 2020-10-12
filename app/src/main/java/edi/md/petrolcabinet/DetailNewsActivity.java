package edi.md.petrolcabinet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edi.md.petrolcabinet.realm.objects.PressObjects;
import io.realm.Realm;

public class DetailNewsActivity extends AppCompatActivity {

    TextView textNamePress;
    ImageButton btnBack;
    ImageView pressImage;
    Realm mRealm;
    WebView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_news);

        textNamePress = findViewById(R.id.title_news_name);
        btnBack = findViewById(R.id.image_back_to_news);
//        pressContent = findViewById(R.id.text_content_press_detail);
        pressImage = findViewById(R.id.image_press_detail);
        contentView = findViewById(R.id.web_view_press);

        mRealm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        String comId = intent.getStringExtra("companyId");
        int id = intent.getIntExtra("id",0);

        PressObjects press = mRealm.where(PressObjects.class).equalTo("id", id).and().equalTo("companyId",comId).findFirst();

        if (press != null) {
            textNamePress.setText(press.getHeader());

            String presCont = press.getContent();
            contentView.loadDataWithBaseURL(null, presCont, "text/html", "utf-8", null);

            if (press.getImage() != null && press.getImage().length > 0) {


                Bitmap bitmap = decodeSampledBitmapFromResource(press.getImage(), 300, 250);
                if (bitmap != null) {
//                    Log.d("TAG", String.format("Required size = %s, bitmap size = %sx%s, byteCount = %s", 300, bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
                    pressImage.setImageBitmap(bitmap);
                }

            }
        }

        btnBack.setOnClickListener(view1 -> {
            finish();
        });
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