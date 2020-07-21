package edi.md.mydesign.remote;

/**
 * Created by Igor on 25.11.2019
 */

public class ApiUtils {
    private static final String BaseURL_ConnectionBroker = "http://217.12.125.222:3333/";

    public static CommandServices getCommandServices(String url){
        return RetrofitClient.getRetrofitClient(url).create(CommandServices.class);
    }
}