package edi.md.petrolcabinet.remoteSettings;

import edi.md.petrolcabinet.remote.authenticate.AuthenticateCard;
import edi.md.petrolcabinet.remote.authenticate.AuthenticateUserBody;
import edi.md.petrolcabinet.remote.cardDetailInfo.GetCardDetailInfoResponse;
import edi.md.petrolcabinet.remote.cardInfo.GetCardInfo;
import edi.md.petrolcabinet.remote.changePassword.ChangePasswordBody;
import edi.md.petrolcabinet.remote.client.GetClientInfoResponse;
import edi.md.petrolcabinet.remote.contract.GetContractInfoResponse;
import edi.md.petrolcabinet.remote.notification.GetNotificationSettings;
import edi.md.petrolcabinet.remote.notification.UpdateNotificationSettingsBody;
import edi.md.petrolcabinet.remote.petrolStation.GetPetrolStationResult;
import edi.md.petrolcabinet.remote.press.PressResponse;
import edi.md.petrolcabinet.remote.prices.GetPriceResult;
import edi.md.petrolcabinet.remote.registerUser.RegisterUserBody;
import edi.md.petrolcabinet.remote.registerUser.RegisterUserResponse;
import edi.md.petrolcabinet.remote.resetPassword.ResetPasswordResponse;
import edi.md.petrolcabinet.remote.response.SIDResponse;
import edi.md.petrolcabinet.remote.response.SIDResponseCard;
import edi.md.petrolcabinet.remote.response.SimpleResponse;
import edi.md.petrolcabinet.remote.transaction.GetTransactionList;
import edi.md.petrolcabinet.remote.updateLimitsCard.UpdateLimitsCardBody;
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

    @GET("/{serviceName}/json/GetPetrolStation")
    Call<GetPetrolStationResult> getPetrolStation (@Path("serviceName") String serviceName);

    @GET("{serviceName}/json/GetTransactionList")
    Call<GetTransactionList> getTransactionList (@Path("serviceName") String serviceName, @Query("SID") String sid);

    @POST("{serviceName}/json/UpdateNotificationSettings")
    Call<SimpleResponse> updateNotificationSettings (@Path("serviceName") String serviceName, @Body UpdateNotificationSettingsBody body);

    @GET("{serviceName}/json/GetNotificationSettings")
    Call<GetNotificationSettings> getNotificationSettings (@Path("serviceName") String serviceName, @Query("SID") String sid, @Query("token") String token);

    @POST("{serviceName}/json/UpdateLimitsCard")
    Call<SimpleResponse> updateLimitsCard (@Path("serviceName") String serviceName, @Query("SID") String sid, @Body UpdateLimitsCardBody body);

    @GET("{serviceName}/json/GetPress")
    Call<PressResponse> getPress (@Path("serviceName") String serviceName, @Query("ID") int id, @Query("Language") int identifier);

    @POST("{serviceName}/json/RegisterUser")
    Call<RegisterUserResponse> registerUser (@Path("serviceName") String serviceName, @Body RegisterUserBody body);

    @GET("{serviceName}/json/ActivateUser")
    Call<SimpleResponse> activateUser (@Path("serviceName") String serviceName, @Query("IDUSER") String IDUSER, @Query("PIN") String PIN);

    @GET("{serviceName}/json/ResetPassword")
    Call<ResetPasswordResponse> resetPassword (@Path("serviceName") String serviceName, @Query("Email") String email, @Query("Identifier") String identifier, @Query("ClientType") int typeClient);

    @POST("{serviceName}/json/ChangePassword")
    Call<SimpleResponse> changePassword (@Path("serviceName") String serviceName, @Body ChangePasswordBody body);
}
