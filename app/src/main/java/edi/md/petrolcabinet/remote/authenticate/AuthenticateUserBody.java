package edi.md.petrolcabinet.remote.authenticate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor on 07.07.2020
 */

public class AuthenticateUserBody {
    @SerializedName("AuthType")
    @Expose
    private Integer authType;
    @SerializedName("IDNO")
    @Expose
    private String iDNO;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("User")
    @Expose
    private String user;

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public String getIDNO() {
        return iDNO;
    }

    public void setIDNO(String iDNO) {
        this.iDNO = iDNO;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
