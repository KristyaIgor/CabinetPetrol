package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import edi.md.mydesign.adapters.AllTransactionListAdapter;

public class HistoryActivity extends AppCompatActivity {
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnBack = findViewById(R.id.image_back_transaction);
        RecyclerView recyclerViewTransaction = findViewById(R.id.recyclerView_history_transaction);

        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewTransaction.setLayoutManager(layoutManagerV);

        AllTransactionListAdapter adapter = new AllTransactionListAdapter(this, BaseApp.getAppInstance().getTransactions());
        recyclerViewTransaction.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}