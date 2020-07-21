package edi.md.mydesign.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.mydesign.BaseApp;
import edi.md.mydesign.R;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.authenticate.AuthenticateUserBody;
import edi.md.mydesign.remote.client.Client;
import edi.md.mydesign.remote.client.GetClientInfoResponse;
import edi.md.mydesign.remote.response.SIDResponse;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Igor on 03.07.2020
 */

public class FragmentLoginJuridic extends Fragment {

    EditText idnp, userName, passwords;
    Button signIn;
    TextView signUp;

    Realm mRealm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootViewAdmin = inflater.inflate(R.layout.fragment_login_juridic, container, false);

        idnp = rootViewAdmin.findViewById(R.id.editTextIDNP);
        userName = rootViewAdmin.findViewById(R.id.editTextUserName);
        passwords = rootViewAdmin.findViewById(R.id.editTextTextPassword);
        signIn = rootViewAdmin.findViewById(R.id.buttonSignInJuridic);
        signUp = rootViewAdmin.findViewById(R.id.buttonSignUpJuridic);
        mRealm = Realm.getDefaultInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpBottomSheetDialog auth = SignUpBottomSheetDialog.newInstance();
                auth.show(getParentFragmentManager(), SignUpBottomSheetDialog.TAG);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idno = idnp.getText().toString();
                String user = userName.getText().toString();
                String password = passwords.getText().toString();

                RealmResults<ClientRealm> realmResults = mRealm.where(ClientRealm.class).equalTo("iDNP",idno).and().equalTo("companyId", BaseApp.getAppInstance().getCompanyClicked().getId()).findAll();
                if(realmResults.isEmpty()){
                    auth(user,idno, password);
                }
                else{
                    for(ClientRealm clientRealm: realmResults){
                        if(clientRealm.getIDNP().equals(idno)){
                            new MaterialAlertDialogBuilder(getContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                                    .setTitle("Oops!")
                                    .setMessage("Client with same IDNP already exist!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    })
                                    .show();

                            break;
                        }
                    }
                }


            }
        });

        return rootViewAdmin;
    }
    private void auth (String login, String idno, String pass){
        AuthenticateUserBody user = new AuthenticateUserBody();
        user.setUser(login);
        user.setPassword(pass);
        user.setIDNO(idno);
        user.setAuthType(1);

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getAddress());
        Call<SIDResponse> call = commandServices.authenticateUser(user);

        call.enqueue(new Callback<SIDResponse>() {
            @Override
            public void onResponse(Call<SIDResponse> call, Response<SIDResponse> response) {
                SIDResponse sidResponse = response.body();
                if(sidResponse != null){
                    if(sidResponse.getErrorCode() == 0){
//                        Toast.makeText(getContext(), "sidResponse.getSID(): " + sidResponse.getSID(), Toast.LENGTH_SHORT).show();
                        String sId = sidResponse.getSID();
                        call.cancel();
                        getClientInfo(sId,login,pass);
                    }
                }

            }

            @Override
            public void onFailure(Call<SIDResponse> call, Throwable t) {
//                Toast.makeText(getContext(), "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getClientInfo(String sid,String userName, String userPass){
        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getAddress());
        Call<GetClientInfoResponse> call = commandServices.getClientInfo(sid);

        call.enqueue(new Callback<GetClientInfoResponse>() {
            @Override
            public void onResponse(Call<GetClientInfoResponse> call, Response<GetClientInfoResponse> response) {
                GetClientInfoResponse clientInfoResponse = response.body();
                if(clientInfoResponse != null && clientInfoResponse.getErrorCode() == 0){

                    try {
                        byte[] secUser = encrypt(userName.getBytes(), BaseApp.getAppInstance().getHuyYou());
                        byte[] secPa = encrypt(userPass.getBytes(), BaseApp.getAppInstance().getHuyYou());

                        Client client = clientInfoResponse.getClient();
                        client.setPassword(secPa);
                        client.setUserName(secUser);
                        client.setSid(sid);
                        client.setTypeClient(1);

//                        Toast.makeText(getContext(), "client.getSID(): " + client.getName() ,Toast.LENGTH_SHORT).show();

                        FragmentContracts.addedNewClient(client,1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetClientInfoResponse> call, Throwable t) {
//                Toast.makeText(getContext(), "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static byte[] encrypt(byte[] plaintext, byte[] balabol) throws Exception {
        byte[] IV = new byte[16];
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(balabol, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(plaintext);
        return cipherText;
    }
}
