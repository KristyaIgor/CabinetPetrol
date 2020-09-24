package edi.md.petrolcabinet.remote;

/**
 * Created by Igor on 25.11.2019
 */

public class ApiUtils {
    private static final String BaseURL_ConnectionBroker = "https://dev.edi.md/";

    public static CommandServices getCommandServices(String baseUrl){
        return RetrofitClient.getRetrofitClient(baseUrl).create(CommandServices.class);
    }
}