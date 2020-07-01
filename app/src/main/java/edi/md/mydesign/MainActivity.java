package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout  layoutContracts, layoutQR, layoutNews;
    TextView title;

    static FragmentManager fgManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContracts = findViewById(R.id.layout_contracts);
        layoutQR = findViewById(R.id.layout_qr);
        layoutNews = findViewById(R.id.layout_news);
        title = findViewById(R.id.title_app);

        fgManager = getSupportFragmentManager();
        context = this;

        //create branch 2

        List<View> list = new ArrayList<>();
        list.add(layoutContracts);
        list.add(layoutQR);
        list.add(layoutNews);

        ListenerView listener = new ListenerView(MainActivity.this, list,title);

        layoutContracts.setOnClickListener(listener);
        ListenerView.setActiveMainLayout(layoutContracts);
        layoutQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PackageManager pm = getPackageManager();
//get a list of installed apps.
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
                    Log.d("TAG", "Installed package :" + packageQR.packageName);
                    Log.d("TAG", "Source dir : " + packageQR.sourceDir);

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
        layoutNews.setOnClickListener(listener);
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
}