package edi.md.mydesign.utils;
import android.content.Context;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;

/**
 * Created by Igor on 12.05.2020
 */

public class CompaniesHelper {
    public static String KEY_LIST_COMPANY = "list_company";


    public interface OnCompaniesCheckListener{
        void onCompaniesCheckListener(RemoteCompanies companies);
    }

    public static Builder with(Context context){
        return new Builder(context);
    }

    private OnCompaniesCheckListener onCompaniesCheckListener;
    private Context context;

    public CompaniesHelper(Context context, OnCompaniesCheckListener onCompaniesCheckListener) {
        this.onCompaniesCheckListener = onCompaniesCheckListener;
        this.context = context;
    }

    public void check(){
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        //changes convert to class
        String changes = remoteConfig.getString(KEY_LIST_COMPANY);

        Gson gson = new Gson();

        RemoteCompanies companies = gson.fromJson(changes,RemoteCompanies.class);


        onCompaniesCheckListener.onCompaniesCheckListener(companies);

    }

    public static class Builder{

        private Context context;
        private OnCompaniesCheckListener onCompaniesCheckListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onCheckList (OnCompaniesCheckListener onCompaniesCheckListener){
            this.onCompaniesCheckListener = onCompaniesCheckListener;
            return this;
        }

        public CompaniesHelper build(){
            return new CompaniesHelper(context,onCompaniesCheckListener);
        }

        public CompaniesHelper check(){
            CompaniesHelper helper = build();
            helper.check();

            return helper;
        }
    }
}
