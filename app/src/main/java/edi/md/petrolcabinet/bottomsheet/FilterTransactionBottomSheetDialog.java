package edi.md.petrolcabinet.bottomsheet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import edi.md.petrolcabinet.CompanyActivity;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.utils.BaseEnum;

import static edi.md.petrolcabinet.HistoryActivity.onFilterList;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     SignInBottomSheetDialog.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class FilterTransactionBottomSheetDialog extends BottomSheetDialogFragment {
    static Dialog dialog;
    static View bottomSheet;

    ChipGroup groupPeriod, groupType;
    Chip day, week, month, year, allPeriod;
    Chip all, alimentare, suplinire;
    Chip typeChip, periodChip;

    TextView deleteAllFilter;

    public static final String TAG = "FilterTransactionBottomSheetDialog";


    public static FilterTransactionBottomSheetDialog newInstance() {
        return new FilterTransactionBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bottom_sheet_transaction_filter, container, false);

        groupPeriod = root.findViewById(R.id.chipGroup);
        groupType = root.findViewById(R.id.chipGroup2);
        day = root.findViewById(R.id.chip5);
        week = root.findViewById(R.id.chip6);
        month = root.findViewById(R.id.chip7);
        year = root.findViewById(R.id.chip8);
        allPeriod = root.findViewById(R.id.chip2);
        all = root.findViewById(R.id.chip9);
        alimentare = root.findViewById(R.id.chip10);
        suplinire = root.findViewById(R.id.chip11);

        deleteAllFilter = root.findViewById(R.id.text_delete_all_filters);

        day.setTag(BaseEnum.chipDay);
        week.setTag(BaseEnum.chipWeek);
        month.setTag(BaseEnum.chipMonth);
        year.setTag(BaseEnum.chipYear);
        allPeriod.setTag(BaseEnum.chipAllPeriod);
        all.setTag(BaseEnum.chipAll);
        alimentare.setTag(BaseEnum.chipAlimentare);
        suplinire.setTag(BaseEnum.chipSuplinire);

        int periods = getContext().getSharedPreferences("CHIP SELECTED", Context.MODE_PRIVATE).getInt("period", allPeriod.getId());
        int types = getContext().getSharedPreferences("CHIP SELECTED", Context.MODE_PRIVATE).getInt("type", all.getId());

        groupType.check(types);
        groupPeriod.check(periods);

        groupType.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                typeChip = group.findViewById(checkedId);

                if (periodChip == null)
                    periodChip = groupPeriod.findViewById(periods);

                int periodTag = (int) periodChip.getTag();

                int typeTag = (int) typeChip.getTag();

                onFilterList(typeTag,periodTag);
                getContext().getSharedPreferences("CHIP SELECTED", Context.MODE_PRIVATE).edit().putInt("type",checkedId).apply();
            }
        });

        groupPeriod.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                periodChip = group.findViewById(checkedId);

                if (typeChip == null)
                    typeChip = groupType.findViewById(types);

                int typeTag = (int) typeChip.getTag();
                int periodTag = (int) periodChip.getTag();

                onFilterList(typeTag,periodTag);

                getContext().getSharedPreferences("CHIP SELECTED", Context.MODE_PRIVATE).edit().putInt("period",checkedId).apply();
            }
        });

        deleteAllFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupPeriod.check(allPeriod.getId());
                groupType.check(all.getId());

                onFilterList((int)all.getTag(), (int)allPeriod.getTag());
            }
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
            int displayHeight = CompanyActivity.displayMetrics.heightPixels;
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