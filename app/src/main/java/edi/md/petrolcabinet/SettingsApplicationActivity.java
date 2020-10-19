package edi.md.petrolcabinet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SettingsApplicationActivity extends LocalizationActivity {
    TextView selectedLanguage, textItemAboutApp, textItemAboutAppInfo , selectedTheme;
    ImageButton btnBack;
    ConstraintLayout layoutLang, layoutWriteUs, layoutAbout, layoutTheme;
    Context context;

    LocalizationActivity localizationActivity = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_application);
        btnBack = findViewById(R.id.image_back_from_settings);
        layoutLang = findViewById(R.id.layout_language);
        layoutWriteUs = findViewById(R.id.layout_feeback);
        layoutAbout = findViewById(R.id.layout_about);
        layoutTheme = findViewById(R.id.layout_theme);
        selectedTheme = findViewById(R.id.text_selected_theme);
        selectedLanguage = findViewById(R.id.text_selected_langauge);
        textItemAboutApp = findViewById(R.id.text_item_about_app);
        textItemAboutAppInfo = findViewById(R.id.text_item_about_app_info);

        context = this;

        String[] languageList = {"English","Русский","Română"};
        String[] themeList = {getString(R.string.light_theme_selected),getString(R.string.dark_theme_selected),getString(R.string.system_theme_selected)};
        String lang = localizationActivity.getCurrentLanguage().getLanguage();

        if(lang.equals("ru"))
            selectedLanguage.setText("Выбран язык: Русский");
        else if(lang.equals("ro"))
            selectedLanguage.setText("Limba selectată: Română");
        else if(lang.equals("en"))
            selectedLanguage.setText("Selected language: English");

        int theme = getSharedPreferences("Theme", MODE_PRIVATE).getInt("theme_mode",2);
        switch (theme){
            case 0:{
                selectedTheme.setText(getString(R.string.light_theme_selected));
            }break;
            case 1:{
                selectedTheme.setText(getString(R.string.dark_theme_selected));
            }break;
            case 2:{
                selectedTheme.setText(getString(R.string.system_theme_selected));
            }break;
        }

        textItemAboutApp.setText(getString(R.string.despre_program_item_settings) + getString(R.string.app_name));
        textItemAboutAppInfo.setText(getString(R.string.informatii_cabinet_petrol_settings) + getString(R.string.app_name));

        layoutLang.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle(getString(R.string.dialog_title_select_your_lang))
                    .setItems(languageList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i){
                                case 0:{
                                    selectedLanguage.setText("Selected language: English");
                                    dialogInterface.dismiss();
//                                    LocaleHelper.setLocale(context,"en");
                                    localizationActivity.setLanguage("en");
                                }break;
                                case 1:{
                                    selectedLanguage.setText("Выбран язык: Русский");
                                    dialogInterface.dismiss();
//                                    LocaleHelper.setLocale(context,"ru");
                                    localizationActivity.setLanguage("ru");
                                }break;
                                case 2:{
                                    selectedLanguage.setText("Limba selectată: Română");
                                    dialogInterface.dismiss();
                                    localizationActivity.setLanguage("ro");
                                }break;
                            }
                        }
                    })
                    .setPositiveButton(getString(R.string.renunt_btn), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        });
        layoutTheme.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle(getString(R.string.msg_dialog_selected_theme))
                    .setItems(themeList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i){
                                case 0:{
                                    selectedTheme.setText(getString(R.string.light_theme_selected));
                                    dialogInterface.dismiss();
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                    getSharedPreferences("Theme", MODE_PRIVATE).edit().putInt("theme_mode",0).apply();
                                }break;
                                case 1:{
                                    selectedTheme.setText(getString(R.string.dark_theme_selected));
                                    dialogInterface.dismiss();
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                    getSharedPreferences("Theme", MODE_PRIVATE).edit().putInt("theme_mode",1).apply();
                                }break;
                                case 2:{
                                    selectedTheme.setText(getString(R.string.system_theme_selected));
                                    dialogInterface.dismiss();
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                                    getSharedPreferences("Theme", MODE_PRIVATE).edit().putInt("theme_mode",2).apply();
                                }break;
                            }
                            restartActivity();
                        }
                    })
                    .setPositiveButton(getString(R.string.renunt_btn), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        });

        layoutWriteUs.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL,  new String[]{"info@edi.md"});
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_mail_send_feedback));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, getString(R.string.send_feedback_msg)));
            }
        });

        layoutAbout.setOnClickListener(view -> {
            startActivity(new Intent(context, AboutAppActivity.class));
        });

        btnBack.setOnClickListener(view -> {
            finish();
        });
    }
    public void restartActivity() {
        Intent i = getIntent();
        this.overridePendingTransition(0, 0);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.finish();
        //restart the activity without animation
        this.overridePendingTransition(0, 0);
        this.startActivity(i);
    }
}