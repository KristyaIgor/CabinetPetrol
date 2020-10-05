package edi.md.petrolcabinet.remoteSettings;


import android.content.Context;

import edi.md.petrolcabinet.BaseApp;
import edi.md.petrolcabinet.R;
/**
 * Created by Igor on 10.08.2020
 */

public class RemoteException {
    static Context context = BaseApp.getAppInstance().getBaseContext();

    public static String getServiceException(int errorCode) {

        switch (errorCode){
            case -1:
                return context.getString(R.string.remote_exception__1);
            case 1:
                return context.getString(R.string.remote_exception_1);
            case 2:
                return context.getString(R.string.remote_exception_2); 
            case 3:
                return context.getString(R.string.remote_exception_3);  
            case 4:
                return context.getString(R.string.remote_exception_4); 
            case 5:
                return context.getString(R.string.remote_exception_5); 
            case 6:
                return context.getString(R.string.remote_exception_6); 
            case 7:
                return context.getString(R.string.remote_exception_7);
            case 8:
                return context.getString(R.string.remote_exception_8);  
            case 9:
                return context.getString(R.string.remote_exception_9);
            case 10:
                return context.getString(R.string.remote_exception_10);  
            case 11:
                return context.getString(R.string.remote_exception_11);
            case 12:
                return context.getString(R.string.remote_exception_12); 
            case 13:
                return context.getString(R.string.remote_exception_13); 
            case 14:
                return context.getString(R.string.remote_exception_14);  
            case 15:
                return context.getString(R.string.remote_exception_15); 
            case 16:
                return context.getString(R.string.remote_exception_16); 
            case 17:
                return context.getString(R.string.remote_exception_17); 
            case 18:
                return context.getString(R.string.remote_exception_18);
            case 19:
                return context.getString(R.string.remote_exception_19);  
            case 20:
                return context.getString(R.string.remote_exception_20);
            case 21:
                return context.getString(R.string.remote_exception_21); 
            case 22:
                return context.getString(R.string.remote_exception_22); 
            case 23:
                return context.getString(R.string.remote_exception_23);  
            case 24:
                return context.getString(R.string.remote_exception_24); 
            case 25:
                return context.getString(R.string.remote_exception_25); 
            case 26:
                return context.getString(R.string.remote_exception_26);
            case 27:
                return context.getString(R.string.remote_exception_27); 
            case 28:
                return context.getString(R.string.remote_exception_28); 
            case 29:
                return context.getString(R.string.remote_exception_29);  
            case 30:
                return context.getString(R.string.remote_exception_30); 
            case 31:
                return context.getString(R.string.remote_exception_31);
            case 32:
                return context.getString(R.string.remote_exception_32); 
            case 33:
                return context.getString(R.string.remote_exception_33); 
            case 34:
                return context.getString(R.string.remote_exception_34); 
            case 35:
                return context.getString(R.string.remote_exception_35); 
            case 36:
                return context.getString(R.string.remote_exception_36); 
            case 37:
                return context.getString(R.string.remote_exception_37); 
            case 38:
                return context.getString(R.string.remote_exception_38); 
            default:
                return context.getString(R.string.remote_exception_unknow); 
        }
    }
}
