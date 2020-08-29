package edi.md.mydesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.mydesign.adapters.CardsInContractClientAdapter;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.RemoteException;
import edi.md.mydesign.remote.authenticate.AuthenticateUserBody;
import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.remote.contract.CardsList;
import edi.md.mydesign.remote.contract.Contract;
import edi.md.mydesign.remote.contract.GetContractInfoResponse;
import edi.md.mydesign.remote.response.SIDResponse;
import edi.md.mydesign.utils.BaseEnum;
import edi.md.mydesign.utils.RecyclerItemClickListener;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardListActivity extends AppCompatActivity {
    ClientRealm client;
    ImageButton btnBack;
    ImageButton btnRetry;
    TextView title;
    AutoCompleteTextView editTextSearch;

    RecyclerView recyclerViewCards;
    Spinner contractsSpinner;
    ConstraintLayout filter;
    ConstraintLayout error;
    ProgressBar progressBar;

    CardsInContractClientAdapter adapterList;
    CommandServices commandServices;
    ArrayAdapter<String> adapter;

    List<CardsList> contractCardsList = new ArrayList<>();
    String sid;
    String contractId;

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getWindow().getDecorView();
        Window window = getWindow();
        view.setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        view.setFitsSystemWindows(true);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        setContentView(R.layout.activity_card_list);

        btnBack = findViewById(R.id.btn_back_to_account);
        btnRetry = findViewById(R.id.img_retry_image);
        title = findViewById(R.id.title_contract_name);
        contractsSpinner = findViewById(R.id.spinner_contracts_in_cards);
        filter = findViewById(R.id.csl_filtering);
        error = findViewById(R.id.cl_error_image);
        recyclerViewCards = findViewById(R.id.recyclerView_contracts);
        progressBar = findViewById(R.id.progressBar_getCards);
        editTextSearch = findViewById(R.id.search_field_card);

        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCards.setLayoutManager(layoutManagerV);

        client = BaseApp.getAppInstance().getClientClicked();
        mRealm = Realm.getDefaultInstance();
        commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());

        List<ContractInClient> contracts = client.getContracts();
        List<String> contractName = new ArrayList<>();

        if(contracts.size() > 1){
            filter.setVisibility(View.VISIBLE);

            for(int i = 0; i < contracts.size(); i ++){
                contractName.add(contracts.get(i).getCode());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,contractName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            contractsSpinner.setAdapter(adapter);
            contractsSpinner.setSelection(0);
        }
        else{
            filter.setVisibility(View.GONE);
            recyclerViewCards.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            ContractInClient first = contracts.get(0);
            sid = client.getSid();
            contractId = first.getID();

            getContractInfo(sid,contractId);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerViewCards.addOnItemTouchListener(
                new RecyclerItemClickListener(CardListActivity.this , recyclerViewCards,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        CardsList item = adapterList.getItem(position);
                        BaseApp.getAppInstance().setClickedCard(item);

                        startActivity(new Intent(CardListActivity.this , CardDetailActivity.class));
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        contractsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                recyclerViewCards.setVisibility(View.INVISIBLE);
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

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchCard(editTextSearch.getText().toString());
                    editTextSearch.dismissDropDown();
                }
                return true;
            }
        });

        editTextSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchCard(adapter.getItem(i));
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = String.valueOf(charSequence);
                if(text.equals("")){
                    adapterList = new CardsInContractClientAdapter(CardListActivity.this, contractCardsList);
                    recyclerViewCards.setAdapter(adapterList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchCard(String text ){
        List<CardsList> cardsList = new ArrayList<>();

        if (contractCardsList != null && contractCardsList.size() > 0){
            for(CardsList card: contractCardsList){
                String name = card.getName().toUpperCase();

                if(name.equals(text.toUpperCase()) || name.contains(text.toUpperCase()))
                    cardsList.add(card);
            }

            adapterList = new CardsInContractClientAdapter(CardListActivity.this, cardsList);
            recyclerViewCards.setAdapter(adapterList);
        }
    }

    private void getContractInfo(String sid, String contractId) {
        Call<GetContractInfoResponse> call = commandServices.getContractInfo(BaseApp.getAppInstance().getCompanyClicked().getServiceName(),sid, contractId);
        enqueueCall(call,sid,contractId);
    }

    private void enqueueCall(Call<GetContractInfoResponse> call, String sid, String contractId) {
        Log.d("TAG", "sid + contractId" + sid + " + " + contractId);

        call.enqueue(new Callback<GetContractInfoResponse>() {
            @Override
            public void onResponse(Call<GetContractInfoResponse> call, Response<GetContractInfoResponse> response) {
                GetContractInfoResponse contractInfoResponse = response.body();

                Log.d("TAG", "onResponse url: " + call.request());
                Log.d("TAG", "onResponse ->" + contractInfoResponse.getErrorCode() + "|| contractId -> " + contractId + "|| sid -> " + sid);

                if (contractInfoResponse != null) {
                    if (contractInfoResponse.getErrorCode() == 0) {

                        Contract contract = contractInfoResponse.getContract();
                        contractCardsList = contract.getCardsList();

                        List<String> cardName = new ArrayList<>();
                        for(int i = 0; i < contractCardsList.size(); i++){
                            cardName.add(contractCardsList.get(i).getName());
                            cardName.add(contractCardsList.get(i).getCode());
                        }
                        adapter = new ArrayAdapter<>(CardListActivity.this, android.R.layout.simple_list_item_1,cardName);
                        editTextSearch.setAdapter(adapter);


                        adapterList = new CardsInContractClientAdapter(CardListActivity.this, contractCardsList);
                        recyclerViewCards.setAdapter(adapterList);

                        recyclerViewCards.setVisibility(View.VISIBLE);
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
                    //TOOD aruncat la lista de conturi
                }
            }

            @Override
            public void onFailure(Call<SIDResponse> call, Throwable t) {
                //TOOD aruncat la lista de conturi
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(event);
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