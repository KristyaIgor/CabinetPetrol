package edi.md.petrolcabinet.bottomsheet;

import android.app.Dialog;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import edi.md.petrolcabinet.BaseApp;
import edi.md.petrolcabinet.DetailCompanyActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.fragments.FragmentAddCardFinal;
import edi.md.petrolcabinet.fragments.FragmentAddCardInit;
import edi.md.petrolcabinet.realm.objects.Company;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     SignInBottomSheetDialog.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class AddCardBottomSheetDialog extends BottomSheetDialogFragment {

    static Company company;
    FragmentManager fgManager;

    static Dialog dialog;
    static View bottomSheet;
    static TextView title;

    public static final String TAG = "ActionBottomDialog";

    private static ViewPager viewPager;

    CustomPagerAdapterSteps mAdapter;

    static Context context;


    public static AddCardBottomSheetDialog newInstance() {
        return new AddCardBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bottom_sheet_add_card, container, false);

        ImageView logo = root.findViewById(R.id.add_card_image_company_logo);
        title = root.findViewById(R.id.title_add_card);
        viewPager = root.findViewById(R.id.screen_slide_fragments_add_cards);

        company = BaseApp.getAppInstance().getCompanyClicked();
        fgManager = getChildFragmentManager();
        context = getContext();

        byte[] decodedString = Base64.decode(company.getLogo(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        logo.setImageBitmap(decodedByte);
        title.setText(getString(R.string.title_add_card) + company.getName());

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentAddCardInit.newInstance());
        fragments.add(FragmentAddCardFinal.newInstance());

        mAdapter = new CustomPagerAdapterSteps(fgManager, fragments);
        viewPager.setAdapter(mAdapter);

        viewPager.setOnTouchListener((view, motionEvent) -> true);

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseApp.getAppInstance().setPassExpired(false);
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


    public static void setNextStep(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

        title.setText(context.getResources().getString(R.string.verificare_cod_add_card));
    }

    public static void setBeforeStep(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

        title.setText(context.getResources().getString(R.string.title_add_card) + company.getName());
    }

    static class CustomPagerAdapterSteps extends FragmentStatePagerAdapter {

        List<Fragment> mFrags = new ArrayList<>();

        public CustomPagerAdapterSteps(FragmentManager fm, List<Fragment> frags) {
            super(fm);
            mFrags = frags;
        }

        @Override
        public Fragment getItem(int position) {
            return mFrags.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}