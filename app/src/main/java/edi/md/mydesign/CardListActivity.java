package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import edi.md.mydesign.adapters.CardsInContractClientAdapter;
import edi.md.mydesign.remote.contract.CardsList;
import edi.md.mydesign.remote.contract.Contract;

public class CardListActivity extends AppCompatActivity {

    Contract contract;
    ImageButton btnBack;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        btnBack = findViewById(R.id.btn_back_to_account);
        title = findViewById(R.id.title_contract_name);

        contract = BaseApp.getAppInstance().getClickedClientContract();

        List<CardsList> contractCardsList = contract.getCardsList();
        CardsInContractClientAdapter adapter1 = new CardsInContractClientAdapter(this, contractCardsList);

        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView_contracts);
        recyclerView2.setLayoutManager(layoutManagerV);
        recyclerView2.setAdapter(adapter1);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}