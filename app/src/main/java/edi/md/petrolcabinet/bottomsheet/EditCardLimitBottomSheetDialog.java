package edi.md.petrolcabinet.bottomsheet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import edi.md.petrolcabinet.BaseApp;
import edi.md.petrolcabinet.CardDetailActivity;
import edi.md.petrolcabinet.DetailCompanyActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.remote.ApiUtils;
import edi.md.petrolcabinet.remote.CommandServices;
import edi.md.petrolcabinet.remote.RemoteException;
import edi.md.petrolcabinet.remote.response.SimpleResponse;
import edi.md.petrolcabinet.remote.updateLimitsCard.UpdateLimitsCardBody;
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
public class EditCardLimitBottomSheetDialog extends BottomSheetDialogFragment {
    static Dialog dialog;
    static View bottomSheet;

    TextInputLayout dailyLayout,weeklyLayout, monthLayout;
    TextInputEditText dailyLimit, weeklyLimit,monthLimit;

    Button apply;

    ProgressDialog progressDialog;

    public static final String TAG = "EditCardBottomSheetDialog";


    public static EditCardLimitBottomSheetDialog newInstance() {
        return new EditCardLimitBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bottom_sheet_edit_card, container, false);

        dailyLayout = root.findViewById(R.id.dailyLayout);
        dailyLimit = root.findViewById(R.id.daily_limit);
        weeklyLayout = root.findViewById(R.id.weekLayout);
        weeklyLimit = root.findViewById(R.id.week_limit);
        monthLayout = root.findViewById(R.id.monthLayout);
        monthLimit = root.findViewById(R.id.month_limit);
        progressDialog = new ProgressDialog(getContext(), R.style.ThemeOverlay_AppCompat_Dialog_Alert_TestDialogTheme);
        apply = root.findViewById(R.id.button_set_limit_for_card);

        CommandServices commandServices = ApiUtils.getCommandServices(BaseApp.getAppInstance().getCompanyClicked().getIp());

        apply.setOnClickListener(view -> {
            progressDialog.setMessage(getString(R.string.updates_limit_edit_card));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();



            UpdateLimitsCardBody body = new UpdateLimitsCardBody();
            body.setID(BaseApp.getAppInstance().getClickedCard().getID());
            if(dailyLimit.getText().toString().equals("")){
                dailyLimit.setText("0");
                body.setDailyLimit(0);
            }
            else
                body.setDailyLimit(Integer.parseInt(dailyLimit.getText().toString()));
            if(weeklyLimit.getText().toString().equals("")){
                weeklyLimit.setText("0");
                body.setWeeklyLimit(0);
            }
            else
                body.setWeeklyLimit(Integer.parseInt(weeklyLimit.getText().toString()));
            if(monthLimit.getText().toString().equals("")){
                monthLimit.setText("0");
                body.setMonthlyLimit(0);
            }
            else
                body.setMonthlyLimit(Integer.parseInt(monthLimit.getText().toString()));


            Call<SimpleResponse> call = commandServices.updateLimitsCard(BaseApp.getAppInstance().getCompanyClicked().getServiceName(),BaseApp.getAppInstance().getClientClicked().getSid(),body);
            call.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    SimpleResponse simpleResponse = response.body();

                    if(simpleResponse != null){
                        if (simpleResponse.getErrorCode() == 0){
                            progressDialog.dismiss();
                            CardDetailActivity.onDismissDialog();
                        }
                        else if (simpleResponse.getErrorCode() == 5){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Need add reauth method!", Toast.LENGTH_SHORT).show();
                            //TODO reauth
                        }
                        else{
                            progressDialog.dismiss();
                            String msg = RemoteException.getServiceException(simpleResponse.getErrorCode());

                            new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                                    .setTitle(getString(R.string.oops_text))
                                    .setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.dialog_button_understand), (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    })
                                    .show();
                        }
                    }
                    else{
                        progressDialog.dismiss();

                        new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                                .setTitle(getString(R.string.oops_text))
                                .setMessage(getString(R.string.dialog_msg_response_from_service_null))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.dialog_button_understand), (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                })
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    progressDialog.dismiss();

                    new MaterialAlertDialogBuilder(getContext(), R.style.MaterialAlertDialogCustom)
                            .setTitle(getString(R.string.oops_text))
                            .setMessage(getString(R.string.dialog_failure_service) + t.getMessage())
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.dialog_button_understand), (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            })
                            .show();
                }
            });
        });

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();

        if (dialog != null) {
            bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            int displayHeight = DetailCompanyActivity.displayMetrics.heightPixels;
            int dialogWindowHeight = (int) (displayHeight * 0.47f);
            int dialogWrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
            bottomSheet.getLayoutParams().height = dialogWrapContent;
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
}