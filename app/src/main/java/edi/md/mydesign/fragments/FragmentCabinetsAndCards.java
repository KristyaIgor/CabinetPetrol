package edi.md.mydesign.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.mydesign.BaseApp;
import edi.md.mydesign.CardAccountActivity;
import edi.md.mydesign.FizicAccountActivity;
import edi.md.mydesign.JuridicAccountActivity;
import edi.md.mydesign.R;
import edi.md.mydesign.adapters.ClientsRealmAdapter;
import edi.md.mydesign.bottomsheet.AddCardBottomSheetDialog;
import edi.md.mydesign.bottomsheet.SignInBottomSheetDialog;
import edi.md.mydesign.realm.objects.ClientRealm;
import edi.md.mydesign.realm.objects.Company;
import edi.md.mydesign.remote.ApiUtils;
import edi.md.mydesign.remote.CommandServices;
import edi.md.mydesign.remote.RemoteException;
import edi.md.mydesign.remote.authenticate.AuthenticateCard;
import edi.md.mydesign.remote.authenticate.AuthenticateUserBody;
import edi.md.mydesign.remote.cardDetailInfo.CardItem;
import edi.md.mydesign.remote.cardDetailInfo.GetCardDetailInfoResponse;
import edi.md.mydesign.remote.client.Client;
import edi.md.mydesign.remote.client.ContractInClient;
import edi.md.mydesign.remote.client.GetClientInfoResponse;
import edi.md.mydesign.remote.client.UnpaidDocument;
import edi.md.mydesign.remote.response.SIDResponse;
import edi.md.mydesign.remote.response.SIDResponseCard;
import edi.md.mydesign.utils.BaseEnum;
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

        list_contracts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClientRealm client = adapter.getItem(i);

                progressDialog.setMessage("load information...");
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



    private void getCardInfo(ClientRealm client) {
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
                        card.setProductsLists(cardInfoResponse.getProductsList());
                        card.setClient(cardInfoResponse.getClient());
                        card.setContract(cardInfoResponse.getContract());
                        card.setContractValidFrom(cardInfoResponse.getContractValidFrom());
                        card.setContractValidTo(cardInfoResponse.getContractValidTo());
                        card.setSID(client.getSid());

                        mRealm.executeTransaction(realm -> {
                            client.setBalance(cardInfoResponse.getBalanceAccount());
                        });

                        BaseApp.getAppInstance().setCardAccount(cardInfoResponse.getCard());

                        startActivity(new Intent(context, CardAccountActivity.class));

                    } else if (cardInfoResponse.getErrorCode() == 5) {
                        reAuthCard(authenticateCard, client);
                    } else {
                        progressDialog.dismiss();
                        String msg = RemoteException.getServiceException(cardInfoResponse.getErrorCode());

                        new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogCustom)
                                .setTitle("Oops!")
                                .setMessage(msg)
                                .setCancelable(false)
                                .setPositiveButton("Am înţeles", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                })
                                .show();
                    }
                } else {
                    progressDialog.dismiss();

                    new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogCustom)
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
                            progressDialog.setMessage("load information...");
                            progressDialog.setCancelable(false);
                            progressDialog.setIndeterminate(true);
                            progressDialog.show();

                            getCardInfo(client);
                        }))
                        .show();
            }
        });

    }

    private void getClientInfo(ClientRealm client) {
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
                            updateClient(client, clientInfoResponse.getClient());
                        });
                    } else if (clientInfoResponse.getErrorCode() == 5) {
                        reAuth(user, client);
                    } else {
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
                } else {
                    progressDialog.dismiss();

                    new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
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
            public void onFailure(Call<GetClientInfoResponse> call, Throwable t) {
                progressDialog.dismiss();

                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                        .setTitle("Oops!")
                        .setMessage("Operația a eșuat!\nEroare: " + t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("Am înţeles", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .setNegativeButton("Reîncercați", ((dialogInterface, i) -> {
                            progressDialog.setMessage("load information...");
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

    private void reAuthCard(AuthenticateCard authenticateCard, ClientRealm client) {
        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<SIDResponseCard> cardCall = commandServices.authenticateCard(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), authenticateCard);

        cardCall.enqueue(new Callback<SIDResponseCard>() {
            @Override
            public void onResponse(Call<SIDResponseCard> call, Response<SIDResponseCard> response) {
                SIDResponseCard sidResponse = response.body();

                if(sidResponse != null){
                    if(sidResponse.getErrorCode() == 0){
                        mRealm.executeTransaction(realm -> {
                            client.setSid(sidResponse.getSID());
                        });

                        getCardInfo(client);
                    }
                    else if(sidResponse.getErrorCode() == 37){
                        progressDialog.dismiss();

                        BaseApp.getAppInstance().setNumberCod(client.getCode());
                        BaseApp.getAppInstance().setNumberPhone(decrypt(client.getPhone(), BaseApp.getAppInstance().getHuyYou()));
                        BaseApp.getAppInstance().setPassExpired(true);

                        cardForm = AddCardBottomSheetDialog.newInstance();
                        cardForm.show(getParentFragmentManager(), AddCardBottomSheetDialog.TAG);
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
                            reAuthCard(authenticateCard,client);
                        }))
                        .show();
            }
        });
    }

    private void reAuth(AuthenticateUserBody user, ClientRealm client) {
        Call<SIDResponse> call = commandServices.authenticateUser(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), user);

        call.enqueue(new Callback<SIDResponse>() {
            @Override
            public void onResponse(Call<SIDResponse> call, Response<SIDResponse> response) {
                SIDResponse sidResponse = response.body();
                if (sidResponse != null) {
                    if (sidResponse.getErrorCode() == 0) {
                        String sId = sidResponse.getSID();
                        mRealm.executeTransaction(realm -> {
                            client.setSid(sId);
                        });

                        getClientInfo(client);
                    } else {
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
                } else {
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

    public static void addedNewClient(Client client) {

        ClientRealm newClient = copyClientToRealm(client);

        mRealm.executeTransaction(realm -> {
            realm.insert(newClient);
        });

        dismissDialog();
        updateListClients();
    }

    public static void addedNewCard(CardItem card) {

        ClientRealm newClient = copyCardToRealm(card);
        mRealm.executeTransaction(realm -> {
            realm.insert(newClient);
        });

        dismissDialog();
        updateListClients();
    }



    public static void updateListClients() {
        RealmResults<ClientRealm> results = mRealm.where(ClientRealm.class).equalTo("companyId", company.getId()).findAll();
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

    private static ClientRealm copyClientToRealm(Client client) {
        ClientRealm realmClient = new ClientRealm();

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

    private static ClientRealm copyCardToRealm(CardItem cardItem) {
        //per card
        ClientRealm realmClient = new ClientRealm();
        realmClient.setCompanyId(company.getId());
        realmClient.setName(cardItem.getName());
        realmClient.setCode(cardItem.getCode());
        realmClient.setPhone(cardItem.getPhone());
        realmClient.setPin(cardItem.getPin());
        realmClient.setTypeClient(BaseEnum.PerCard);
        realmClient.setBalance(cardItem.getBalanceAccountCard());
        realmClient.setCardId(cardItem.getID());
        realmClient.setSid(cardItem.getSID());


        return realmClient;
    }

    private void updateClient(ClientRealm realmClient, Client client) {
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
