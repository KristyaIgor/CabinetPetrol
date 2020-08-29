package edi.md.mydesign.fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.mydesign.BaseApp;
import edi.md.mydesign.R;
import edi.md.mydesign.bottomsheet.SignUpBottomSheetDialog;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.RemoteException;
import edi.md.mydesign.remote.authenticate.AuthenticateUserBody;
import edi.md.mydesign.remote.client.Client;
import edi.md.mydesign.remote.client.GetClientInfoResponse;
import edi.md.mydesign.remote.response.SIDResponse;
import edi.md.mydesign.utils.BaseEnum;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Igor on 03.07.2020
 */

public class FragmentLoginJuridic extends Fragment {

    Button signIn;
    TextView signUp, forgotPassword;
    TextInputEditText passwords, userName, idnp;
    Realm mRealm;

    String idno,user, password, sId;
    ProgressDialog progressDialog;

    TextInputLayout layoutPassword, layoutUserName, layoutIndp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootViewAdmin = inflater.inflate(R.layout.fragment_login_juridic, container, false);

        idnp = rootViewAdmin.findViewById(R.id.editTextIDNP);
        userName = rootViewAdmin.findViewById(R.id.editTextUserName);
        passwords = rootViewAdmin.findViewById(R.id.editTextTextPassword);
        signIn = rootViewAdmin.findViewById(R.id.buttonSignInJuridic);
        signUp = rootViewAdmin.findViewById(R.id.buttonSignUpJuridic);
        forgotPassword = rootViewAdmin.findViewById(R.id.text_forgot_password_juridic);
        layoutPassword = rootViewAdmin.findViewById(R.id.editTextTextPasswordLayout);
        layoutUserName = rootViewAdmin.findViewById(R.id.editTextUserNameLayout);
        layoutIndp = rootViewAdmin.findViewById(R.id.editTextIDNPLayout);

        mRealm = Realm.getDefaultInstance();
        progressDialog = new ProgressDialog(getContext(),  R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);

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
                idno = idnp.getText().toString();
                user = userName.getText().toString();
                password = passwords.getText().toString();

                if (idno.equals("") && user.equals("") && password.equals("")){
                    layoutIndp.setError("Introduceți IDNP!");
                    layoutUserName.setError("Introduceți numele utilizatorului!");
                    layoutPassword.setError("Introduceți parola!");
                }
                else{
                    if(idno.equals("") || user.equals("") || password.equals("")){
                        if(idno.equals("")){
                            layoutIndp.setError("Introduceți IDNP!");
                        }
                        if (user.equals("")){
                            layoutUserName.setError("Introduceți numele utilizatorului!");
                        }
                        if (password.equals("")){
                            layoutPassword.setError("Introduceți parola!");
                        }
                    }
                    else{
                        RealmResults<ClientRealm> realmResults = mRealm.where(ClientRealm.class).equalTo("iDNP",idno).and().equalTo("companyId", BaseApp.getAppInstance().getCompanyClicked().getId()).findAll();
                        if(realmResults.isEmpty()){
                            progressDialog.setIndeterminate(false);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("autentificare...");
                            progressDialog.show();

                            auth(user,idno, password);
                        }
                        else{
                            for(ClientRealm clientRealm: realmResults){
                                if(clientRealm.getIDNP().equals(idno)){
                                    new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                                            .setTitle("Oops!")
                                            .setMessage("Clientul cu același IDNP există deja!")
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
                }
            }
        });

        idnp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    layoutIndp.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    layoutUserName.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passwords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    layoutPassword.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return rootViewAdmin;
    }
    private void auth (String login, String idno, String pass){
        AuthenticateUserBody authenticateUserBody = new AuthenticateUserBody();
        authenticateUserBody.setUser(login);
        authenticateUserBody.setPassword(pass);
        authenticateUserBody.setIDNO(idno);
        authenticateUserBody.setAuthType(1);

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<SIDResponse> call = commandServices.authenticateUser(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), authenticateUserBody);

        call.enqueue(new Callback<SIDResponse>() {
            @Override
            public void onResponse(Call<SIDResponse> call, Response<SIDResponse> response) {
                SIDResponse sidResponse = response.body();
                if(sidResponse != null){
                    if(sidResponse.getErrorCode() == 0){
//                        Toast.makeText(getContext(), "sidResponse.getSID(): " + sidResponse.getSID(), Toast.LENGTH_SHORT).show();
                        sId = sidResponse.getSID();

                        if(sId == null){
                            progressDialog.dismiss();
                            new MaterialAlertDialogBuilder(getContext(),R.style.MaterialAlertDialogCustom)
                                    .setTitle("Oops!")
                                    .setMessage(sidResponse.getErrorMessage())
                                    .setCancelable(false)
                                    .setPositiveButton("Am înţeles", (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    })
                                    .show();
                        }
                        else {
                            progressDialog.setMessage("obținerea informației despre client...");
                            getClientInfo(sId,login,pass);
                        }

                    }
                    else{
                        String msg = RemoteException.getServiceException(sidResponse.getErrorCode());
                        //progressDialog dismiss
                        progressDialog.dismiss();

                        new MaterialAlertDialogBuilder(getContext(),R.style.MaterialAlertDialogCustom)
                                .setTitle("Oops!")
                                .setMessage(msg)
                                .setCancelable(false)
                                .setPositiveButton("Am înţeles", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                })
                                .show();
                    }
                }
                else{
                    progressDialog.dismiss();

                    new MaterialAlertDialogBuilder(getContext(),R.style.MaterialAlertDialogCustom)
                            .setTitle("Oops!")
                            .setMessage("Răspunsul de la serviciu este gol!")
                            .setCancelable(false)
                            .setPositiveButton("Am înţeles", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<SIDResponse> call, Throwable t) {
                progressDialog.dismiss();

                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                        .setTitle("Oops!")
                        .setMessage("Operația a eșuat!\nEroare: " + t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("Am înţeles", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .setNegativeButton("Reîncercați", ((dialogInterface, i) -> {
                            progressDialog.setIndeterminate(false);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("autentificare...");
                            progressDialog.show();
                            auth(user,idno, password);
                        }))
                        .show();
            }
        });
    }

    private void getClientInfo(String sid,String userName, String userPass){
        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<GetClientInfoResponse> call = commandServices.getClientInfo(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), sid);

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
                        client.setTypeClient(BaseEnum.PersoanaJuridica);

//                        Toast.makeText(getContext(), "client.getSID(): " + client.getName() ,Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        FragmentCabinetsAndCards.addedNewClient(client);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    String msg = RemoteException.getServiceException(clientInfoResponse.getErrorCode());
                    //progressDialog dismiss
                    progressDialog.dismiss();

                    new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                            .setTitle("Oops!")
                            .setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton("Am înţeles", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<GetClientInfoResponse> call, Throwable t) {
                //progressDialog dismiss
                progressDialog.dismiss();

                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                        .setTitle("Oops!")
                        .setMessage("Operația a eșuat!\nEroare: " + t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("Am înţeles", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .setNegativeButton("Reîncercați", ((dialogInterface, i) -> {
                            progressDialog.setIndeterminate(false);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("obținerea informații despre client...");
                            progressDialog.show();
                            getClientInfo(sId,user,password);
                        }))
                        .show();
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
