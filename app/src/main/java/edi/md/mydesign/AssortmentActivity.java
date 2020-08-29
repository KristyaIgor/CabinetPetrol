package edi.md.mydesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.mydesign.adapters.CardItemProductListAdapter;
import edi.md.mydesign.adapters.CardsInContractClientAdapter;
import edi.md.mydesign.adapters.ProductListAdapter;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.authenticate.AuthenticateUserBody;
import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.remote.contract.CardsList;
import edi.md.mydesign.remote.contract.Contract;
import edi.md.mydesign.remote.contract.GetContractInfoResponse;
import edi.md.mydesign.remote.contract.ProductsList;
import edi.md.mydesign.remote.response.SIDResponse;
import edi.md.mydesign.utils.BaseEnum;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssortmentActivity extends AppCompatActivity {
    ImageButton btnBack;

    TextView title;
    ConstraintLayout filter;

    Spinner productSpiner;
    ProgressBar progressBar;

    String sid;
    String contractId;

    RecyclerView recyclerView2;
    List<ProductsList> productsList = new ArrayList<>();
    ProductListAdapter productListAdapter;
    List<ContractInClient> contracts = new ArrayList<>();

    CommandServices commandServices;
    Realm mRealm;
    ClientRealm client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        Window window = getWindow();
        decorView.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        decorView.setFitsSystemWindows(true);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        setContentView(R.layout.activity_assortment);

        btnBack = findViewById(R.id.btn_back_to_account);
        title = findViewById(R.id.title_product_name);
        filter = findViewById(R.id.csl_filtering);
        productSpiner = findViewById(R.id.spinner_contracts_in_products);
        progressBar = findViewById(R.id.progressBar_getProduct);

        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView2 = findViewById(R.id.recyclerView_produse_reducere);
        recyclerView2.setLayoutManager(layoutManagerV);

        commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        mRealm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        boolean accountCard = intent.getBooleanExtra("CardAccount", false);

        if(accountCard){
            filter.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

            if(BaseApp.getAppInstance().getCardAccount().getCardAssortments().size() == 0){
                title.setText("Reducere la produse");

                productsList = BaseApp.getAppInstance().getCardAccount().getProductsLists();

                productListAdapter = new ProductListAdapter(this, productsList);
                recyclerView2.setAdapter(productListAdapter);
            }
            else{
                title.setText("Informații produse");

                CardItemProductListAdapter adapter = new CardItemProductListAdapter(this, BaseApp.getAppInstance().getCardAccount().getCardAssortments());
                recyclerView2.setAdapter(adapter);
            }
            recyclerView2.setVisibility(View.VISIBLE);
        }
        else{
            title.setText("Reducere la produse");

            client = BaseApp.getAppInstance().getClientClicked();

            contracts = client.getContracts();
            List<String> contractName = new ArrayList<>();

            if(contracts.size() > 1){
                filter.setVisibility(View.VISIBLE);

                for(int i = 0; i < contracts.size(); i ++){
                    contractName.add(contracts.get(i).getCode());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,contractName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                productSpiner.setAdapter(adapter);
                productSpiner.setSelection(0);
            }
            else{
                filter.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                ContractInClient first = contracts.get(0);
                sid = client.getSid();
                contractId = first.getID();

                getContractInfo(sid,contractId);
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        productSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                recyclerView2.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                ContractInClient first = contracts.get(i);
                sid = client.getSid();
                contractId = first.getID();

                getContractInfo(sid,contractId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getContractInfo(String sid, String contractId) {
        Call<GetContractInfoResponse> call = commandServices.getContractInfo(BaseApp.getAppInstance().getCompanyClicked().getServiceName(),sid, contractId);
        enqueueCall(call);
    }

    private void enqueueCall(Call<GetContractInfoResponse> call) {
        call.enqueue(new Callback<GetContractInfoResponse>() {
            @Override
            public void onResponse(Call<GetContractInfoResponse> call, Response<GetContractInfoResponse> response) {
                GetContractInfoResponse contractInfoResponse = response.body();

                if (contractInfoResponse != null) {
                    if (contractInfoResponse.getErrorCode() == 0) {

                        Contract contract = contractInfoResponse.getContract();
                        productsList = contract.getProductsList();

                        productListAdapter = new ProductListAdapter(AssortmentActivity.this, productsList);
                        recyclerView2.setAdapter(productListAdapter);

                        recyclerView2.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                    else if(contractInfoResponse.getErrorCode() == 5){
                        reAuth(client);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetContractInfoResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void reAuth(ClientRealm client) {
        AuthenticateUserBody user = new AuthenticateUserBody();

        if (client.getTypeClient() == BaseEnum.PersoanaFizica) {
            user.setPassword(decrypt(client.getPassword(), BaseApp.getAppInstance().getHuyYou()));
            user.setPhone(decrypt(client.getPhone(), BaseApp.getAppInstance().getHuyYou()));
            user.setAuthType(0);
        } else if (client.getTypeClient() == BaseEnum.PersoanaJuridica) {
            user.setPassword(decrypt(client.getPassword(), BaseApp.getAppInstance().getHuyYou()));
            user.setUser(decrypt(client.getUserName(), BaseApp.getAppInstance().getHuyYou()));
            user.setIDNO(client.getIDNP());
            user.setAuthType(1);
        }

        Call<SIDResponse> call = commandServices.authenticateUser(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), user);

        call.enqueue(new Callback<SIDResponse>() {
            @Override
            public void onResponse(Call<SIDResponse> call, Response<SIDResponse> response) {
                SIDResponse sidResponse = response.body();
                if (sidResponse != null && sidResponse.getErrorCode() == 0) {
                    sid = sidResponse.getSID();
                    mRealm.executeTransaction(realm -> {
                        client.setSid(sid);
                    });

                    getContractInfo(sid, contractId);
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SIDResponse> call, Throwable t) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    public static String decrypt(byte[] cipherText, byte[] balabolDoi) {
        byte[] IV = new byte[16];
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(balabolDoi, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}