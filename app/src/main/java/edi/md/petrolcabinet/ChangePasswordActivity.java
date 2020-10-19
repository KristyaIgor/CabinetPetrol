package edi.md.petrolcabinet;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.remote.authenticate.AuthenticateUserBody;
import edi.md.petrolcabinet.remote.changePassword.ChangePasswordBody;
import edi.md.petrolcabinet.remote.response.SIDResponse;
import edi.md.petrolcabinet.remote.response.SimpleResponse;
import edi.md.petrolcabinet.remoteSettings.ApiUtils;
import edi.md.petrolcabinet.remoteSettings.CommandServices;
import edi.md.petrolcabinet.remoteSettings.RemoteException;
import edi.md.petrolcabinet.utils.BaseEnum;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextInputLayout layoutOldPass, layoutNewPass, layoutConfirmNewPass;
    TextInputEditText etOldPass, etNewPass, etConfirmNewPass;
    Button btnChange;

    Realm mRealm;
    Accounts clientRealm;
    ProgressDialog progressDialog;
    CommandServices commandServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);

        btnBack = findViewById(R.id.image_back_change_password);
        btnChange = findViewById(R.id.button_change_password);
        layoutOldPass = findViewById(R.id.layoutOldPassword);
        layoutNewPass = findViewById(R.id.layoutNewPassword);
        layoutConfirmNewPass = findViewById(R.id.layoutConfirmNewPassword);
        etOldPass = findViewById(R.id.editTextOldPass);
        etNewPass = findViewById(R.id.editTextNewPassword);
        etConfirmNewPass = findViewById(R.id.editTextConfirmNewPassword);
        progressDialog = new ProgressDialog(this);

        clientRealm = BaseApp.getAppInstance().getClientClicked();
        mRealm = Realm.getDefaultInstance();
        commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        btnChange.setOnClickListener(view -> {
            String oldPass = etOldPass.getText().toString();
            String newPass = etNewPass.getText().toString();
            String newPass2 = etConfirmNewPass.getText().toString();

            if(oldPass.equals("") && newPass.equals("") && newPass2.equals("")){
                layoutOldPass.setError(getString(R.string.error_input_old_pass));
                layoutNewPass.setError(getString(R.string.error_input_new_password));
                layoutConfirmNewPass.setError(getString(R.string.error_confirm_new_pass));
            }
            else {
                if(oldPass.equals("") || newPass.equals("") || newPass2.equals("")){
                    if(oldPass.equals(""))
                        layoutOldPass.setError(getString(R.string.error_input_old_pass));
                    if(newPass.equals(""))
                        layoutNewPass.setError(getString(R.string.error_input_new_password));
                    if(newPass2.equals(""))
                        layoutConfirmNewPass.setError(getString(R.string.error_confirm_new_pass));
                }
                else{
                    if(clientRealm.getTypeClient() == 0){
                        Accounts client = mRealm.where(Accounts.class).equalTo("phone",clientRealm.getPhone())
                                .and()
                                .equalTo("password", encrypt(oldPass.getBytes(),BaseApp.getAppInstance().getHuyYou()))
                                .findFirst();
                        if(client == null){
                            layoutOldPass.setError(getString(R.string.error_password_wrong));
                        }
                        else{
                            if(newPass.equals(newPass2)){
                                changePassword(client, oldPass, newPass);
                            }
                            else{
                                layoutConfirmNewPass.setError(getString(R.string.error_password_is_not_identical));
                            }
                        }
                    }
                    else{
                        Accounts client = mRealm.where(Accounts.class).equalTo("iDNP",clientRealm.getIDNP())
                                .equalTo("userName",clientRealm.getUserName())
                                .and()
                                .equalTo("password", encrypt(oldPass.getBytes(),BaseApp.getAppInstance().getHuyYou()))
                                .findFirst();
                        if(client == null){
                            layoutOldPass.setError(getString(R.string.error_password_wrong));
                        }
                        else{
                            if(newPass.equals(newPass2)){
                                changePassword(client, oldPass, newPass);
                            }
                            else{
                                layoutConfirmNewPass.setError(getString(R.string.error_password_is_not_identical));
                            }
                        }
                    }
                }
            }
        });

        etOldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    layoutOldPass.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    layoutNewPass.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etConfirmNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    layoutConfirmNewPass.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btnBack.setOnClickListener(view ->{
            finish();
        });
    }

    private void changePassword(Accounts client, String oldPass, String newPass){
        ChangePasswordBody body = new ChangePasswordBody();
        body.setOldPassword(oldPass);
        body.setNewPassword(newPass);
        body.setSID(clientRealm.getSid());

        progressDialog.setMessage(getString(R.string.progres_msg_change_password));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        Call<SimpleResponse> call = commandServices.changePassword(BaseApp.getAppInstance().getCompanyClicked().getServiceName(),body);

        enqueueCallChangePass(call, client, oldPass, newPass);
    }

    private void enqueueCallChangePass(Call<SimpleResponse> call, Accounts client, String oldPass, String newPass) {
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                SimpleResponse simpleResponse = response.body();
                if(simpleResponse != null){
                    if(simpleResponse.getErrorCode() == 0){
                        progressDialog.dismiss();

                        mRealm.executeTransaction(realm -> {
                            client.setPassword(encrypt(newPass.getBytes(),BaseApp.getAppInstance().getHuyYou()));
                        });
                        BaseApp.getAppInstance().setClientClicked(mRealm.copyFromRealm(client));

                        new MaterialAlertDialogBuilder(ChangePasswordActivity.this, R.style.MaterialAlertDialogCustom)
                                .setTitle(getString(R.string.dialog_title_ura))
                                .setMessage(getString(R.string.dialog_msg_password_is_updated))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.dialog_btn_ok), (dialogInterface, i) -> {
                                    finish();
                                })
                                .show();

                    }
                    else if(simpleResponse.getErrorCode() == 5){
                        reAuthCurrentPass(client,oldPass, newPass);
                    }
                    else{
                        String msg = RemoteException.getServiceException(simpleResponse.getErrorCode());
                        //progressDialog dismiss
                        progressDialog.dismiss();

                        new MaterialAlertDialogBuilder(ChangePasswordActivity.this, R.style.MaterialAlertDialogCustom)
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

                    new MaterialAlertDialogBuilder(ChangePasswordActivity.this, R.style.MaterialAlertDialogCustom)
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
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressDialog.dismiss();

                new MaterialAlertDialogBuilder(ChangePasswordActivity.this, R.style.MaterialAlertDialogCustom)
                        .setTitle(getString(R.string.oops_text)      )
                        .setMessage(getString(R.string.dialog_failure_service) + t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .setNegativeButton(getString(R.string.dialog_button_retry), ((dialogInterface, i) -> {
                            progressDialog.setMessage(getString(R.string.progres_msg_loading));
                            progressDialog.setCancelable(false);
                            progressDialog.setIndeterminate(true);
                            progressDialog.show();

                            changePassword(client, oldPass, newPass);
                        }))
                        .show();
            }
        });
    }

    private void reAuthCurrentPass(Accounts client, String oldPass, String newPass) {
        int types = client.getTypeClient();

        AuthenticateUserBody user = new AuthenticateUserBody();

        if (types == BaseEnum.PersoanaFizica) {
            user.setPassword(decrypt(client.getPassword(), BaseApp.getAppInstance().getHuyYou()));
            user.setPhone(decrypt(client.getPhone(), BaseApp.getAppInstance().getHuyYou()));
            user.setAuthType(0);
        } else if (types == BaseEnum.PersoanaJuridica) {
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

                if(sidResponse != null){
                    if(sidResponse.getErrorCode() == 0){
                        if(sidResponse.getSID() != null){
                            mRealm.executeTransaction(realm -> {
                                client.setSid(sidResponse.getSID());
                            });
                            changePassword(client, oldPass, newPass);
                        }
                        else{
                            progressDialog.dismiss();

                            new MaterialAlertDialogBuilder(ChangePasswordActivity.this,R.style.MaterialAlertDialogCustom)
                                    .setTitle(getString(R.string.oops_text)      )
                                    .setMessage(sidResponse.getErrorMessage())
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    })
                                    .show();
                        }

                    }
                    else{
                        progressDialog.dismiss();
                        String msg = RemoteException.getServiceException(sidResponse.getErrorCode());

                        new MaterialAlertDialogBuilder(ChangePasswordActivity.this,R.style.MaterialAlertDialogCustom)
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

                    new MaterialAlertDialogBuilder(ChangePasswordActivity.this,R.style.MaterialAlertDialogCustom)
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

                new MaterialAlertDialogBuilder(ChangePasswordActivity.this, R.style.MaterialAlertDialogCustom)
                        .setTitle(getString(R.string.oops_text)      )
                        .setMessage(getString(R.string.dialog_failure_service) + t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .setNegativeButton(getString(R.string.dialog_button_retry), ((dialogInterface, i) -> {
                            reAuthCurrentPass(client, oldPass, newPass);
                        }))
                        .show();
            }
        });
    }

    public static byte[] encrypt(byte[] plaintext, byte[] balabol) {
        byte[] IV = new byte[16];
        byte[] cipherText = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance("AES");

            SecretKeySpec keySpec = new SecretKeySpec(balabol, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            cipherText = cipher.doFinal(plaintext);

        } catch (BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
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
}