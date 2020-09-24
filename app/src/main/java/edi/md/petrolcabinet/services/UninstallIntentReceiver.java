package edi.md.petrolcabinet.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Igor on 21.08.2020
 */

public class UninstallIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String[] packageNames = intent.getStringArrayExtra("android.intent.extra.PACKAGES");

        String action = intent.getAction();
        Log.d("TAG", "onReceive: action uninstall application" + action);

        if(action.equals("ACTION_PACKAGE_REMOVED")){

            Log.d("TAG", "onReceive: action uninstall application");
        }

        if(packageNames!=null){
            for(String packageName: packageNames){
                if(packageName!=null && packageName.equals("edi.md.petrolcabinet")){
                    Log.d("TAG", "onReceive: uninstall application");
                }
            }
        }
    }
}
