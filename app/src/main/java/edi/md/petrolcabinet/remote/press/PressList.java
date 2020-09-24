package edi.md.petrolcabinet.remote.press;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor on 01.09.2020
 */

public class PressList {

    @SerializedName("Content")
    @Expose
    private String content;
    @SerializedName("DateTime")
    @Expose
    private String dateTime;
    @SerializedName("Header")
    @Expose
    private String header;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Picture")
    @Expose
    private byte[] picture;
    @SerializedName("PressType")
    @Expose
    private Integer pressType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Integer getPressType() {
        return pressType;
    }

    public void setPressType(Integer pressType) {
        this.pressType = pressType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
