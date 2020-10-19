package edi.md.petrolcabinet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import edi.md.petrolcabinet.adapters.NotificationsAdapter;
import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.realm.objects.NotificationRealm;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class NotificationListActivity extends AppCompatActivity {

    Realm mRealm;
    Accounts clientRealm;
    ListView listNotification;
    ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification_list);
        listNotification = findViewById(R.id.list_notification);
        buttonBack = findViewById(R.id.image_back_notification_list);

        mRealm = Realm.getDefaultInstance();
        clientRealm = BaseApp.getAppInstance().getClientClicked();

        Intent intent = getIntent();
        boolean fromService = intent.getBooleanExtra("fromService",false);
        String id = "";
        if(fromService)
            id = intent.getStringExtra("id");
        else
            if(clientRealm != null)
                id = clientRealm.getId();

        RealmResults<NotificationRealm> results = mRealm.where(NotificationRealm.class).equalTo("clientId", id).sort("createDate", Sort.DESCENDING).findAll();
        if(!results.isEmpty()){
            NotificationsAdapter adapter = new NotificationsAdapter(this,results);
            listNotification.setAdapter(adapter);
        }

        buttonBack.setOnClickListener(view -> {
            finish();
        });

    }
}