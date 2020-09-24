package edi.md.petrolcabinet.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import edi.md.petrolcabinet.BaseApp;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.bottomsheet.ResetPasswordJuridicBottomSheetDialog;
import edi.md.petrolcabinet.bottomsheet.SignUpBottomSheetDialog;
import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.remote.ApiUtils;
import edi.md.petrolcabinet.remote.CommandServices;
import edi.md.petrolcabinet.remote.RemoteException;
import edi.md.petrolcabinet.remote.authenticate.AuthenticateUserBody;
import edi.md.petrolcabinet.remote.client.Client;
import edi.md.petrolcabinet.remote.client.GetClientInfoResponse;
import edi.md.petrolcabinet.remote.response.SIDResponse;
import edi.md.petrolcabinet.utils.BaseEnum;
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
                    layoutIndp.setError(getString(R.string.error_input_idnp));
                    layoutUserName.setError(getString(R.string.error_input_username));
                    layoutPassword.setError(getString(R.string.error_input_password));
                }
                else{
                    if(idno.equals("") || user.equals("") || password.equals("")){
                        if(idno.equals("")){
                            layoutIndp.setError(getString(R.string.error_input_idnp));
                        }
                        if (user.equals("")){
                            layoutUserName.setError(getString(R.string.error_input_username));
                        }
                        if (password.equals("")){
                            layoutPassword.setError(getString(R.string.error_input_password));
                        }
                    }
                    else{
                        RealmResults<Accounts> realmResults = mRealm.where(Accounts.class).equalTo("iDNP",idno).and().equalTo("companyId", BaseApp.getAppInstance().getCompanyClicked().getId()).findAll();
                        if(realmResults.isEmpty()){
                            progressDialog.setIndeterminate(false);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage(getString(R.string.progres_msg_authenticate));
                            progressDialog.show();

                            auth(user,idno, password);
                        }
                        else{
                            for(Accounts clientRealm: realmResults){
                                if(clientRealm.getIDNP().equals(idno)){
                                    new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                                            .setTitle(getString(R.string.oops_text))
                                            .setMessage(getString(R.string.client_with_some_idnp_exist))
                                            .setCancelable(false)
                                            .setPositiveButton(getString(R.string.dialog_btn_ok), (dialogInterface, i) -> {
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

        forgotPassword.setOnClickListener(view -> {
            ResetPasswordJuridicBottomSheetDialog reset = ResetPasswordJuridicBottomSheetDialog.newInstance();
            reset.show(getParentFragmentManager(), ResetPasswordJuridicBottomSheetDialog.TAG);
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
                                    .setTitle(getString(R.string.oops_text)      )
                                    .setMessage(sidResponse.getErrorMessage())
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    })
                                    .show();
                        }
                        else {
                            progressDialog.setMessage(getString(R.string.progres_msg_get_info_client));
                            getClientInfo(sId,login,pass);
                        }

                    }
                    else{
                        String msg = RemoteException.getServiceException(sidResponse.getErrorCode());
                        //progressDialog dismiss
                        progressDialog.dismiss();

                        new MaterialAlertDialogBuilder(getContext(),R.style.MaterialAlertDialogCustom)
                                .setTitle(getString(R.string.oops_text)      )
                                .setMessage(msg)
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                })
                                .show();
                    }
                }
                else{
                    progressDialog.dismiss();

                    new MaterialAlertDialogBuilder(getContext(),R.style.MaterialAlertDialogCustom)
                            .setTitle(getString(R.string.oops_text)      )
                            .setMessage(getString(R.string.dialog_msg_response_from_service_null))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<SIDResponse> call, Throwable t) {
                progressDialog.dismiss();

                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                        .setTitle(getString(R.string.oops_text)      )
                        .setMessage(getString(R.string.dialog_failure_service) + t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .setNegativeButton(getString(R.string.dialog_button_retry), ((dialogInterface, i) -> {
                            progressDialog.setIndeterminate(false);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage(getString(R.string.progres_msg_authenticate));
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
                            .setTitle(getString(R.string.oops_text)      )
                            .setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
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
                        .setTitle(getString(R.string.oops_text)      )
                        .setMessage(getString(R.string.dialog_failure_service) + t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .setNegativeButton(getString(R.string.dialog_button_retry), ((dialogInterface, i) -> {
                            progressDialog.setIndeterminate(false);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage(getString(R.string.progres_msg_get_info_client));
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
