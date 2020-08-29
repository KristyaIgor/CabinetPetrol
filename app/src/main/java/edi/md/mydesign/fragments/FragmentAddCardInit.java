package edi.md.mydesign.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.mydesign.BaseApp;
import edi.md.mydesign.R;
import edi.md.mydesign.bottomsheet.AddCardBottomSheetDialog;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.RemoteException;
import edi.md.mydesign.remote.authenticate.AuthenticateCard;
import edi.md.mydesign.remote.response.SIDResponseCard;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Igor on 21.07.2020
 */

public class FragmentAddCardInit extends Fragment {

    Button nextStep;
    EditText numberCard, numberPhone;

    TextView signInCabinet;

    ProgressDialog progressDialog;

    Realm mRealm;

    public static FragmentAddCardInit newInstance() {
        return new FragmentAddCardInit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_card_init, container, false);

        nextStep = rootView.findViewById(R.id.button_nextStepAddCard);
        numberCard = rootView.findViewById(R.id.editText_number_card);
        numberPhone = rootView.findViewById(R.id.editText_number_phone);
        signInCabinet = rootView.findViewById(R.id.buttonSignInCabinetForCard);
        progressDialog = new ProgressDialog(getContext(),  R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);

        mRealm = Realm.getDefaultInstance();

        if(BaseApp.getAppInstance().isPassExpired()){
            numberPhone.setText(BaseApp.getAppInstance().getNumberPhone());
            numberCard.setText(BaseApp.getAppInstance().getNumberCod());
        }

        nextStep.setOnClickListener(view -> {
            if(numberPhone.getText().toString().equals("") || numberCard.getText().toString().equals("") || numberPhone.getText().toString().length() < 9){

            }
            else {
                byte[] secPhone = new byte[0];
                try {
                    secPhone = encrypt(numberPhone.getText().toString().getBytes(), BaseApp.getAppInstance().getHuyYou());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RealmResults<ClientRealm> realmResults = mRealm.where(ClientRealm.class)
                        .equalTo("code", numberCard.getText().toString())
                        .and()
                        .equalTo("phone", secPhone)
                        .and()
                        .equalTo("companyId", BaseApp.getAppInstance().getCompanyClicked().getId())
                        .findAll();

                if (realmResults.isEmpty()) {

                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("autentificare card...");
                    progressDialog.show();

                    AuthenticateCard authenticateCard = new AuthenticateCard();
                    authenticateCard.setCardNumber(numberCard.getText().toString());
                    authenticateCard.setPhone(numberPhone.getText().toString());

                    CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
                    Call<SIDResponseCard> cardCall = commandServices.authenticateCard(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), authenticateCard);

                    enqueueRequest(cardCall);
                }
                else{
                    if(BaseApp.getAppInstance().isPassExpired()){
                        progressDialog.setIndeterminate(false);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("autentificare card...");
                        progressDialog.show();

                        AuthenticateCard authenticateCard = new AuthenticateCard();
                        authenticateCard.setCardNumber(numberCard.getText().toString());
                        authenticateCard.setPhone(numberPhone.getText().toString());

                        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
                        Call<SIDResponseCard> cardCall = commandServices.authenticateCard(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), authenticateCard);

                        enqueueRequest(cardCall);
                    }
                    else{
                        new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                                .setTitle("Oops!")
                                .setMessage("Acest card există deja!")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                })
                                .show();
                    }

                }
            }
        });

        signInCabinet.setOnClickListener(view -> {

        });

        return rootView;
    }

    private void enqueueRequest (Call<SIDResponseCard> call){
        call.enqueue(new Callback<SIDResponseCard>() {
            @Override
            public void onResponse(Call<SIDResponseCard> call, Response<SIDResponseCard> response) {
                SIDResponseCard sidResponse = response.body();

                if(sidResponse != null){
                    if(sidResponse.getErrorCode() == 0){
                        BaseApp.getAppInstance().setNumberCod(numberCard.getText().toString());
                        BaseApp.getAppInstance().setNumberPhone(numberPhone.getText().toString());

                        progressDialog.dismiss();
                        AddCardBottomSheetDialog.setNextStep();
                    }
                    else{
                        progressDialog.dismiss();
                        String msg = RemoteException.getServiceException(sidResponse.getErrorCode());

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
            public void onFailure(Call<SIDResponseCard> call, Throwable t) {
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
                            progressDialog.setMessage("autentificare card...");
                            progressDialog.show();

                            AuthenticateCard authenticateCard = new AuthenticateCard();
                            authenticateCard.setCardNumber(numberCard.getText().toString());
                            authenticateCard.setPhone(numberPhone.getText().toString());

                            CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
                            Call<SIDResponseCard> cardCall = commandServices.authenticateCard(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), authenticateCard);

                            enqueueRequest(cardCall);
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
