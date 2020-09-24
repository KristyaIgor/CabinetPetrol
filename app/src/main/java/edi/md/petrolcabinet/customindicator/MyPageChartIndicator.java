package edi.md.petrolcabinet.customindicator;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import edi.md.petrolcabinet.CardDetailActivity;
import edi.md.petrolcabinet.R;

/**
 * Created by Ali Muzaffar on 5/02/2016.
 */
public class MyPageChartIndicator implements ViewPager.OnPageChangeListener {
    private Context mContext;
    private LinearLayout mContainer;
    private ViewPager mViewPager;
    private int mPageCount = 3;
    private int mInitialPage = 0;
    private List<String> listTitle;

    public MyPageChartIndicator(@NonNull Context context, @NonNull LinearLayout containerView, @NonNull ViewPager viewPager) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        } else if (containerView == null) {
            throw new IllegalArgumentException("containerView cannot be null");
        } else if (viewPager == null) {
            throw new IllegalArgumentException("ViewPager cannot be null");
        } else if (viewPager.getAdapter() == null) {
            throw new IllegalArgumentException("ViewPager does not have an adapter set on it.");
        }
        mContext = context;
        mContainer = containerView;
        mViewPager = viewPager;
    }

    public void setInitialPage(int page) {
        mInitialPage = page;
    }

    public void setPageTitles(List<String> items) {
        listTitle = items;
    }

    public void show() {
        initIndicators();
        setIndicatorAsSelected(mInitialPage);
    }

    private void initIndicators() {
        if (mContainer == null || mPageCount <= 0) {
            return;
        }

        mViewPager.addOnPageChangeListener(this);
        mContainer.removeAllViews();
        for (int i = 0; i < mPageCount; i++) {
            TextView view = new TextView(mContext);
            view.setText(listTitle.get(i));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);
            view.setLayoutParams(lp);
            view.setTextSize(22);
            view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            view.setSelected(i == 1);
            mContainer.addView(view);
        }
    }

    private void setIndicatorAsSelected(int index) {
        if (mContainer == null) {
            return;
        }
        for (int i = 0; i < mContainer.getChildCount(); i++) {
            TextView view = (TextView) mContainer.getChildAt(i);
            if (i == index){
                view.setSelected(true);
                view.setTypeface(Typeface.DEFAULT_BOLD);
                view.setTextColor(mContext.getResources().getColor(R.color.indicator_selected));
            }
            else{
                view.setSelected(false);
                view.setTypeface(Typeface.DEFAULT);
                view.setTextColor(mContext.getResources().getColor(R.color.indicator_unselected));
            }
        }
        CardDetailActivity.setButtonsTint(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int index = position % mPageCount;
        setIndicatorAsSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void cleanup() {
        mViewPager.clearOnPageChangeListeners();
    }
}
