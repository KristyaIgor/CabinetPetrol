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

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import edi.md.petrolcabinet.BaseApp;
import edi.md.petrolcabinet.DetailCompanyActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.realm.objects.Company;
import edi.md.petrolcabinet.remoteSettings.ApiUtils;
import edi.md.petrolcabinet.remoteSettings.CommandServices;
import edi.md.petrolcabinet.remoteSettings.RemoteException;
import edi.md.petrolcabinet.remote.resetPassword.ResetPasswordResponse;
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
public class ResetPasswordJuridicBottomSheetDialog extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialogRese";
    Company company;
    ImageView logo;
    TextInputEditText emailET, idnpET;
    TextInputLayout emailLayout, idnpLayout;
    Button recuperareBtn;

    ProgressDialog progressDialog;
    Dialog dialog;

    // TODO: Customize parameters
    public static ResetPasswordJuridicBottomSheetDialog newInstance() {
        return new ResetPasswordJuridicBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.bottom_sheet_reset_password_juridic, container, false);

        logo = root.findViewById(R.id.image_company_logo_reset_juridic);
        emailET = root.findViewById(R.id.editTextEmailResetJuridic);
        idnpET = root.findViewById(R.id.editTextIDNPResetJuridic);
        emailLayout = root.findViewById(R.id.emailLayout);
        idnpLayout = root.findViewById(R.id.editTextIDNPLayoutReset);
        recuperareBtn = root.findViewById(R.id.btn_reset_password_juridic);
        progressDialog = new ProgressDialog(getContext(),  R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);

        company = BaseApp.getAppInstance().getCompanyClicked();

        byte[] decodedString = Base64.decode(company.getLogo(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        logo.setImageBitmap(decodedByte);

        recuperareBtn.setOnClickListener(view -> {
            String email = emailET.getText().toString();
            String idnp = idnpET.getText().toString();

            if(email.equals("") && idnp.equals("")){
                emailLayout.setError(getString(R.string.error_input_email));
                idnpLayout.setError(getString(R.string.error_input_idnp));
            }
            else{
                if(email.equals("") || idnp.equals("")){
                    if (email.equals(""))
                        emailLayout.setError(getString(R.string.error_input_email));
                    if (idnp.equals(""))
                        idnpLayout.setError(getString(R.string.error_input_idnp));
                }
                else{
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Recuperarea parolei...");
                    progressDialog.show();

                    CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());
                    Call<ResetPasswordResponse> call = commandServices.resetPassword(BaseApp.getAppInstance().getCompanyClicked().getServiceName(),email,idnp,1);

                    enqueueCall(call);
                }
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

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();

        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.bottom_sheet_reset_juridic);
            int displayHeight = DetailCompanyActivity.displayMetrics.heightPixels;
            int dialogWindowHeight = (int) (displayHeight * 0.7f);
            int dialogWrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
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

    private void enqueueCall(Call<ResetPasswordResponse> call) {
        call.enqueue(new Callback<ResetPasswordResponse>() {
            @Override
            public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                ResetPasswordResponse resetPasswordResponse = response.body();

                if(resetPasswordResponse != null){
                    if(resetPasswordResponse.getErrorCode() == 0){
                        progressDialog.dismiss();
                        new MaterialAlertDialogBuilder(getContext(),R.style.MaterialAlertDialogCustom)
                                .setTitle(getString(R.string.dialog_title_succes))
                                .setMessage(getString(R.string.dialog_msg_send_new_password))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.dialog_button_understand), (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    dialog.dismiss();
                                })
                                .show();
                    }
                    else{
                        String msg = RemoteException.getServiceException(resetPasswordResponse.getErrorCode());
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
            public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
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
}