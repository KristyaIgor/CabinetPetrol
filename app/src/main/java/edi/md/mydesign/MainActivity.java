package edi.md.mydesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edi.md.mydesign.realm.objects.Company;
import edi.md.mydesign.utils.CompaniesHelper;
import edi.md.mydesign.utils.MainListener;
import edi.md.mydesign.utils.RemoteCompanies;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout layoutCompanies, layoutNews;
    TextView title;

    static FragmentManager fgManager;
    Context context;

    ImageButton openQR;
    Realm mRealm;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutCompanies = findViewById(R.id.layout_companies);
        layoutNews = findViewById(R.id.layout_news);
        title = findViewById(R.id.title_app);
        openQR = findViewById(R.id.btn_open_qr_code);

        fgManager = getSupportFragmentManager();
        context = this;
        progressDialog = new ProgressDialog(context,R.style.ThemeOverlay_AppCompat_Dialog_Alert_TestDialogTheme);

        checkCompaniesList ();

        mRealm = Realm.getDefaultInstance();

        List<View> list = new ArrayList<>();
        list.add(layoutCompanies);
        list.add(layoutNews);

        MainListener listener = new MainListener(MainActivity.this, list,title);

        layoutCompanies.setOnClickListener(listener);

        openQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PackageManager pm = getPackageManager();

                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                boolean findApp = false;
                ApplicationInfo packageQR = null;

                for (ApplicationInfo packageInfo : packages) {
                    if(packageInfo.packageName.equals("com.example.guid_gen")){
                        packageQR = packageInfo;
                        Log.d("TAG", "Installed package :" + packageInfo.packageName);
                        Log.d("TAG", "Source dir : " + packageInfo.sourceDir);
                        findApp = true;
                        break;
                    }
                }

                if(findApp){
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.guid_gen");
                    if (launchIntent != null) {
                        startActivity(launchIntent);//null pointer check in case package name was not found
                    }
                }
                else{
                    openPlayMarket("com.example.guid_gen");
                }
            }
        });

        AskForPermissions();
        layoutNews.setOnClickListener(listener);
    }

    private void AskForPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        int READ_PHONEpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (READ_PHONEpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    public static void replaceFragment(Fragment fragment){
        fgManager.beginTransaction().replace(R.id.container_main,fragment).commit();
    }

    void openPlayMarket(String appPackageName){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void checkCompaniesList (){
        progressDialog.setMessage("load company list...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

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

                    RemoteCompanies companies = gson.fromJson(changes,RemoteCompanies.class);
                    checkCompanies(companies);
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
                            intoDbCompany.setAddress(company.getAddress());
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


//        BaseApp.getAppInstance().setCompanyList(remoteCompany);

        MainListener.setActiveMainLayout(layoutCompanies);
        progressDialog.dismiss();
    }
}