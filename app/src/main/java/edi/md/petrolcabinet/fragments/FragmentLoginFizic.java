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
import edi.md.petrolcabinet.bottomsheet.ResetPasswordFizicBottomSheetDialog;
import edi.md.petrolcabinet.bottomsheet.SignUpBottomSheetDialog;
import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.remoteSettings.ApiUtils;
import edi.md.petrolcabinet.remoteSettings.CommandServices;
import edi.md.petrolcabinet.remoteSettings.RemoteException;
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

public class FragmentLoginFizic extends Fragment {

    TextInputEditText password, phone;
    TextInputLayout passwordLayout, phoneLayout;
    Button signIn;
    TextView signUp, resetPassword;

    Realm mRealm;
    ProgressDialog progressDialog;
    SignUpBottomSheetDialog signUpBottomSheetDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootViewAdmin = inflater.inflate(R.layout.fragment_login_fizic, container, false);

        phone = rootViewAdmin.findViewById(R.id.editTextPhoneLogin);
        password = rootViewAdmin.findViewById(R.id.editTextTextPasswordLogin);
        signIn = rootViewAdmin.findViewById(R.id.buttonSignInFizic);
        signUp = rootViewAdmin.findViewById(R.id.buttonSignUpFizic);
        passwordLayout = rootViewAdmin.findViewById(R.id.editTextTextPasswordLoginLayout);
        phoneLayout = rootViewAdmin.findViewById(R.id.editTextPhoneLoginPLayout);
        resetPassword = rootViewAdmin.findViewById(R.id.text_forgot_password_fizic);

        mRealm = Realm.getDefaultInstance();
        progressDialog = new ProgressDialog(getContext(),  R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpBottomSheetDialog = SignUpBottomSheetDialog.newInstance();
                signUpBottomSheetDialog.show(getParentFragmentManager(), SignUpBottomSheetDialog.TAG);
            }
        });

        resetPassword.setOnClickListener(view -> {
            ResetPasswordFizicBottomSheetDialog reset = ResetPasswordFizicBottomSheetDialog.newInstance();
            reset.show(getParentFragmentManager(), ResetPasswordFizicBottomSheetDialog.TAG);
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phone.getText().toString();
                String passwords = password.getText().toString();


                if(phoneNumber.equals("") && passwords.equals("")){
                    phoneLayout.setError(getString(R.string.error_input_number_phone));
                    passwordLayout.setError(getString(R.string.error_input_password));
                }
                else{
                    if(phoneNumber.equals("") || passwords.equals("")){
                        if(phoneNumber.equals("")){
                            passwordLayout.setError(getString(R.string.error_input_number_phone));
                        }
                        if (passwords.equals("")){
                            passwordLayout.setError(getString(R.string.error_input_password));
                        }
                    }
                    else{
                        byte[] byePhone = new byte[0];
                        byte[] bytePass = new byte[0];
                        try {
                            byePhone = encrypt(phoneNumber.getBytes(),BaseApp.getAppInstance().getHuyYou());
                            bytePass = encrypt(passwords.getBytes(),BaseApp.getAppInstance().getHuyYou());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        RealmResults<Accounts> realmResults = mRealm.where(Accounts.class)
                                .equalTo("phone",byePhone)
                                .and()
                                .equalTo("password",bytePass)
                                .and()
                                .equalTo("companyId", BaseApp.getAppInstance().getCompanyClicked().getId())
                                .findAll();
                        if(realmResults.isEmpty()){
                            progressDialog.setIndeterminate(false);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage(getString(R.string.progres_msg_authenticate));
                            progressDialog.show();

                            auth(phoneNumber, passwords);
                        }
                        else{
                            for(Accounts clientRealm: realmResults){
                                String realmPhone = decrypt(clientRealm.getPhone(),BaseApp.getAppInstance().getHuyYou());
                                if(realmPhone.equals(phoneNumber)){
                                    new MaterialAlertDialogBuilder(getContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                                            .setTitle(getString(R.string.oops_text)      )
                                            .setMessage(getString(R.string.client_with_some_phone_exist))
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

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    phoneLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    passwordLayout.setError(null);
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

    private void auth (String phones, String pass){
        AuthenticateUserBody user = new AuthenticateUserBody();
        user.setPassword(pass);
        user.setPhone(phones);
        user.setAuthType(0);

        String url = BaseApp.getAppInstance().getCompanyClicked().getIp();
        CommandServices commandServices = ApiUtils.getCommandServices(url);
        Call<SIDResponse> call = commandServices.authenticateUser(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), user);

        call.enqueue(new Callback<SIDResponse>() {
            @Override
            public void onResponse(Call<SIDResponse> call, Response<SIDResponse> response) {
                SIDResponse sidResponse = response.body();
                if(sidResponse != null){
                    if(sidResponse.getErrorCode() == 0){
                        String sId = sidResponse.getSID();

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
                        else{
                            progressDialog.setMessage(getString(R.string.progres_msg_get_info_client));
                            getClientInfo(sId,phones,pass);
                        }

                    }
                    else{
                        String msg = RemoteException.getServiceException(sidResponse.getErrorCode());
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
                            auth(phones, pass);
                        }))
                        .show();
            }
        });
    }

    private void getClientInfo(String sid, String phon, String pass){

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<GetClientInfoResponse> call = commandServices.getClientInfo(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), sid);

        call.enqueue(new Callback<GetClientInfoResponse>() {
            @Override
            public void onResponse(Call<GetClientInfoResponse> call, Response<GetClientInfoResponse> response) {
                GetClientInfoResponse clientInfoResponse = response.body();
                if(clientInfoResponse != null && clientInfoResponse.getErrorCode() == 0){
                    try {
                        byte[] secPh = encrypt(phon.getBytes(), BaseApp.getAppInstance().getHuyYou());
                        byte[] secPa = encrypt(pass.getBytes(), BaseApp.getAppInstance().getHuyYou());

                        Client client = clientInfoResponse.getClient();
                        client.setPassword(secPa);
                        client.setPhone(secPh);
                        client.setSid(sid);
                        client.setTypeClient(BaseEnum.PersoanaFizica);

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
                            getClientInfo(sid,phon,pass);
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
