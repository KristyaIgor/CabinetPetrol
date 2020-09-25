package edi.md.petrolcabinet.bottomsheet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edi.md.petrolcabinet.BaseApp;
import edi.md.petrolcabinet.DetailCompanyActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.fragments.FragmentCabinetsAndCards;
import edi.md.petrolcabinet.realm.objects.Company;
import edi.md.petrolcabinet.remoteSettings.ApiUtils;
import edi.md.petrolcabinet.remoteSettings.CommandServices;
import edi.md.petrolcabinet.remoteSettings.RemoteException;
import edi.md.petrolcabinet.remote.authenticate.AuthenticateUserBody;
import edi.md.petrolcabinet.remote.client.Client;
import edi.md.petrolcabinet.remote.client.GetClientInfoResponse;
import edi.md.petrolcabinet.remote.registerUser.RegisterUserBody;
import edi.md.petrolcabinet.remote.registerUser.RegisterUserResponse;
import edi.md.petrolcabinet.remote.response.SIDResponse;
import edi.md.petrolcabinet.remote.response.SimpleResponse;
import edi.md.petrolcabinet.utils.BaseEnum;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     SignInBottomSheetDialog.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class SignUpBottomSheetDialog extends BottomSheetDialogFragment {
    Company company;
    Button btn_signUp;

    TextInputLayout cardCodeLayout, emailLayout, firstLayout, lastLayout, idnpLayout, phoneLayout, passLayout;
    TextInputEditText cardCodeET, emailET, firstET, lastET, idnpET, phoneET, passET;

    ProgressDialog progressDialog;

    public static final String TAG = "ActionBottomDialogSignUp";

    CommandServices commandServices;

    RegisterUserBody body;
    Dialog dialog;

    // TODO: Customize parameters
    public static SignUpBottomSheetDialog newInstance() {
        return new SignUpBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.bottom_sheet_sign_up, container, false);

        ImageView logo = root.findViewById(R.id.image_company_logo_sign_up);
        TextView title = root.findViewById(R.id.title_user_sign_up);
        btn_signUp = root.findViewById(R.id.buttonSignUp);

        cardCodeLayout = root.findViewById(R.id.et_card_code_sign_up_Layout);
        emailLayout = root.findViewById(R.id.et_email_sign_up_Layout);
        firstLayout = root.findViewById(R.id.et_firts_name_sign_up_Layout);
        lastLayout = root.findViewById(R.id.et_last_name_sign_up_Layout);
        idnpLayout = root.findViewById(R.id.et_idnp_sign_up_Layout);
        phoneLayout = root.findViewById(R.id.et_phone_sign_up_Layout);
        passLayout = root.findViewById(R.id.et_password_sign_up_Layout);

        cardCodeET = root.findViewById(R.id.et_card_code_sign_up);
        emailET = root.findViewById(R.id.et_email_sign_up);
        firstET = root.findViewById(R.id.et_firts_name_sign_up);
        lastET = root.findViewById(R.id.et_last_name_sign_up);
        idnpET = root.findViewById(R.id.et_idnp_sign_up);
        phoneET = root.findViewById(R.id.et_phone_sign_up);
        passET = root.findViewById(R.id.et_password_sign_up);

        company = BaseApp.getAppInstance().getCompanyClicked();

        progressDialog = new ProgressDialog(getContext(),  R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);
        commandServices = ApiUtils.getCommandServices(company.getIp());

        byte[] decodedString = Base64.decode(company.getLogo(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        logo.setImageBitmap(decodedByte);
        title.setText(getString(R.string.welcome_message_comapny) + company.getName());

        btn_signUp.setOnClickListener(view -> {
            String card = cardCodeET.getText().toString();
            String email = emailET.getText().toString();
            String first = firstET.getText().toString();
            String last = lastET.getText().toString();
            String idnp = idnpET.getText().toString();
            String phone = phoneET.getText().toString();
            String pass = passET.getText().toString();

            if(card.equals("") && email.equals("") && first.equals("") && last.equals("") && idnp.equals("") && phone.equals("") && pass.equals("")){
                cardCodeLayout.setError(getString(R.string.error_input_number_card));
                emailLayout.setError(getString(R.string.error_input_email));
                firstLayout.setError(getString(R.string.error_input_first_name));
                lastLayout.setError(getString(R.string.error_input_last_name));
                idnpLayout.setError(getString(R.string.error_input_idnp));
                phoneLayout.setError(getString(R.string.error_input_number_phone));
                passLayout.setError(getString(R.string.error_input_password));
            }
            else{
                if (card.equals("") || email.equals("") || first.equals("") || last.equals("") || idnp.equals("") || phone.equals("") || pass.equals("")) {
                    if (card.equals(""))
                        cardCodeLayout.setError(getString(R.string.error_input_number_card));
                    if (email.equals(""))
                        emailLayout.setError(getString(R.string.error_input_email));
                    if (first.equals(""))
                        firstLayout.setError(getString(R.string.error_input_first_name));
                    if (last.equals(""))
                        lastLayout.setError(getString(R.string.error_input_last_name));
                    if (idnp.equals(""))
                        idnpLayout.setError(getString(R.string.error_input_idnp));
                    if (phone.equals(""))
                        phoneLayout.setError(getString(R.string.error_input_number_phone));
                    if (pass.equals(""))
                        passLayout.setError(getString(R.string.error_input_password));
                } else {
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage(getString(R.string.progres_msg_register_user));
                    progressDialog.show();

                    body = new RegisterUserBody();
                    body.setCardCod(card);
                    body.setEmail(email);
                    body.setFName(first);
                    body.setIDNO(idnp);
                    body.setLName(last);
                    body.setPhone(phone);
                    body.setPassword(pass);

                    Call<RegisterUserResponse> call = commandServices.registerUser(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), body);
                    enqueuCall(call);
                }
            }
        });

        cardCodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    cardCodeLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    emailLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        firstET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    firstLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lastET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    lastLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        idnpET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    idnpLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phoneET.addTextChangedListener(new TextWatcher() {
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
        passET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals(""))
                    passLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return root;
    }

    private void enqueuCall(Call<RegisterUserResponse> call) {

        call.enqueue(new Callback<RegisterUserResponse>() {
            @Override
            public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {
                RegisterUserResponse registerUserResponse = response.body();
                if (registerUserResponse != null){
                    if(registerUserResponse.getErrorCode() == 0 ){
                        progressDialog.setMessage(getString(R.string.progres_msg_activate_user));

                        Call<SimpleResponse> simpleResponseCall = commandServices.activateUser(company.getServiceName(), registerUserResponse.getIDUSER(), registerUserResponse.getPIN());
                        enqueueCallActivateUser(simpleResponseCall);

                    }
                    else{
                        String msg = RemoteException.getServiceException(registerUserResponse.getErrorCode());
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
            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
                progressDialog.dismiss();

                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                        .setTitle(getString(R.string.oops_text)      )
                        .setMessage(getString(R.string.dialog_failure_service) + t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .show();
            }
        });
    }

    private void enqueueCallActivateUser(Call<SimpleResponse> simpleResponseCall) {
        simpleResponseCall.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                SimpleResponse simpleResponse = response.body();
                if(simpleResponse != null) {
                    if (simpleResponse.getErrorCode() == 0) {
                        progressDialog.setMessage(getString(R.string.progres_msg_authenticate));

                        authClient();
                    }
                    else{
                        String msg = RemoteException.getServiceException(simpleResponse.getErrorCode());
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
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressDialog.dismiss();

                new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                        .setTitle(getString(R.string.oops_text)      )
                        .setMessage(getString(R.string.dialog_failure_service) + t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_button_understand) , (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .show();
            }
        });
    }

    private void authClient() {
        AuthenticateUserBody user = new AuthenticateUserBody();
        user.setPassword(body.getPassword());
        user.setPhone(body.getPhone());
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
                            getClientInformation(sId);
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
                            authClient();
                        }))
                        .show();
            }
        });
    }

    private void getClientInformation(String sId) {
        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
        Call<GetClientInfoResponse> call = commandServices.getClientInfo(BaseApp.getAppInstance().getCompanyClicked().getServiceName(), sId);

        call.enqueue(new Callback<GetClientInfoResponse>() {
            @Override
            public void onResponse(Call<GetClientInfoResponse> call, Response<GetClientInfoResponse> response) {
                GetClientInfoResponse clientInfoResponse = response.body();
                if(clientInfoResponse != null && clientInfoResponse.getErrorCode() == 0){
                    try {
                        byte[] secPh = encrypt(body.getPhone().getBytes(), BaseApp.getAppInstance().getHuyYou());
                        byte[] secPa = encrypt(body.getPassword().getBytes(), BaseApp.getAppInstance().getHuyYou());

                        Client client = clientInfoResponse.getClient();
                        client.setPassword(secPa);
                        client.setPhone(secPh);
                        client.setSid(sId);
                        client.setTypeClient(BaseEnum.PersoanaFizica);

                        progressDialog.dismiss();
                        dialog.dismiss();
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
                            getClientInformation(sId);
                        }))
                        .show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();

        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            int displayHeight = DetailCompanyActivity.displayMetrics.heightPixels;
            int dialogWindowHeight = (int) (displayHeight * 0.9f);
            bottomSheet.getLayoutParams().height = dialogWindowHeight;
        }
        View view = getView();
        view.post(() -> {
            View parent = (View) view.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
//            ((View) .getParent()).setBackgroundColor(Color.TRANSPARENT);

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