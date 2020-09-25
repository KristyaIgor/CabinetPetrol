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

import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.petrolcabinet.BaseApp;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.bottomsheet.AddCardBottomSheetDialog;
import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.remoteSettings.ApiUtils;
import edi.md.petrolcabinet.remoteSettings.CommandServices;
import edi.md.petrolcabinet.remoteSettings.RemoteException;
import edi.md.petrolcabinet.remote.authenticate.AuthenticateCard;
import edi.md.petrolcabinet.remote.response.SIDResponseCard;
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
    TextInputEditText numberCard, numberPhone;
    TextInputLayout numberLayout, phoneLayout;

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
        phoneLayout = rootView.findViewById(R.id.editTextnumberPoneLayout);
        numberLayout = rootView.findViewById(R.id.editTextpasswordsLayout);
        signInCabinet = rootView.findViewById(R.id.buttonSignInCabinetForCard);
        progressDialog = new ProgressDialog(getContext(),  R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);

        mRealm = Realm.getDefaultInstance();

        if(BaseApp.getAppInstance().isPassExpired()){
            numberPhone.setText(BaseApp.getAppInstance().getNumberPhone());
            numberCard.setText(BaseApp.getAppInstance().getNumberCod());

            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.progres_msg_authenticate_card));
            progressDialog.show();

            AuthenticateCard authenticateCard = new AuthenticateCard();
            authenticateCard.setCardNumber(numberCard.getText().toString());
            authenticateCard.setPhone(numberPhone.getText().toString());

            CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
            Call<SIDResponseCard> cardCall = commandServices.authenticateCard(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), authenticateCard);

            enqueueRequest(cardCall);
        }

        nextStep.setOnClickListener(view -> {
            String phone = numberPhone.getText().toString();
            String numberC = numberCard.getText().toString();

            if (phone.equals("") &&  numberC.equals("")){
                phoneLayout.setError(getString(R.string.error_input_number_phone));
                numberLayout.setError(getString(R.string.error_input_number_card));
            }
            else{
                if(phone.equals("") || numberC.equals("")){
                    if(phone.equals(""))
                        phoneLayout.setError(getString(R.string.error_input_number_phone));
                    if (numberC.equals(""))
                        numberLayout.setError(getString(R.string.error_input_number_card));
                }
                else{
                    byte[] secPhone = new byte[0];
                    try {
                        secPhone = encrypt(numberPhone.getText().toString().getBytes(), BaseApp.getAppInstance().getHuyYou());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    RealmResults<Accounts> realmResults = mRealm.where(Accounts.class)
                            .equalTo("code", numberCard.getText().toString())
                            .and()
                            .equalTo("phone", secPhone)
                            .and()
                            .equalTo("companyId", BaseApp.getAppInstance().getCompanyClicked().getId())
                            .findAll();

                    if (realmResults.isEmpty()) {

                        progressDialog.setIndeterminate(false);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage(getString(R.string.progres_msg_authenticate_card));
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
                            progressDialog.setMessage(getString(R.string.progres_msg_authenticate_card));
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
                                    .setTitle(getString(R.string.oops_text)      )
                                    .setMessage(getString(R.string.dialog_msg_this_card_exist))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.dialog_btn_ok), (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    })
                                    .show();
                        }

                    }
                }
            }
        });

        signInCabinet.setOnClickListener(view -> {
            FragmentCabinetsAndCards.dissmisAddCardDialogAndConnectLogin();
        });

        numberPhone.addTextChangedListener(new TextWatcher() {
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
        numberCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    numberLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
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
            public void onFailure(Call<SIDResponseCard> call, Throwable t) {
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
                            progressDialog.setMessage(getString(R.string.progres_msg_authenticate_card));
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
