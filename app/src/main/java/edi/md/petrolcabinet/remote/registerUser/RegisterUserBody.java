package edi.md.petrolcabinet.remote.registerUser;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor on 31.08.2020
 */

public class RegisterUserBody {
    @SerializedName("CardCod")
    @Expose
    private String cardCod;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("FName")
    @Expose
    private String fName;
    @SerializedName("IDNO")
    @Expose
    private String iDNO;
    @SerializedName("LName")
    @Expose
    private String lName;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("Phone")
    @Expose
    private String phone;

    public String getCardCod() {
        return cardCod;
    }

    public void setCardCod(String cardCod) {
        this.cardCod = cardCod;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getIDNO() {
        return iDNO;
    }

    public void setIDNO(String iDNO) {
        this.iDNO = iDNO;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
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
}
