package edi.md.petrolcabinet.remote.registerUser;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor on 31.08.2020
 */

public class RegisterUserResponse {
    @SerializedName("ErrorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("IDUSER")
    @Expose
    private String iDUSER;
    @SerializedName("PIN")
    @Expose
    private String pIN;

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

    public String getIDUSER() {
        return iDUSER;
    }

    public void setIDUSER(String iDUSER) {
        this.iDUSER = iDUSER;
    }

    public String getPIN() {
        return pIN;
    }

    public void setPIN(String pIN) {
        this.pIN = pIN;
    }
}
