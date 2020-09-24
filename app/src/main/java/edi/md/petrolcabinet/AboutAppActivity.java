package edi.md.petrolcabinet;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AboutAppActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView textVersion;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getWindow().getDecorView();
        Window window = getWindow();
        view.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        view.setFitsSystemWindows(true);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        setContentView(R.layout.activity_about_app);

        btnBack = findViewById(R.id.image_back_from_about);
        textVersion = findViewById(R.id.text_version_cabinet);

        context = this;

        String version ="1.0.0";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        textVersion.setText("ver " + version);


        btnBack.setOnClickListener(view1 -> {
            finish();
        });
    }
}