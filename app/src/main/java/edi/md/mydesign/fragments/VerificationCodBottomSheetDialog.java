package edi.md.mydesign.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import edi.md.mydesign.BaseApp;
import edi.md.mydesign.DetailCompanyActivity;
import edi.md.mydesign.R;
import edi.md.mydesign.realm.objects.Company;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     SignInBottomSheetDialog.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class VerificationCodBottomSheetDialog extends BottomSheetDialogFragment {

    Company company;
    FragmentManager fgManager;

    static Dialog dialog;
    static View bottomSheet;

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    public static final String TAG = "ActionBottomDialog";

    // TODO: Customize parameters
    public static VerificationCodBottomSheetDialog newInstance() {
        return new VerificationCodBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bottom_sheet_add_card_verification, container, false);

        ImageView logo = root.findViewById(R.id.add_card_image_company_logo);
        TextView title = root.findViewById(R.id.title_add_card);

        company = BaseApp.getAppInstance().getCompanyClicked();
        fgManager = getChildFragmentManager();

        byte[] decodedString = Base64.decode(company.getLogo(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        logo.setImageBitmap(decodedByte);
        title.setText("Verificarea");

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();

        if (dialog != null) {
            bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            int displayHeight = DetailCompanyActivity.displayMetrics.heightPixels;
            int dialogWindowHeight = (int) (displayHeight * 0.75f);
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
}