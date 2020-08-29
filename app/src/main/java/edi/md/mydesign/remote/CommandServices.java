package edi.md.mydesign.remote;
import edi.md.mydesign.remote.authenticate.AuthenticateCard;
import edi.md.mydesign.remote.authenticate.AuthenticateUserBody;
import edi.md.mydesign.remote.cardDetailInfo.GetCardDetailInfoResponse;
import edi.md.mydesign.remote.cardInfo.GetCardInfo;
import edi.md.mydesign.remote.client.GetClientInfoResponse;
import edi.md.mydesign.remote.contract.GetContractInfoResponse;
import edi.md.mydesign.remote.notification.GetNotificationSettings;
import edi.md.mydesign.remote.notification.UpdateNotificationSettingsBody;
import edi.md.mydesign.remote.petrolStation.GetPetrolStationResult;
import edi.md.mydesign.remote.prices.GetPriceResult;
import edi.md.mydesign.remote.response.SIDResponse;
import edi.md.mydesign.remote.response.SIDResponseCard;
import edi.md.mydesign.remote.response.SimpleResponse;
import edi.md.mydesign.remote.transaction.GetTransactionList;
import edi.md.mydesign.remote.updateLimitsCard.UpdateLimitsCardBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Igor on 31.01.2020
 */

public interface CommandServices {

    @GET("{serviceName}/json/GetPrice")
    Call<GetPriceResult> getPrices (@Path("serviceName") String serviceName);

    @POST("{serviceName}/json/AuthenticateUser")
    Call<SIDResponse> authenticateUser (@Path("serviceName") String serviceName, @Body AuthenticateUserBody user);

    @POST("{serviceName}/json/AuthenticateCard")
    Call<SIDResponseCard> authenticateCard (@Path("serviceName") String serviceName, @Body AuthenticateCard card);

    @GET("{serviceName}/json/GetCardDetailInfo")
    Call<GetCardDetailInfoResponse> getCardDetailInfo(@Path("serviceName") String serviceName, @Query("SID") String sid, @Query("CardID") String cardId,  @Query("Language") String language);

    @GET("{serviceName}/json/GetCardInfo")
    Call<GetCardInfo> getCardInfo (@Path("serviceName") String serviceName, @Query("SID") String sid, @Query("CardID") String cardId);

    @GET("{serviceName}/json/GetClientInfo")
    Call<GetClientInfoResponse> getClientInfo (@Path("serviceName") String serviceName, @Query("SID") String sid);

    @GET("{serviceName}/json/GetContractInfo")
    Call<GetContractInfoResponse> getContractInfo (@Path("serviceName") String serviceName, @Query("SID") String sid, @Query("ContractID") String contractId);

    @GET("{serviceName}/json/GetPetrolStation")
    Call<GetPetrolStationResult> getPetrolStation (@Path("serviceName") String serviceName);

    @GET("{serviceName}/json/GetTransactionList")
    Call<GetTransactionList> getTransactionList (@Path("serviceName") String serviceName, @Query("SID") String sid);

    @POST("{serviceName}/json/UpdateNotificationSettings")
    Call<SimpleResponse> updateNotificationSettings (@Path("serviceName") String serviceName, @Body UpdateNotificationSettingsBody body);

    @GET("{serviceName}/json/GetNotificationSettings")
    Call<GetNotificationSettings> getNotificationSettings (@Path("serviceName") String serviceName, @Query("SID") String sid, @Query("token") String token);

    @POST("{serviceName}/json/UpdateLimitsCard")
    Call<SimpleResponse> updateLimitsCard (@Path("serviceName") String serviceName, @Query("SID") String sid, @Body UpdateLimitsCardBody body);
}
