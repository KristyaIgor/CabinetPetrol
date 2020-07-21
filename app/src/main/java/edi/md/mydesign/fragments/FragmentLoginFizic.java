package edi.md.mydesign.fragments;


import android.os.Bundle;
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

import java.util.List;

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
import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.remote.client.GetClientInfoResponse;
import edi.md.mydesign.remote.client.UnpaidDocument;
import edi.md.mydesign.remote.response.SIDResponse;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static edi.md.mydesign.fragments.FragmentContracts.addedNewClient;

/**
 * Created by Igor on 03.07.2020
 */

public class FragmentLoginFizic extends Fragment {

    EditText password, phone;
    Button signIn;
    TextView signUp;

    Realm mRealm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootViewAdmin = inflater.inflate(R.layout.fragment_login_fizic, container, false);

        phone = rootViewAdmin.findViewById(R.id.editTextPhoneLogin);
        password = rootViewAdmin.findViewById(R.id.editTextTextPasswordLogin);
        signIn = rootViewAdmin.findViewById(R.id.buttonSignInFizic);
        signUp = rootViewAdmin.findViewById(R.id.buttonSignUpFizic);

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
                String phoneNumber = phone.getText().toString();
                String passwords = password.getText().toString();
                byte[] terfs = new byte[0];
                try {
                    terfs = encrypt(phoneNumber.getBytes(),BaseApp.getAppInstance().getHuyYou());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RealmResults<ClientRealm> realmResults = mRealm.where(ClientRealm.class).equalTo("phone",terfs).and().equalTo("companyId", BaseApp.getAppInstance().getCompanyClicked().getId()).findAll();
                if(realmResults.isEmpty()){
                    auth(phoneNumber, passwords);
                }
                else{
                    for(ClientRealm clientRealm: realmResults){
                        String realmPhone = decrypt(clientRealm.getPhone(),BaseApp.getAppInstance().getHuyYou());
                        if(realmPhone.equals(phoneNumber)){
                            new MaterialAlertDialogBuilder(getContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                                    .setTitle("Oops!")
                                    .setMessage("Client with same phone already exist!")
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

    private void auth (String phones, String pass){
        AuthenticateUserBody user = new AuthenticateUserBody();
        user.setPassword(pass);
        user.setPhone(phones);
        user.setAuthType(0);

        String url = BaseApp.getAppInstance().getCompanyClicked().getAddress();
        CommandServices commandServices = ApiUtils.getCommandServices(url);
        Call<SIDResponse> call = commandServices.authenticateUser(user);

        call.enqueue(new Callback<SIDResponse>() {
            @Override
            public void onResponse(Call<SIDResponse> call, Response<SIDResponse> response) {
                SIDResponse sidResponse = response.body();
                if(sidResponse != null){
                    if(sidResponse.getErrorCode() == 0){
                        String sId = sidResponse.getSID();
                        getClientInfo(sId,phones,pass);
                    }
                }
            }

            @Override
            public void onFailure(Call<SIDResponse> call, Throwable t) {

            }
        });
    }

    private void getClientInfo(String sid, String phon, String pass){

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getAddress());
        Call<GetClientInfoResponse> call = commandServices.getClientInfo(sid);

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
                        client.setTypeClient(0);

                        FragmentContracts.addedNewClient(client,0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetClientInfoResponse> call, Throwable t) {

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
