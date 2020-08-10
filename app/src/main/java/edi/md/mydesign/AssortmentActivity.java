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
import edi.md.mydesign.adapters.ProductListAdapter;
import edi.md.mydesign.remote.contract.CardsList;
import edi.md.mydesign.remote.contract.Contract;
import edi.md.mydesign.remote.contract.ProductsList;

public class AssortmentActivity extends AppCompatActivity {
    Contract contract;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);

        btnBack = findViewById(R.id.btn_back_to_account);

        contract = BaseApp.getAppInstance().getClickedClientContract();

        List<ProductsList> productsList = contract.getProductsList();
        ProductListAdapter adapter1 = new ProductListAdapter(this, productsList);

        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView_produse_reducere);
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