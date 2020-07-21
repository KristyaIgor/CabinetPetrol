package edi.md.mydesign.remote.petrolStation;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Igor on 06.07.2020
 */

public class GetPetrolStationResult {

    @SerializedName("ErrorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("PetrolStations")
    @Expose
    private List<PetrolStation> petrolStations = null;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<PetrolStation> getPetrolStations() {
        return petrolStations;
    }

    public void setPetrolStations(List<PetrolStation> petrolStations) {
        this.petrolStations = petrolStations;
    }
}
