package edi.md.petrolcabinet;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends LocalizationActivity {
    LocalizationActivity localizationActivity = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int theme = getSharedPreferences("Theme", MODE_PRIVATE).getInt("theme_mode",2);
        switch (theme){
            case 0:{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }break;
            case 1:{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }break;
            case 2:{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar var10000 = this.getSupportActionBar();
        if (var10000 != null) {
            var10000.hide();
        }
        new Timer().schedule(new TimerTask() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivityV1.class));
                finish();
            }
        }, 1000);
    }
    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }
}