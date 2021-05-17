package edi.md.petrolcabinet;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.BuildConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edi.md.petrolcabinet.fragments.FragmentCompanies;
import edi.md.petrolcabinet.fragments.FragmentNews;
import edi.md.petrolcabinet.realm.objects.Company;
import edi.md.petrolcabinet.utils.CompaniesHelper;
import edi.md.petrolcabinet.utils.RemoteCompanies;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivityV1 extends LocalizationActivity {
    ViewPager viewPager;

    ConstraintLayout layoutCompanies, layoutNews;
    TextView title ,titleNews, titleCompanies;
    ImageView imgNews, imgCompanies;

    Context context;

    ImageButton openQR, openAppSettings;
    Realm mRealm;
    RemoteCompanies companies;

//    ProgressDialog progressDialog;

    static Activity activity;
    boolean isFirstStart = false;

    public static Activity getActivity() {
        return  activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v_1);

        viewPager = findViewById(R.id.container_main);
        layoutCompanies = findViewById(R.id.layout_companies);
        layoutNews = findViewById(R.id.layout_news);
        title = findViewById(R.id.title_app);
        openQR = findViewById(R.id.btn_open_qr_code);
        openAppSettings = findViewById(R.id.btn_open_app_settings);
        titleNews = findViewById(R.id.textView_news);
        titleCompanies = findViewById(R.id.textView_contracts);
        imgNews = findViewById(R.id.imageView_news);
        imgCompanies = findViewById(R.id.imageView_contracts);

        context = this;
        activity = this;
//        progressDialog = new ProgressDialog(context);

        checkCompaniesList();

        mRealm = Realm.getDefaultInstance();
        createViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0 && positionOffsetPixels == 0){
                    setLayoutActive(0);
                }
                if(position == 1 && positionOffsetPixels == 0){
                    setLayoutActive(1);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        openQR.setOnClickListener(view -> {
            final PackageManager pm = getPackageManager();

            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            boolean findApp = false;
            ApplicationInfo packageQR = null;

            for (ApplicationInfo packageInfo : packages) {
                if(packageInfo.packageName.equals("com.edi.md.android.guid_gen")){
                    packageQR = packageInfo;
                    Log.d("TAG", "Installed package :" + packageInfo.packageName);
                    Log.d("TAG", "Source dir : " + packageInfo.sourceDir);
                    findApp = true;
                    break;
                }
            }

            if(findApp){
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.edi.md.android.guid_gen");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
            else{
                openPlayMarket("com.edi.md.android.guid_gen");
            }
        });

        openAppSettings.setOnClickListener(view -> {
            startActivity(new Intent(MainActivityV1.this, SettingsApplicationActivity.class));
        });

        layoutCompanies.setOnClickListener(view -> {
            viewPager.setCurrentItem(0);
        });
        layoutNews.setOnClickListener(view -> {
            viewPager.setCurrentItem(1);
        });

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }

            // Get new Instance ID token
            String token = task.getResult().getToken();
            getSharedPreferences("firebase",MODE_PRIVATE).edit().putString("token",token).apply();

            Log.d("TAG", token );
        });

        AskForPermissions();
    }

    private void setLayoutActive(int position) {
        if(position == 0){
            layoutNews.setSelected(false);
            titleNews.setTypeface(Typeface.DEFAULT);
            imgNews.setColorFilter(getColor(R.color.green));

            layoutCompanies.setSelected(true);
            titleCompanies.setTypeface(Typeface.DEFAULT_BOLD);
            imgCompanies.setColorFilter(Color.rgb(255, 255, 255));
            title.setText(getString(R.string.main_menu_companii));
        }
        else if (position == 1){
            layoutCompanies.setSelected(false);
            titleCompanies.setTypeface(Typeface.DEFAULT);
            imgCompanies.setColorFilter(getColor(R.color.green));

            layoutNews.setSelected(true);
            titleNews.setTypeface(Typeface.DEFAULT_BOLD);
            imgNews.setColorFilter(Color.rgb(255, 255, 255));
            title.setText(getString(R.string.main_menu_news));
        }
    }

    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new FragmentCompanies());
