package edi.md.petrolcabinet.remote.changePassword;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor on 15.09.2020
 */

public class ChangePasswordBody {
    @SerializedName("NewPassword")
    @Expose
    private String newPassword;
    @SerializedName("OldPassword")
    @Expose
    private String oldPassword;
    @SerializedName("SID")
    @Expose
    private String sID;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getSID() {
        return sID;
    }

    public void setSID(String sID) {
        this.sID = sID;
    }
}
