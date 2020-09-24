package edi.md.petrolcabinet.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.petrolcabinet.BaseApp;
import edi.md.petrolcabinet.CardAccountActivity;
import edi.md.petrolcabinet.FizicAccountActivity;
import edi.md.petrolcabinet.JuridicAccountActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.adapters.ClientsRealmAdapter;
import edi.md.petrolcabinet.bottomsheet.AddCardBottomSheetDialog;
import edi.md.petrolcabinet.bottomsheet.SignInBottomSheetDialog;
import edi.md.petrolcabinet.realm.objects.Accounts;
import edi.md.petrolcabinet.realm.objects.Company;
import edi.md.petrolcabinet.remote.ApiUtils;
import edi.md.petrolcabinet.remote.CommandServices;
import edi.md.petrolcabinet.remote.RemoteException;
import edi.md.petrolcabinet.remote.authenticate.AuthenticateCard;
import edi.md.petrolcabinet.remote.authenticate.AuthenticateUserBody;
import edi.md.petrolcabinet.remote.cardDetailInfo.CardItem;
import edi.md.petrolcabinet.remote.cardDetailInfo.GetCardDetailInfoResponse;
import edi.md.petrolcabinet.remote.client.Client;
import edi.md.petrolcabinet.remote.client.ContractInClient;
import edi.md.petrolcabinet.remote.client.GetClientInfoResponse;
import edi.md.petrolcabinet.remote.client.UnpaidDocument;
import edi.md.petrolcabinet.remote.response.SIDResponse;
import edi.md.petrolcabinet.remote.response.SIDResponseCard;
import edi.md.petrolcabinet.utils.BaseEnum;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Igor on 30.06.2020
 */

public class FragmentCabinetsAndCards extends Fragment {

    static ConstraintLayout layoutListContracts, layoutAddContract;

    static ListView list_contracts;

    Button addCabinet, addCard;
    static ImageView imageCompany;
    static TextView textTitle;

    static SignInBottomSheetDialog loginForm;
    static AddCardBottomSheetDialog cardForm;

    static Context context;
    static Company company;

    static ClientsRealmAdapter adapter;

    static ProgressDialog progressDialog;

    TextView addNewContract;

    static Realm mRealm;

    CommandServices commandServices;

    static FragmentManager parentFragmentManager;
    AlertDialog  checkFizicDialog,checkJuridicDialog, checkCardDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootViewAdmin = inflater.inflate(R.layout.fragment_contracts_v0, container, false);

        list_contracts = rootViewAdmin.findViewById(R.id.list_contracts);
        layoutAddContract = rootViewAdmin.findViewById(R.id.layoutAdd);
        layoutListContracts = rootViewAdmin.findViewById(R.id.layout_list_contracts);
        addNewContract = rootViewAdmin.findViewById(R.id.textView_add_contract);
        addCabinet = rootViewAdmin.findViewById(R.id.buttonAddCabinetPersonal);
        addCard = rootViewAdmin.findViewById(R.id.buttonAddFuelCard);
        imageCompany = rootViewAdmin.findViewById(R.id.image_company_logon);
        textTitle = rootViewAdmin.findViewById(R.id.text_message_welcome);

        mRealm = Realm.getDefaultInstance();
        context = getActivity();
        progressDialog = new ProgressDialog(context, R.style.ThemeOverlay_AppCompat_Dialog_Alert_TestDialogTheme);
        company = BaseApp.getAppInstance().getCompanyClicked();

        commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());

         parentFragmentManager = getParentFragmentManager();

        list_contracts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Accounts client = adapter.getItem(i);

                progressDialog.setMessage(getString(R.string.progres_msg_loading));
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();

                if (client.getTypeClient() == BaseEnum.PerCard) {
                    getCardInfo(client);
                } else {
                    getClientInfo(client);
                }
            }
        });

        addCabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginForm = SignInBottomSheetDialog.newInstance();
                loginForm.show(getParentFragmentManager(), SignInBottomSheetDialog.TAG);
            }
        });

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardForm = AddCardBottomSheetDialog.newInstance();
                cardForm.show(getParentFragmentManager(), AddCardBottomSheetDialog.TAG);
            }
        });

        addNewContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View dialogView = inflater.inflate(R.layout.dialog_add_cabinet_or_card, null);

                AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setView(dialogView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ImageView logo = dialogView.findViewById(R.id.dialog_image_company_logon);
                TextView title = dialogView.findViewById(R.id.dialog_text_message_welcome);
                Button addCardDialog = dialogView.findViewById(R.id.dialog_buttonAddFuelCard);
                Button addCabinteDialog = dialogView.findViewById(R.id.dialog_buttonAddCabinetPersonal);

                byte[] decodedString = Base64.decode(company.getLogo(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                logo.setImageBitmap(decodedByte);
                title.setText(getString(R.string.message_dialog_login) + company.getName() + "!");

                addCabinteDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        loginForm = SignInBottomSheetDialog.newInstance();
                        loginForm.show(getParentFragmentManager(), SignInBottomSheetDialog.TAG);
                    }
                });

                addCardDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        cardForm = AddCardBottomSheetDialog.newInstance();
                        cardForm.show(getParentFragmentManager(), AddCardBottomSheetDialog.TAG);
                    }
                });

                dialog.show();
            }
        });

        return rootViewAdmin;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateListClients();
    }



    private void getCardInfo(Accounts client) {
        AuthenticateCard authenticateCard = new AuthenticateCard();
        authenticateCard.setCardNumber(client.getCode());
        authenticateCard.setPhone(decrypt(client.getPhone(), BaseApp.getAppInstance().getHuyYou()));
        authenticateCard.setPassword(decrypt(client.getPin(), BaseApp.getAppInstance().getHuyYou()));

        String sid = client.getSid();
        String id = client.getCardId();

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<GetCardDetailInfoResponse> cardCall = commandServices.getCardDetailInfo(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), sid, id,"0");

        cardCall.enqueue(new Callback<GetCardDetailInfoResponse>() {
            @Override
            public void onResponse(Call<GetCardDetailInfoResponse> call, Response<GetCardDetailInfoResponse> response) {
                GetCardDetailInfoResponse cardInfoResponse = response.body();

                if (cardInfoResponse != null) {
                    if (cardInfoResponse.getErrorCode() == 0) {

                        CardItem card = cardInfoResponse.getCard();
                        card.setBalanceAccount(cardInfoResponse.getBalanceAccount());
                        card.setTransactions(cardInfoResponse.getTransactions());
                        card.setClient(cardInfoResponse.getClient());
                        card.setContract(cardInfoResponse.getContract());
                        card.setContractValidFrom(cardInfoResponse.getContractValidFrom());
                        card.setContractValidTo(cardInfoResponse.getContractValidTo());
                        card.setSID(client.getSid());

                        mRealm.executeTransaction(realm -> {
                            client.setBalance(cardInfoResponse.getBalanceAccount());
                            client.setId(card.getID());
                        });

                        BaseApp.getAppInstance().setCardAccount(card);
                        BaseApp.getAppInstance().setClientClicked(client);

                        startActivity(new Intent(context, CardAccountActivity.class));

                    } else if (cardInfoResponse.getErrorCode() == 5) {
                        reAuthCard(authenticateCard, client);
                    } else {
                        progressDialog.dismiss();
                        String msg = RemoteException.getServiceException(cardInfoResponse.getErrorCode());

                        new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogCustom)
                                .setTitle(getString(R.string.oops_text)      )
                                .setMessage(msg)
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                })
                                .show();
                    }
                } else {
                    progressDialog.dismiss();

                    new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogCustom)
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
            public void onFailure(Call<GetCardDetailInfoResponse> call, Throwable t) {
                progressDialog.dismiss();

                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
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

                            getCardInfo(client);
                        }))
                        .show();
            }
        });

    }

    private void getClientInfo(Accounts client) {
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

        String sid = client.getSid();

        Call<GetClientInfoResponse> call = commandServices.getClientInfo(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), sid);

        call.enqueue(new Callback<GetClientInfoResponse>() {
            @Override
            public void onResponse(Call<GetClientInfoResponse> call, Response<GetClientInfoResponse> response) {
                GetClientInfoResponse clientInfoResponse = response.body();
                if (clientInfoResponse != null) {
                    if (clientInfoResponse.getErrorCode() == 0) {
                        mRealm.executeTransaction(realm -> {
                            Client client1 = clientInfoResponse.getClient();
                            client1.setSid(sid);
                            updateClient(client, client1);
                        });
                    } else if (clientInfoResponse.getErrorCode() == 5) {
                        reAuth(user, client);
                    } else {
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
                } else {
                    progressDialog.dismiss();

                    new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
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
                            progressDialog.setMessage(getString(R.string.progres_msg_loading));
                            progressDialog.setCancelable(false);
                            progressDialog.setIndeterminate(true);
                            progressDialog.show();

                            getClientInfo(client);
                        }))
                        .show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }

    private void reAuthCard(AuthenticateCard authenticateCard, Accounts client) {
        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<SIDResponseCard> cardCall = commandServices.authenticateCard(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), authenticateCard);

        cardCall.enqueue(new Callback<SIDResponseCard>() {
            @Override
            public void onResponse(Call<SIDResponseCard> call, Response<SIDResponseCard> response) {
                SIDResponseCard sidResponse = response.body();

                if(sidResponse != null){
                    if(sidResponse.getErrorCode() == 0){
                        if(sidResponse.getSID() != null){
                            mRealm.executeTransaction(realm -> {
                                client.setSid(sidResponse.getSID());
                            });

                            getCardInfo(client);
                        }
                        else{
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

                    }
                    else if(sidResponse.getErrorCode() == 37){
                        progressDialog.dismiss();

                        BaseApp.getAppInstance().setNumberCod(client.getCode());
                        BaseApp.getAppInstance().setNumberPhone(decrypt(client.getPhone(), BaseApp.getAppInstance().getHuyYou()));
                        BaseApp.getAppInstance().setPassExpired(true);

                        cardForm = AddCardBottomSheetDialog.newInstance();
                        cardForm.show(getParentFragmentManager(), AddCardBottomSheetDialog.TAG);
                    }
                    else if(sidResponse.getErrorCode() == 9){
                        progressDialog.dismiss();

                        final View customLayout = getLayoutInflater().inflate(R.layout.fragment_check_card_data, null);

                        checkCardDialog = new MaterialAlertDialogBuilder(getContext(),R.style.MaterialAlertDialogCustom).create();
                        checkCardDialog.setTitle(getString(R.string.dialog_title_card_incorect_phone));
                        checkCardDialog.setMessage(getString(R.string.dialog_msg_check_card_number_phone));
                        checkCardDialog.setView(customLayout);
                        checkCardDialog.setCancelable(false);

                        MaterialButton btnCheck = customLayout.findViewById(R.id.button_check_card);
                        MaterialButton btnCancel = customLayout.findViewById(R.id.button_cancel_card);
                        TextInputEditText etPhone = customLayout.findViewById(R.id.editTextPhoneCardCheck);
                        TextInputLayout phoneLayout = customLayout.findViewById(R.id.editTextPhoneCardPLayout);

                        etPhone.addTextChangedListener(new TextWatcher() {
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

                        btnCheck.setOnClickListener(view -> {
                            String phoneNumber = etPhone.getText().toString();

                            if(phoneNumber.equals("")){
                                phoneLayout.setError(getString(R.string.error_input_number_phone));
                            }
                            else {
                                byte[] byePhone = new byte[0];
                                try {
                                    byePhone = encrypt(phoneNumber.getBytes(), BaseApp.getAppInstance().getHuyYou());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                byte[] finalByePhone = byePhone;
                                mRealm.executeTransaction(realm -> {
                                    client.setPhone(finalByePhone);
                                });

                                AuthenticateCard authenticateCard = new AuthenticateCard();
                                authenticateCard.setCardNumber(client.getCode());
                                authenticateCard.setPhone(phoneNumber);
                                authenticateCard.setPassword(decrypt(client.getPin(),BaseApp.getAppInstance().getHuyYou()));

                                //checkFizic.dismiss();

                                progressDialog.setMessage(getString(R.string.progres_msg_loading));
                                progressDialog.setCancelable(false);
                                progressDialog.setIndeterminate(true);
                                progressDialog.show();

                                reAuthCardDialog(authenticateCard, client);
                            }
                        });

                        btnCancel.setOnClickListener(view1 -> {
                            checkCardDialog.dismiss();
                        });

                        checkCardDialog.show();

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
                            reAuthCard(authenticateCard,client);
                        }))
                        .show();
            }
        });
    }

    private void reAuthCardDialog(AuthenticateCard authenticateCard, Accounts client) {
        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<SIDResponseCard> cardCall = commandServices.authenticateCard(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), authenticateCard);

        cardCall.enqueue(new Callback<SIDResponseCard>() {
            @Override
            public void onResponse(Call<SIDResponseCard> call, Response<SIDResponseCard> response) {
                SIDResponseCard sidResponse = response.body();

                if(sidResponse != null){
                    if(sidResponse.getErrorCode() == 0){
                        if(sidResponse.getSID() != null){
                            mRealm.executeTransaction(realm -> {
                                client.setSid(sidResponse.getSID());
                            });
                            if(checkCardDialog != null && checkCardDialog.isShowing())
                                checkCardDialog.dismiss();

                            getCardInfo(client);
                        }
                        else{
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

                    }
                    else if(sidResponse.getErrorCode() == 37){
                        progressDialog.dismiss();

                        BaseApp.getAppInstance().setNumberCod(client.getCode());
                        BaseApp.getAppInstance().setNumberPhone(decrypt(client.getPhone(), BaseApp.getAppInstance().getHuyYou()));
                        BaseApp.getAppInstance().setPassExpired(true);

                        if(checkCardDialog != null && checkCardDialog.isShowing())
                            checkCardDialog.dismiss();

                        cardForm = AddCardBottomSheetDialog.newInstance();
                        cardForm.show(getParentFragmentManager(), AddCardBottomSheetDialog.TAG);
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
                            reAuthCard(authenticateCard,client);
                        }))
                        .show();
            }
        });
    }

    private void reAuth(AuthenticateUserBody user, Accounts client) {
        Call<SIDResponse> call = commandServices.authenticateUser(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), user);

        call.enqueue(new Callback<SIDResponse>() {
            @Override
            public void onResponse(Call<SIDResponse> call, Response<SIDResponse> response) {
                SIDResponse sidResponse = response.body();
                if (sidResponse != null) {
                    if (sidResponse.getErrorCode() == 0) {
                        String sId = sidResponse.getSID();
                        if(sId != null){
                            mRealm.executeTransaction(realm -> {
                                client.setSid(sId);
                            });

                            getClientInfo(client);
                        }
                        else{
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
                    }
                    else if(sidResponse.getErrorCode() == 4){
                        progressDialog.dismiss();

                        if(client.getTypeClient() == BaseEnum.PersoanaFizica){
                            final View customLayout = getLayoutInflater().inflate(R.layout.fragment_check_fizic_data, null);

                            checkFizicDialog = new MaterialAlertDialogBuilder(getContext(),R.style.MaterialAlertDialogCustom).create();
                            checkFizicDialog.setTitle(getString(R.string.title_dialog_incorect_log_data_fizic));
                            checkFizicDialog.setMessage(getString(R.string.dialog_msg_check_log_data));
                            checkFizicDialog.setView(customLayout);
                            checkFizicDialog.setCancelable(false);

                            MaterialButton btnCheck = customLayout.findViewById(R.id.button_check);
                            MaterialButton btnCancel = customLayout.findViewById(R.id.button_cancel);
                            TextInputEditText etLogin = customLayout.findViewById(R.id.editTextPhoneLoginCheck);
                            TextInputEditText etPass = customLayout.findViewById(R.id.editTextTextPasswordLoginCheck);
                            TextInputLayout phoneLayout = customLayout.findViewById(R.id.editTextPhoneLoginPLayout);
                            TextInputLayout passwordLayout = customLayout.findViewById(R.id.editTextTextPasswordLoginLayout);

                            etLogin.addTextChangedListener(new TextWatcher() {
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
                            etPass.addTextChangedListener(new TextWatcher() {
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

                            btnCheck.setOnClickListener(view -> {
                                String phoneNumber = etLogin.getText().toString();
                                String passwords = etPass.getText().toString();

                                if(phoneNumber.equals("") && passwords.equals("")){
                                    phoneLayout.setError(getString(R.string.error_input_number_phone));
                                    passwordLayout.setError(getString(R.string.error_input_password));
                                }
                                else {
                                    if (phoneNumber.equals("") || passwords.equals("")) {
                                        if (phoneNumber.equals("")) {
                                            passwordLayout.setError(getString(R.string.error_input_number_phone));
                                        }
                                        if (passwords.equals("")) {
                                            passwordLayout.setError(getString(R.string.error_input_password));
                                        }
                                    } else {
                                        byte[] byePhone = new byte[0];
                                        byte[] bytePass = new byte[0];
                                        try {
                                            byePhone = encrypt(phoneNumber.getBytes(), BaseApp.getAppInstance().getHuyYou());
                                            bytePass = encrypt(passwords.getBytes(), BaseApp.getAppInstance().getHuyYou());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        byte[] finalByePhone = byePhone;
                                        byte[] finalBytePass = bytePass;
                                        mRealm.executeTransaction(realm -> {
                                            client.setPhone(finalByePhone);
                                            client.setPassword(finalBytePass);
                                        });

                                        AuthenticateUserBody user_new = new AuthenticateUserBody();
                                        user_new.setPassword(passwords);
                                        user_new.setPhone(phoneNumber);
                                        user_new.setAuthType(0);

                                        //checkFizic.dismiss();

                                        progressDialog.setMessage(getString(R.string.progres_msg_loading));
                                        progressDialog.setCancelable(false);
                                        progressDialog.setIndeterminate(true);
                                        progressDialog.show();

                                        reAuthDialog(user_new, client);
                                    }
                                }
                            });

                            btnCancel.setOnClickListener(view1 -> {
                                checkFizicDialog.dismiss();
                            });

                            checkFizicDialog.show();
                        }
                        else{
                            final View customLayout = getLayoutInflater().inflate(R.layout.fragment_check_juridic_data, null);

                            checkJuridicDialog = new MaterialAlertDialogBuilder(getContext(),R.style.MaterialAlertDialogCustom).create();
                            checkJuridicDialog.setTitle(getString(R.string.title_dialog_incorect_log_data_juridic));
                            checkJuridicDialog.setMessage(getString(R.string.dialog_msg_check_log_data));
                            checkJuridicDialog.setView(customLayout);
                            checkJuridicDialog.setCancelable(false);

                            MaterialButton btnCheck = customLayout.findViewById(R.id.button_check_juridic);
                            MaterialButton btnCancel = customLayout.findViewById(R.id.button_cancel_juridic);
                            TextInputEditText userName = customLayout.findViewById(R.id.editTextUserName);
                            TextInputEditText passwords = customLayout.findViewById(R.id.editTextTextPassword);
                            TextInputLayout layoutUserName = customLayout.findViewById(R.id.editTextUserNameLayout);
                            TextInputLayout layoutPassword = customLayout.findViewById(R.id.editTextTextPasswordLayout);


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

                            btnCheck.setOnClickListener(view -> {
                                String userN = userName.getText().toString();
                                String password = passwords.getText().toString();

                                if (userN.equals("") && password.equals("")){
                                    layoutUserName.setError(getString(R.string.error_input_username));
                                    layoutPassword.setError(getString(R.string.error_input_password));
                                }
                                else{
                                    if(userN.equals("") || password.equals("")){
                                        if (userN.equals("")){
                                            layoutUserName.setError(getString(R.string.error_input_username));
                                        }
                                        if (password.equals("")){
                                            layoutPassword.setError(getString(R.string.error_input_password));
                                        }
                                    }
                                    else{
                                        byte[] secUser = new byte[0];
                                        byte[] secPa = new byte[0];
                                        try {
                                            secUser = encrypt(userN.getBytes(), BaseApp.getAppInstance().getHuyYou());
                                            secPa = encrypt(password.getBytes(), BaseApp.getAppInstance().getHuyYou());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        byte[] finalSecUser = secUser;
                                        byte[] finalSecPa = secPa;
                                        mRealm.executeTransaction(realm -> {
                                            client.setPassword(finalSecPa);
                                            client.setUserName(finalSecUser);
                                        });

                                        AuthenticateUserBody user_new = new AuthenticateUserBody();

                                        user_new.setPassword(password);
                                        user_new.setUser(userN);
                                        user_new.setIDNO(client.getIDNP());
                                        user_new.setAuthType(1);

                                        //checkFizic.dismiss();

                                        progressDialog.setMessage(getString(R.string.progres_msg_loading));
                                        progressDialog.setCancelable(false);
                                        progressDialog.setIndeterminate(true);
                                        progressDialog.show();

                                        reAuthDialog(user_new, client);
                                    }
                                }
                            });

                            btnCancel.setOnClickListener(view1 -> {
                                checkJuridicDialog.dismiss();
                            });

                            checkJuridicDialog.show();
                        }
                    }
                    else {
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
                } else {
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
                            progressDialog.setMessage(getString(R.string.progres_msg_loading));
                            progressDialog.setCancelable(false);
                            progressDialog.setIndeterminate(true);
                            progressDialog.show();

                            reAuth(user,client);
                        }))
                        .show();
            }
        });
    }

    private void reAuthDialog(AuthenticateUserBody user, Accounts client) {
        Call<SIDResponse> call = commandServices.authenticateUser(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), user);

        call.enqueue(new Callback<SIDResponse>() {
            @Override
            public void onResponse(Call<SIDResponse> call, Response<SIDResponse> response) {
                SIDResponse sidResponse = response.body();
                if (sidResponse != null) {
                    if (sidResponse.getErrorCode() == 0) {
                        String sId = sidResponse.getSID();
                        if(sId != null){
                            if(checkFizicDialog != null)
                                if(checkFizicDialog.isShowing())
                                    checkFizicDialog.dismiss();
                            if(checkJuridicDialog != null)
                                if(checkJuridicDialog.isShowing())
                                    checkJuridicDialog.dismiss();

                            mRealm.executeTransaction(realm -> {
                                client.setSid(sId);
                            });
                            progressDialog.dismiss();
                            progressDialog.setMessage(getString(R.string.progres_msg_loading));
                            progressDialog.setCancelable(false);
                            progressDialog.setIndeterminate(true);
                            progressDialog.show();
                            getClientInfo(client);
                        }
                        else{
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
                    }
                    else {
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
                } else {
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
                            progressDialog.setMessage(getString(R.string.progres_msg_loading));
                            progressDialog.setCancelable(false);
                            progressDialog.setIndeterminate(true);
                            progressDialog.show();

                            reAuth(user,client);
                        }))
                        .show();
            }
        });
    }

    public static void dismissDialog() {
        if (loginForm != null && loginForm.getShowsDialog()) {
            loginForm.dismiss();
        }
        if (cardForm != null && cardForm.getShowsDialog()) {
            cardForm.dismiss();
        }
    }

    public static void dissmisAddCardDialogAndConnectLogin(){
        if (cardForm != null && cardForm.getShowsDialog()) {
            cardForm.dismiss();
        }
        loginForm = SignInBottomSheetDialog.newInstance();
        loginForm.show(parentFragmentManager, SignInBottomSheetDialog.TAG);

    }

    public static void addedNewClient(Client client) {

        Accounts newClient = copyClientToRealm(client);

        mRealm.executeTransaction(realm -> {
            realm.insert(newClient);
        });

        dismissDialog();
        updateListClients();
    }

    public static void addedNewCard(CardItem card) {

        Accounts newClient = copyCardToRealm(card);
        mRealm.executeTransaction(realm -> {
            realm.insert(newClient);
        });

        dismissDialog();
        updateListClients();
    }



    public static void updateListClients() {
        RealmResults<Accounts> results = mRealm.where(Accounts.class).equalTo("companyId", company.getId()).findAll();
        if (results.isEmpty()) {
            layoutAddContract.setVisibility(View.VISIBLE);
            byte[] decodedString = Base64.decode(company.getLogo(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageCompany.setImageBitmap(decodedByte);
            textTitle.setText(context.getResources().getString(R.string.message_dialog_login) + company.getName() + "!");
            layoutListContracts.setVisibility(View.GONE);
        } else {
            layoutAddContract.setVisibility(View.GONE);
            layoutListContracts.setVisibility(View.VISIBLE);

            adapter = new ClientsRealmAdapter(context, results);
            list_contracts.setAdapter(adapter);
        }

    }

    private static Accounts copyClientToRealm(Client client) {
        Accounts realmClient = new Accounts();

        realmClient.setId(client.getId());
        realmClient.setTypeClient(client.getTypeClient());
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
        RealmList<ContractInClient> docs = new RealmList<>();
        docs.addAll(list2);
        realmClient.setContracts(docs);

        if (client.getTypeClient() == BaseEnum.PersoanaFizica) {
            realmClient.setPassword(client.getPassword());
            realmClient.setPhone(client.getPhone());
            realmClient.setSid(client.getSid());
        } else {
            realmClient.setPassword(client.getPassword());
            realmClient.setUserName(client.getUserName());
            realmClient.setSid(client.getSid());
        }
        return realmClient;
    }

    private static Accounts copyCardToRealm(CardItem cardItem) {
        //per card
        Accounts realmClient = new Accounts();
        realmClient.setCompanyId(company.getId());
        realmClient.setName(cardItem.getName());
        realmClient.setCode(cardItem.getCode());
        realmClient.setPhone(cardItem.getPhone());
        realmClient.setPin(cardItem.getPin());
        realmClient.setTypeClient(BaseEnum.PerCard);
        realmClient.setBalance(cardItem.getBalanceAccountCard());
        realmClient.setId(cardItem.getID());
        realmClient.setSid(cardItem.getSID());

        return realmClient;
    }

    private void updateClient(Accounts realmClient, Client client) {
        realmClient.setId(client.getId());
        realmClient.setSid(client.getSid());
        realmClient.setAmount(client.getAmount());
        realmClient.setBalance(client.getBalance());
        realmClient.setCardsBalance(client.getCardsBalance());
        realmClient.setCredit(client.getCredit());
        realmClient.setName(client.getName());
        realmClient.setMasterBalance(client.getMasterBalance());
        realmClient.setOverdraft(client.getOverdraft());
        realmClient.setNonInvoicedConsumptionAmount(client.getNonInvoicedConsumptionAmount());
        realmClient.setUnpaidInvoiceConsumptionAmount(client.getUnpaidInvoiceConsumptionAmount());
        realmClient.setTotalDebtSum(client.getTotalDebtSum());
        realmClient.setStatus(client.getStatus());

        List<UnpaidDocument> list = client.getUnpaidDocuments();
        RealmList<UnpaidDocument> unpaidRealm = realmClient.getUnpaidDocuments();
        unpaidRealm.deleteAllFromRealm();
        unpaidRealm.addAll(list);
        realmClient.setUnpaidDocuments(unpaidRealm);

        List<ContractInClient> list1 = client.getContracts();
        RealmList<ContractInClient> unpaidRealm1 = realmClient.getContracts();
        unpaidRealm1.deleteAllFromRealm();
        unpaidRealm1.addAll(list1);
        realmClient.setContracts(unpaidRealm1);

        BaseApp.getAppInstance().setClientClicked(mRealm.copyFromRealm(realmClient));

        if (realmClient.getTypeClient() == 1){
            Intent juric = new Intent(getContext(), JuridicAccountActivity.class);
            startActivity(juric);
        }
        else if (realmClient.getTypeClient() == 0){
            Intent fizic = new Intent(getContext(), FizicAccountActivity.class);
            startActivity(fizic);
        }
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
