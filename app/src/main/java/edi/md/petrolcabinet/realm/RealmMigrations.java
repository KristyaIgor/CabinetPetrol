package edi.md.petrolcabinet.realm;
import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by Igor on 20.12.2019
 */

public class RealmMigrations implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();
        if(oldVersion == 1){
            schema.get("Accounts")
                    .addField("id",String.class);
            oldVersion++;
        }
        if(oldVersion == 2){
            schema.create("NotificationRealm");
            schema.get("NotificationRealm")
                    .addField("companyId",String.class)
                    .addField("clientId",String.class)
                    .addField("category",int.class)
                    .addField("title",String.class)
                    .addField("body",String.class)
                    .addField("largeIcon",String.class)
                    .addField("bigPicture",String.class)
                    .addField("bigLargeIcon",String.class)
                    .addField("createDate",long.class);

            oldVersion++;
        }
        if(oldVersion == 3){
            schema.create("PressObjects");
            schema.get("PressObjects")
                    .addRealmObjectField("company",schema.get("Company"))
                    .addField("header",String.class)
                    .addField("content",String.class)
                    .addField("image",byte[].class)
                    .addField("dateTime",long.class)
                    .addField("type",int.class)
                    .addField("id",int.class);

            schema.get("Company").addField("idPress",int.class);

            oldVersion++;
        }
        if(oldVersion == 4){
            schema.get("PressObjects")
                    .addField("companyId",String.class)
                    .addField("companyName",String.class)
                    .addField("companyLogo",String.class)
                    .removeField("company");
            oldVersion++;
        }
        if(oldVersion == 5){
            schema.get("ClientRealm")
                    .removeField("fillingAccount")
                    .removeField("newDiscount")
                    .removeField("news")
                    .removeField("payAccount");
            schema.rename("ClientRealm", "Accounts");


            oldVersion++;
        }
        if(oldVersion == 6){
            schema.get("Accounts").removeField("cardId");
            oldVersion++;
        }
    }
}
