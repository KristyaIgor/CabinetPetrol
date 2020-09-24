package edi.md.petrolcabinet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.utils.LocaleHelper;
import io.realm.Realm;

public class SettingsApplicationActivity extends AppCompatActivity {
    TextView selectedLanguage;
    Accounts client;
    Realm mRealm;
    ImageButton btnBack;
    ConstraintLayout layoutLang, layoutWriteUs, layoutAbout;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_application);
        btnBack = findViewById(R.id.image_back_from_settings);
        layoutLang = findViewById(R.id.layout_language);
        layoutWriteUs = findViewById(R.id.layout_feeback);
        layoutAbout = findViewById(R.id.layout_about);
        selectedLanguage = findViewById(R.id.text_selected_langauge);

        context = this;

        String[] languageList = {"English","Русский","Română"};
        String lang = LocaleHelper.getLanguage(context);

        if(lang.equals("ru"))
            selectedLanguage.setText("Выбран язык: Русский");
        else if(lang.equals("ro"))
            selectedLanguage.setText("Limba selectată: Română");
        else if(lang.equals("en"))
            selectedLanguage.setText("Selected language: English");

        layoutLang.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                    .setTitle(getString(R.string.dialog_title_select_your_lang))
                    .setItems(languageList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i){
                                case 0:{
                                    selectedLanguage.setText("Selected language: English");
                                    dialogInterface.dismiss();
                                    LocaleHelper.setLocale(context,"en");
                                }break;
                                case 1:{
                                    selectedLanguage.setText("Выбран язык: Русский");
                                    dialogInterface.dismiss();
                                    LocaleHelper.setLocale(context,"ru");
                                }break;
                                case 2:{
                                    selectedLanguage.setText("Limba selectată: Română");
                                    dialogInterface.dismiss();
                                    LocaleHelper.setLocale(context,"ro");

                                }break;
                            }

                            Activity activity = MainActivity.getActivity();
                            Intent start = new Intent(context, SplashActivity.class);
                            activity.finish();
                            activity.startActivity(start);

                        }
                    })
                    .setCancelable(false)
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
}