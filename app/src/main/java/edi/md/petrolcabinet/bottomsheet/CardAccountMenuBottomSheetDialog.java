package edi.md.petrolcabinet.bottomsheet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import edi.md.petrolcabinet.AssortmentActivity;
import edi.md.petrolcabinet.CompanyActivity;
import edi.md.petrolcabinet.HistoryActivity;
import edi.md.petrolcabinet.NotificationListActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.SettingsCardAccountActivity;


/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     SignInBottomSheetDialog.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class CardAccountMenuBottomSheetDialog extends BottomSheetDialogFragment {

    static Dialog dialog;
    static View bottomSheet;

    Button openHistoryList, suplyAccount,openAssortmentList, openNotification, openInformation;

    Context context;

    public static final String TAG = "ActionBottomDialog";

    public static CardAccountMenuBottomSheetDialog newInstance() {
        return new CardAccountMenuBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.bottom_sheet_card_menu, container, false);

        openHistoryList = root.findViewById(R.id.btn_menu_history_card);
        suplyAccount = root.findViewById(R.id.btn_menu_suply_account);
        openAssortmentList = root.findViewById(R.id.btn_menu_product_list);
        openNotification = root.findViewById(R.id.btn_menu_notification);
        openInformation = root.findViewById(R.id.btn_menu_information);

        context = getContext();

        openHistoryList.setOnClickListener(view -> {
            Intent hist = new Intent(context, HistoryActivity.class);
            hist.putExtra("CardAccount",true);
            startActivity(hist);
        });

        suplyAccount.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(context,R.style.MaterialAlertDialogCustom)
                    .setTitle(getString(R.string.oops_text))
                    .setMessage(getString(R.string.msg_supply_action_button))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.dialog_button_understand), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        });

        openAssortmentList.setOnClickListener(view -> {
            Intent hist = new Intent(context, AssortmentActivity.class);
            hist.putExtra("CardAccount",true);
            startActivity(hist);
        });

        openInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SettingsCardAccountActivity.class));
            }
        });

        openNotification.setOnClickListener(view -> {
            Intent hist = new Intent(context, NotificationListActivity.class);
            startActivity(hist);
        });

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

        this.dismiss();
    }


    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();

        if (dialog != null) {
            bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            int displayHeight = CompanyActivity.displayMetrics.heightPixels;
            int dialogWindowHeight = (int) (displayHeight * 0.4f);
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