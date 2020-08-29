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
import edi.md.mydesign.remote.cardDetailInfo.CardItem;
import edi.md.mydesign.remote.cardDetailInfo.GetCardDetailInfoResponse;
import edi.md.mydesign.remote.response.SIDResponseCard;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Igor on 21.07.2020
 */

public class FragmentAddCardFinal extends Fragment {

    Button confirmPIN;
    EditText pinCod;

    TextView signInCabinet, textBackInit;

    ProgressDialog progressDialog;

    String cardId, sid;

    Realm mRealm;

    public static FragmentAddCardFinal newInstance() {
        return new FragmentAddCardFinal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_card_final, container, false);

        confirmPIN = rootView.findViewById(R.id.buttonConfirmPIN);
        pinCod = rootView.findViewById(R.id.editTextPinCod);
        signInCabinet = rootView.findViewById(R.id.buttonSignInCabinetForCardInFInal);
        textBackInit = rootView.findViewById(R.id.text_view_back_initial);
        progressDialog = new ProgressDialog(getContext(),  R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);

        mRealm = Realm.getDefaultInstance();

        confirmPIN.setOnClickListener(view -> {
            if(pinCod.getText().toString().equals("") || pinCod.getText().toString().length() < 4){

            }
            else{
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("confirmare pin...");
                progressDialog.show();

                AuthenticateCard authenticateCard = new AuthenticateCard();
                authenticateCard.setCardNumber(BaseApp.getAppInstance().getNumberCod());
                authenticateCard.setPhone(BaseApp.getAppInstance().getNumberPhone());
                authenticateCard.setPassword(pinCod.getText().toString());

                CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
                Call<SIDResponseCard> cardCall = commandServices.authenticateCard(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), authenticateCard);

                enqueueRequest(cardCall);
            }
        });

        textBackInit.setOnClickListener(view -> {
            AddCardBottomSheetDialog.setBeforeStep();
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
                        cardId = sidResponse.getCardID();
                        sid = sidResponse.getSID();

                        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
                        Call<GetCardDetailInfoResponse> cardInfoResponseCall = commandServices.getCardDetailInfo(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), sid,cardId,"0");

                        progressDialog.setMessage("obținerea informației despre card...");

                        enqueueGetCardRequest(cardInfoResponseCall);
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
                            progressDialog.setMessage("confirmare pin...");
                            progressDialog.show();

                            AuthenticateCard authenticateCard = new AuthenticateCard();
                            authenticateCard.setCardNumber(BaseApp.getAppInstance().getNumberCod());
                            authenticateCard.setPhone(BaseApp.getAppInstance().getNumberPhone());
                            authenticateCard.setPassword(pinCod.getText().toString());

                            CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
                            Call<SIDResponseCard> cardCall = commandServices.authenticateCard(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), authenticateCard);

                            enqueueRequest(cardCall);
                        }))
                        .show();
            }
        });
    }

    private void enqueueGetCardRequest (Call<GetCardDetailInfoResponse> call){
        call.enqueue(new Callback<GetCardDetailInfoResponse>() {
            @Override
            public void onResponse(Call<GetCardDetailInfoResponse> call, Response<GetCardDetailInfoResponse> response) {
                GetCardDetailInfoResponse cardInfoResponse = response.body();

                if(cardInfoResponse != null){
                    if(cardInfoResponse.getErrorCode() == 0){

                        try {
                            byte[] secPin = encrypt(pinCod.getText().toString().getBytes(), BaseApp.getAppInstance().getHuyYou());
                            byte[] secPhone = encrypt(BaseApp.getAppInstance().getNumberPhone().getBytes(), BaseApp.getAppInstance().getHuyYou());

                            CardItem card = cardInfoResponse.getCard();

                            card.setPin(secPin);
                            card.setPhone(secPhone);
                            card.setSID(sid);

                            progressDialog.dismiss();

                            if(BaseApp.getAppInstance().isPassExpired()){
                                RealmResults<ClientRealm> realmResults = mRealm.where(ClientRealm.class).equalTo("cardId",card.getID()).and().equalTo("companyId", BaseApp.getAppInstance().getCompanyClicked().getId()).findAll();
                                if(!realmResults.isEmpty()){
                                    ClientRealm client = realmResults.first();

                                    mRealm.executeTransaction(realm -> {
                                        client.setPin(card.getPin());
                                        client.setSid(card.getSID());
                                    });
                                }
                                FragmentCabinetsAndCards.dismissDialog();
                                FragmentCabinetsAndCards.updateListClients();
                            }
                            else{
                                FragmentCabinetsAndCards.addedNewCard(card);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        progressDialog.dismiss();
                        String msg = RemoteException.getServiceException(cardInfoResponse.getErrorCode());

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
            public void onFailure(Call<GetCardDetailInfoResponse> call, Throwable t) {
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
                            progressDialog.setMessage("obținerea informației despre card...");
                            progressDialog.show();

                            CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
                            Call<GetCardDetailInfoResponse> cardInfoResponseCall = commandServices.getCardDetailInfo(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), sid,cardId,"0");

                            enqueueGetCardRequest(cardInfoResponseCall);
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
