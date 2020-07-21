package edi.md.mydesign.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.mydesign.BaseApp;
import edi.md.mydesign.ContractActivity;
import edi.md.mydesign.R;
import edi.md.mydesign.adapters.ClientsRealmAdapter;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.realm.objects.Company;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.authenticate.AuthenticateUserBody;
import edi.md.mydesign.remote.client.Client;
import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.remote.client.UnpaidDocument;
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
    static ImageButton addContract;

    static SignInBottomSheetDialog loginForm;

    static Context context;
    static Company company;

    static ClientsRealmAdapter adapter;

    static ProgressDialog progressDialog;

    TextView addNewContract;

    static Realm mRealm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootViewAdmin = inflater.inflate(R.layout.fragment_contracts_v0, container, false);

        list_contracts = rootViewAdmin.findViewById(R.id.list_contracts);
        addContract = rootViewAdmin.findViewById(R.id.attach_company_to_cabinet);
        layoutAddContract = rootViewAdmin.findViewById(R.id.layoutAdd);
        layoutListContracts = rootViewAdmin.findViewById(R.id.layout_list_contracts);
        addNewContract = rootViewAdmin.findViewById(R.id.textView_add_contract);

        mRealm = Realm.getDefaultInstance();
        context = getActivity();
        progressDialog = new ProgressDialog(context,R.style.ThemeOverlay_AppCompat_Dialog_Alert_TestDialogTheme);
        company = BaseApp.getAppInstance().getCompanyClicked();

        updateListClients();

        list_contracts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClientRealm client = adapter.getItem(i);

                BaseApp.getAppInstance().setClientClicked(mRealm.copyFromRealm(client));

                progressDialog.dismiss();
                startActivity(new Intent(getContext(), ContractActivity.class));



//                getContractInfo(client);
            }
        });

        addContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginForm = SignInBottomSheetDialog.newInstance();
                loginForm.show(getParentFragmentManager(), SignInBottomSheetDialog.TAG);
            }
        });

        addNewContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginForm = SignInBottomSheetDialog.newInstance();
                loginForm.show(getParentFragmentManager(), SignInBottomSheetDialog.TAG);
            }
        });

        return rootViewAdmin;
    }

    private void getContractInfo(ClientRealm client) {
        progressDialog.setMessage("load contract info...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

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

        ContractInClient contract = client.getContracts().first();
        String contractId = contract.getID();

        CommandServices commandServices = ApiUtils.getCommandServices(company.getAddress());
        Call<GetContractInfoResponse> call = commandServices.getContractInfo(sid,contractId);

        call.enqueue(new Callback<GetContractInfoResponse>() {
            @Override
            public void onResponse(Call<GetContractInfoResponse> call, Response<GetContractInfoResponse> response) {
                GetContractInfoResponse contractInfoResponse = response.body();
                if(contractInfoResponse != null && contractInfoResponse.getErrorCode() == 0){
                    BaseApp.getAppInstance().setClientClicked(mRealm.copyFromRealm(client));
                    BaseApp.getAppInstance().setClickedClientContract(contractInfoResponse.getContract());

                    progressDialog.dismiss();
                    startActivity(new Intent(getContext(), ContractActivity.class));

                }
                else { //if(contractInfoResponse.getErrorCode() == 5)  authUser()   get error code
                    Toast.makeText(context, "Error code: " + contractInfoResponse.getErrorCode(), Toast.LENGTH_SHORT).show();
                    reAuth(user,client);

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetContractInfoResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });


    }

    private void reAuth (AuthenticateUserBody user, ClientRealm client){

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getAddress());
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

                        getContractInfo(client);
                    }
                }
            }

            @Override
            public void onFailure(Call<SIDResponse> call, Throwable t) {

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
            RealmList<ContractInClient> doc2 = new RealmList<>();
            doc2.addAll(list2);
            realmClient.setContracts(doc2);

            realmClient.setPassword(client.getPassword());
            realmClient.setPhone(client.getPhone());
            realmClient.setSid(client.getSid());

            return realmClient;
        }
        else{
            ClientRealm realmClient = new ClientRealm();
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
            RealmList<ContractInClient> doc2 = new RealmList<>();
            doc2.addAll(list2);
            realmClient.setContracts(doc2);

            realmClient.setPassword(client.getPassword());
            realmClient.setPhone(client.getPhone());
            realmClient.setSid(client.getSid());

            return realmClient;
        }
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
