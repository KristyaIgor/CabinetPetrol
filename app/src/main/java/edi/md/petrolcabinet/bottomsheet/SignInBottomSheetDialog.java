package edi.md.petrolcabinet.bottomsheet;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import edi.md.petrolcabinet.BaseApp;
import edi.md.petrolcabinet.CompanyActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.fragments.FragmentLoginFizic;
import edi.md.petrolcabinet.fragments.FragmentLoginJuridic;
import edi.md.petrolcabinet.realm.objects.Company;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     SignInBottomSheetDialog.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class SignInBottomSheetDialog extends BottomSheetDialogFragment {

    Company company;
    RadioButton buttonFizic, buttonJuridic;
    FragmentManager fgManager;

    static Dialog dialog;
    static View bottomSheet;

    public static final String TAG = "ActionBottomDialog";

    public static SignInBottomSheetDialog newInstance() {
        return new SignInBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.bottom_sheet_sign_in, container, false);

        ImageView logo = root.findViewById(R.id.image_company_logo_auth);
        TextView title = root.findViewById(R.id.title_auth_user);
        buttonFizic = root.findViewById(R.id.rbtnFizic);
        buttonJuridic = root.findViewById(R.id.rbtnJuridic);

        company = BaseApp.getAppInstance().getCompanyClicked();
        fgManager = getChildFragmentManager();

        byte[] decodedString = Base64.decode(company.getLogo(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        logo.setImageBitmap(decodedByte);
        title.setText(getString(R.string.authentificate_cabinet_title) + company.getName());
//        title.setText(getString(R.string.welcome_message_comapny) + company.getName());

        fgManager.beginTransaction().replace(R.id.container_login,new FragmentLoginFizic()).commit();

        buttonFizic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    fgManager.beginTransaction().replace(R.id.container_login,new FragmentLoginFizic()).commit();
                }
            }
        });
        buttonJuridic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    fgManager.beginTransaction().replace(R.id.container_login,new FragmentLoginJuridic()).commit();
                }
            }
        });


        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();

        if (dialog != null) {
            bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            int displayHeight = CompanyActivity.displayMetrics.heightPixels;
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
}