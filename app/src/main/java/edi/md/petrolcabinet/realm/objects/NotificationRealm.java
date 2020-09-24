package edi.md.petrolcabinet.realm.objects;


import io.realm.RealmObject;

/**
 * Created by Igor on 15.09.2020
 */

public class NotificationRealm extends RealmObject {
    private String companyId;
    private String clientId;
    private int category;
    private String title;
    private String body;
    private String largeIcon;
    private String bigPicture;
    private String bigLargeIcon;
    private long createDate;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String getBigPicture() {
        return bigPicture;
    }

    public void setBigPicture(String bigPicture) {
        this.bigPicture = bigPicture;
    }

    public String getBigLargeIcon() {
        return bigLargeIcon;
    }

    public void setBigLargeIcon(String bigLargeIcon) {
        this.bigLargeIcon = bigLargeIcon;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
