package edi.md.petrolcabinet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutAppActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView textVersion, textPrivacy, textInfo;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        View view = getWindow().getDecorView();
//        Window window = getWindow();
//        view.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        view.setFitsSystemWindows(true);
//        window.setStatusBarColor(ContextCompat.getColor(this,R.color.itemBgDark));

        setContentView(R.layout.activity_about_app);

        btnBack = findViewById(R.id.image_back_from_about);
        textVersion = findViewById(R.id.text_version_cabinet);
        textPrivacy = findViewById(R.id.privacy_policy_app);
        textInfo = findViewById(R.id.text_info_app);

        context = this;

        String version ="1.0.0";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        textVersion.setText(getString(R.string.version_text) + version);
        textInfo.setText(getString(R.string.app_name) + getString(R.string.cabinet_petrol_aplicatie_info));

        btnBack.setOnClickListener(view1 -> {
            finish();
        });

        textPrivacy.setOnClickListener(view1 -> {
            String url = "https://edi.md/privacy/privacy_policy.html";

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }
}