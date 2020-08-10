package edi.md.mydesign.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.mydesign.BaseApp;
import edi.md.mydesign.FizicAccountActivity;
import edi.md.mydesign.JuridicAccountActivity;
import edi.md.mydesign.R;
import edi.md.mydesign.adapters.ClientsRealmAdapter;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.realm.objects.Company;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.authenticate.AuthenticateUserBody;
import edi.md.mydesign.remote.client.Client;
import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.remote.client.GetClientInfoResponse;
import edi.md.mydesign.remote.client.UnpaidDocument;
import edi.md.mydesign.remote.contract.Contract;
import edi.md.mydesign.remote.contract.GetContractInfoResponse;
import edi.md.mydesign.remote.response.SIDResponse;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Igor on 30.06.2020
 */

public class FragmentContracts extends Fragment {

    static ConstraintLayout layoutListContracts, layoutAddContract;

    static ListView list_contracts;

    Button addCabinet, addCard;
    static ImageView imageCompany;
    static TextView textTitle;

    static SignInBottomSheetDialog loginForm;

    static Context context;
    static Company company;

    static ClientsRealmAdapter adapter;

    static ProgressDialog progressDialog;

    TextView addNewContract;

    static Realm mRealm;

    CommandServices commandServices;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootViewAdmin = inflater.inflate(R.layout.fragment_contracts_v0, container, false);

        list_contracts = rootViewAdmin.findViewById(R.id.list_contracts);
        layoutAddContract = rootViewAdmin.findViewById(R.id.layoutAdd);
        layoutListContracts = rootViewAdmin.findViewById(R.id.layout_list_contracts);
        addNewContract = rootViewAdmin.findViewById(R.id.textView_add_contract);
        addCabinet = rootViewAdmin.findViewById(R.id.buttonAddCabinetPersonal);
        addCard = rootViewAdmin.findViewById(R.id.buttonAddFuelCard);
        imageCompany = rootViewAdmin.findViewById(R.id.image_company_logon);
        textTitle = rootViewAdmin.findViewById(R.id.text_message_welcome);


        mRealm = Realm.getDefaultInstance();
        context = getActivity();
        progressDialog = new ProgressDialog(context,R.style.ThemeOverlay_AppCompat_Dialog_Alert_TestDialogTheme);
        company = BaseApp.getAppInstance().getCompanyClicked();

        commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getAddress());


        updateListClients();

        list_contracts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClientRealm client = adapter.getItem(i);

                progressDialog.setMessage("load contract info...");
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();

                getClientInfo(client);
            }
        });



        addCabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginForm = SignInBottomSheetDialog.newInstance();
                loginForm.show(getParentFragmentManager(), SignInBottomSheetDialog.TAG);
            }
        });

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCardBottomSheetDialog cardForm = AddCardBottomSheetDialog.newInstance();
                cardForm.show(getParentFragmentManager(), AddCardBottomSheetDialog.TAG);
            }
        });

        addNewContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View dialogView = inflater.inflate(R.layout.dialog_add_cabinet_or_card, null);

                AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setView(dialogView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ImageView logo = dialogView.findViewById(R.id.dialog_image_company_logon);
                TextView title = dialogView.findViewById(R.id.dialog_text_message_welcome);
                Button addCardDialog = dialogView.findViewById(R.id.dialog_buttonAddFuelCard);
                Button addCabinteDialog = dialogView.findViewById(R.id.dialog_buttonAddCabinetPersonal);

                byte[] decodedString = Base64.decode(company.getLogo(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                logo.setImageBitmap(decodedByte);
                title.setText(getString(R.string.message_dialog_login) + company.getName() + "!");

                addCabinteDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        loginForm = SignInBottomSheetDialog.newInstance();
                        loginForm.show(getParentFragmentManager(), SignInBottomSheetDialog.TAG);
                    }
                });

                addCardDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        AddCardBottomSheetDialog cardForm = AddCardBottomSheetDialog.newInstance();
                        cardForm.show(getParentFragmentManager(), AddCardBottomSheetDialog.TAG);
                    }
                });

                dialog.show();
            }
        });

        return rootViewAdmin;
    }

    private void getClientInfo(ClientRealm client) {
        int types = client.getTypeClient();

        AuthenticateUserBody user = new AuthenticateUserBody();

        if(types == 0){
            user.setPassword(decrypt(client.getPassword(),BaseApp.getAppInstance().getHuyYou()));
            user.setPhone(decrypt(client.getPhone(),BaseApp.getAppInstance().getHuyYou()));
            user.setAuthType(0);
        }
        else if (types == 1){
            user.setPassword(decrypt(client.getPassword(),BaseApp.getAppInstance().getHuyYou()));
            user.setUser(decrypt(client.getUserName(),BaseApp.getAppInstance().getHuyYou()));
            user.setIDNO(client.getIDNP());
            user.setAuthType(1);
        }

        String sid = client.getSid();


        Call<GetClientInfoResponse> call = commandServices.getClientInfo(sid);

        call.enqueue(new Callback<GetClientInfoResponse>() {
            @Override
            public void onResponse(Call<GetClientInfoResponse> call, Response<GetClientInfoResponse> response) {
                GetClientInfoResponse clientInfoResponse = response.body();
                if (clientInfoResponse != null ){
                    if( clientInfoResponse.getErrorCode() == 0){
                        mRealm.executeTransaction(realm -> {
                            updateClient(client,clientInfoResponse.getClient());
                        });
                    }

                    else{
                        reAuth(user,client);
                    }
                }
                else{
                    progressDialog.dismiss();
                    Log.d("TAG", "GetClientInfoTag onFailure null response");
                }

            }

            @Override
            public void onFailure(Call<GetClientInfoResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("TAG", "GetClientInfoTag onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }

    private void getContractInfo(ClientRealm client) {
        String sid = client.getSid();

        String contractId = client.getContracts().get(0).getID();

        Call<GetContractInfoResponse> call = commandServices.getContractInfo(sid,contractId);

        call.enqueue(new Callback<GetContractInfoResponse>() {
            @Override
            public void onResponse(Call<GetContractInfoResponse> call, Response<GetContractInfoResponse> response) {
                GetContractInfoResponse contractInfoResponse = response.body();
                if(contractInfoResponse != null){
                    if(contractInfoResponse.getErrorCode() == 0){
                        BaseApp.getAppInstance().setClientClicked(mRealm.copyFromRealm(client));
                        BaseApp.getAppInstance().setClickedClientContract(contractInfoResponse.getContract());

                        if(client.getTypeClient() == 1)
                            startActivity(new Intent(getContext(), JuridicAccountActivity.class));
                        else if (client.getTypeClient() == 0)
                            startActivity(new Intent(getContext(), FizicAccountActivity.class));
                    }
                    else{
                        progressDialog.dismiss();
                        Log.d("TAG", "getContractInfoTag onFailure errore: " + contractInfoResponse.getErrorCode());
                    }
                }
                else{
                    progressDialog.dismiss();
                    Log.d("TAG", "getContractInfoTag onFailure null response: ");
                }
            }

            @Override
            public void onFailure(Call<GetContractInfoResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("TAG", "getContractInfoTag onFailure: " + t.getMessage());
            }
        });
    }

    private void reAuth (AuthenticateUserBody user, ClientRealm client){
        Call<SIDResponse> call = commandServices.authenticateUser(user);

        call.enqueue(new Callback<SIDResponse>() {
            @Override
            public void onResponse(Call<SIDResponse> call, Response<SIDResponse> response) {
                SIDResponse sidResponse = response.body();
                if(sidResponse != null){
                    if(sidResponse.getErrorCode() == 0){
                        String sId = sidResponse.getSID();
                        mRealm.executeTransaction(realm -> {
                            client.setSid(sId);
                        });

                        getClientInfo(client);
                    }
                    else{
                        progressDialog.dismiss();
                        Log.d("TAG", "reAuthTag onFailure errore:" + sidResponse.getErrorCode() );
                    }
                }
                else{
                    progressDialog.dismiss();
                    Log.d("TAG", "reAuthTag onFailure null response");
                }
            }

            @Override
            public void onFailure(Call<SIDResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("TAG", "reAuthTag onFailure: " + t.getMessage());
            }
        });
    }

    public static void dismissDialog(){
        if(loginForm.getShowsDialog()){
            loginForm.dismiss();
        }
    }

    public static void addedNewClient (Client client, int type){
        ClientRealm newClient = copyClientToRealm(client,type);

        mRealm.executeTransaction(realm -> {
            realm.insert(newClient);
        });

        dismissDialog();
        updateListClients();

    }

    private static void updateListClients(){
        RealmResults<ClientRealm>  results = mRealm.where(ClientRealm.class).equalTo("companyId", company.getId()).findAll();
        if(results.isEmpty()){
            layoutAddContract.setVisibility(View.VISIBLE);
            byte[] decodedString = Base64.decode(company.getLogo(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageCompany.setImageBitmap(decodedByte);
            textTitle.setText(context.getResources().getString(R.string.message_dialog_login) + company.getName() + "!");
            layoutListContracts.setVisibility(View.GONE);
        }
        else {
            layoutAddContract.setVisibility(View.GONE);
            layoutListContracts.setVisibility(View.VISIBLE);

            adapter = new ClientsRealmAdapter(context,results);
            list_contracts.setAdapter(adapter);
        }

    }

    private static ClientRealm copyClientToRealm(Client client, int type){
        if(type == 0){
            ClientRealm realmClient = new ClientRealm();
            realmClient.setTypeClient(0);
            realmClient.setCompanyId(company.getId());
            realmClient.setAmount(client.getAmount());
            realmClient.setBalance(client.getBalance());
            realmClient.setCardsBalance(client.getCardsBalance());
            realmClient.setCredit(client.getCredit());
            realmClient.setIDNP(client.getIDNP());
            realmClient.setName(client.getName());
            realmClient.setMasterBalance(client.getMasterBalance());
            realmClient.setOverdraft(client.getOverdraft());
            realmClient.setNonInvoicedConsumptionAmount(client.getNonInvoicedConsumptionAmount());
            realmClient.setUnpaidInvoiceConsumptionAmount(client.getUnpaidInvoiceConsumptionAmount());
            realmClient.setTotalDebtSum(client.getTotalDebtSum());
            realmClient.setStatus(client.getStatus());

            List<UnpaidDocument> list = client.getUnpaidDocuments();
            RealmList<UnpaidDocument> doc = new RealmList<>();
            doc.addAll(list);
            realmClient.setUnpaidDocuments(doc);

            List<ContractInClient> list2 = client.getContracts();
            RealmList<ContractInClient> docs = new RealmList<>();
            docs.addAll(list2);
            realmClient.setContracts(docs);

            realmClient.setPassword(client.getPassword());
            realmClient.setPhone(client.getPhone());
            realmClient.setSid(client.getSid());

            return realmClient;
        }
        else{
            ClientRealm realmClient = new ClientRealm();
            realmClient.setTypeClient(1);
            realmClient.setCompanyId(company.getId());
            realmClient.setAmount(client.getAmount());
            realmClient.setBalance(client.getBalance());
            realmClient.setCardsBalance(client.getCardsBalance());
            realmClient.setCredit(client.getCredit());
            realmClient.setIDNP(client.getIDNP());
            realmClient.setName(client.getName());
            realmClient.setMasterBalance(client.getMasterBalance());
            realmClient.setOverdraft(client.getOverdraft());
            realmClient.setNonInvoicedConsumptionAmount(client.getNonInvoicedConsumptionAmount());
            realmClient.setUnpaidInvoiceConsumptionAmount(client.getUnpaidInvoiceConsumptionAmount());
            realmClient.setTotalDebtSum(client.getTotalDebtSum());
            realmClient.setStatus(client.getStatus());

            List<UnpaidDocument> list = client.getUnpaidDocuments();
            RealmList<UnpaidDocument> doc = new RealmList<>();
            doc.addAll(list);
            realmClient.setUnpaidDocuments(doc);

            List<ContractInClient> list2 = client.getContracts();
            RealmList<ContractInClient> docs = new RealmList<>();
            docs.addAll(list2);
            realmClient.setContracts(docs);

            realmClient.setPassword(client.getPassword());
            realmClient.setUserName(client.getUserName());
            realmClient.setSid(client.getSid());

            return realmClient;
        }
    }
    private void updateClient(ClientRealm realmClient,Client client){

        realmClient.setAmount(client.getAmount());
        realmClient.setBalance(client.getBalance());
        realmClient.setCardsBalance(client.getCardsBalance());
        realmClient.setCredit(client.getCredit());
        realmClient.setName(client.getName());
        realmClient.setMasterBalance(client.getMasterBalance());
        realmClient.setOverdraft(client.getOverdraft());
        realmClient.setNonInvoicedConsumptionAmount(client.getNonInvoicedConsumptionAmount());
        realmClient.setUnpaidInvoiceConsumptionAmount(client.getUnpaidInvoiceConsumptionAmount());
        realmClient.setTotalDebtSum(client.getTotalDebtSum());
        realmClient.setStatus(client.getStatus());

        List<UnpaidDocument> list = client.getUnpaidDocuments();
        RealmList<UnpaidDocument> unpaidRealm = realmClient.getUnpaidDocuments();
        unpaidRealm.deleteAllFromRealm();
        unpaidRealm.addAll(list);
        realmClient.setUnpaidDocuments(unpaidRealm);

        List<ContractInClient> list1 = client.getContracts();
        RealmList<ContractInClient> unpaidRealm1 = realmClient.getContracts();
        unpaidRealm1.deleteAllFromRealm();
        unpaidRealm1.addAll(list1);
        realmClient.setContracts(unpaidRealm1);

        getContractInfo(realmClient);
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