//        adapter.addFrag(new FragmentNews());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0 )
                return new FragmentCompanies();
            else
                return new FragmentNews();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void AskForPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        int READ_PHONEpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int read_storage_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int write_storage_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int wake_up_permision = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK);

        if (READ_PHONEpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (read_storage_permission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (write_storage_permission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (wake_up_permision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WAKE_LOCK);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    void openPlayMarket(String appPackageName){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void checkCompaniesList (){
//        progressDialog.setMessage(getString(R.string.progres_msg_loading_company));
//        progressDialog.setCancelable(false);
//        progressDialog.setIndeterminate(true);
//        progressDialog.show();

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);

        //defaultvalue

        Map<String,Object> defaultValue = new HashMap<>();
        defaultValue.put(CompaniesHelper.KEY_LIST_COMPANY,"");

        remoteConfig.setDefaultsAsync(defaultValue);

        remoteConfig.fetchAndActivate().addOnCompleteListener( new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.isSuccessful()){
                    Log.d("TAG", "remote config is fetched.");

                    FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

                    //changes convert to class
                    String changes = remoteConfig.getString("list_company");

                    Gson gson = new Gson();

                    companies = gson.fromJson(changes,RemoteCompanies.class);
                    checkCompanies(companies);
                }
                else{
//                    progressDialog.dismiss();
                }
            }
        });
    }

    private void checkCompanies(RemoteCompanies companies){
        List<Company> remoteCompany = companies.getCompanies();

        RealmResults<Company> companyRealmList = mRealm.where(Company.class).findAll();

        int dbSize = companyRealmList.size();
        int remoteSize = remoteCompany.size();

        if(dbSize == remoteSize){
            for (Company company: remoteCompany){
                Company intoDbCompany = mRealm.where(Company.class).equalTo("id", company.getId()).findFirst();

                if(intoDbCompany != null){
                    mRealm.executeTransaction(realm -> {
                        intoDbCompany.setId(company.getId());
                        intoDbCompany.setName(company.getName());
                        intoDbCompany.setLogo(company.getLogo());
                        intoDbCompany.setIp(company.getIp());
                        intoDbCompany.setActive(company.isActive());
                        intoDbCompany.setServiceName(company.getServiceName());
                    });
                }
            }
        }

        if(remoteSize > dbSize){
            for (Company company: remoteCompany){
                Company intoDbCompany = mRealm.where(Company.class).equalTo("id", company.getId()).findFirst();
                if(intoDbCompany == null){
                    mRealm.executeTransaction(realm -> {
                        realm.insert(company);
                    });
                }
            }
        }
        if(dbSize > remoteSize){
            mRealm.executeTransaction(realm -> {
                companyRealmList.setBoolean("preparingToDelete",true);
            });


            for (Company company: companyRealmList){
                for (Company remoteComp: remoteCompany){
                    boolean intoRemoteCompany = false;

                    if(remoteComp.getId().equals(company.getId())){
                        intoRemoteCompany = true;
                    }

                    if(intoRemoteCompany){
                        mRealm.executeTransaction(realm -> {
                            company.setPreparingToDelete(false);
                        });
                    }
                }
            }
            mRealm.executeTransaction(realm -> {
                companyRealmList.where().equalTo("preparingToDelete",true).findAll().deleteAllFromRealm();
            });
        }
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

    private void restartActivity(){
        Intent i = getIntent();
        this.overridePendingTransition(0, 0);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.finish();
        //restart the activity without animation
        this.overridePendingTransition(0, 0);
        this.startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isFirstStart)
            FragmentCompanies.updateListCompanies();

        isFirstStart = true;
    }
}