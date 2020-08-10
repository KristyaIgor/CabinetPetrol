package edi.md.mydesign.remote;
import edi.md.mydesign.remote.authenticate.AuthenticateUserBody;
import edi.md.mydesign.remote.client.GetClientInfoResponse;
import edi.md.mydesign.remote.contract.GetContractInfoResponse;
import edi.md.mydesign.remote.petrolStation.GetPetrolStationResult;
import edi.md.mydesign.remote.prices.GetPriceResult;
import edi.md.mydesign.remote.response.SIDResponse;
import edi.md.mydesign.remote.transaction.GetTransactionList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Igor on 31.01.2020
 */

public interface CommandServices {
    @GET("/PetrolCabinetWebService/json/GetPrice")
    Call<GetPriceResult> getPrices ();

    @POST("/PetrolCabinetWebService/json/AuthenticateUser")
    Call<SIDResponse> authenticateUser (@Body AuthenticateUserBody user);

    @GET("/PetrolCabinetWebService/json/GetClientInfo")
    Call<GetClientInfoResponse> getClientInfo (@Query("SID") String sid);

    @GET("/PetrolCabinetWebService/json/GetContractInfo")
    Call<GetContractInfoResponse> getContractInfo (@Query("SID") String sid, @Query("ContractID") String contractId);

    @GET("/PetrolCabinetWebService/json/GetPetrolStation")
    Call<GetPetrolStationResult> getPetrolStation ();

    @GET("/PetrolCabinetWebService/json/GetTransactionList")
    Call<GetTransactionList> getTransactionList (@Query("SID") String sid);
}
