package edi.md.petrolcabinet.remote.resetPassword;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor on 31.08.2020
 */

public class ConfirmResetPasswordBody {
    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("USERID")
    @Expose
    private String uSERID;

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUSERID() {
        return uSERID;
    }

    public void setUSERID(String uSERID) {
        this.uSERID = uSERID;
    }
}
