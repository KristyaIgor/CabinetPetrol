package edi.md.petrolcabinet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import edi.md.petrolcabinet.fragments.FragmentCompanies;
import edi.md.petrolcabinet.fragments.FragmentNews;

public class MainActivityV1 extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v_1);

        viewPager = (ViewPager) findViewById(R.id.container_main);
        createViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_main_layout);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

//        tabLayout.setupWithViewPager(viewPager);
//        createTabIcons();

    }

    private void createTabIcons() {

//        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_layout_v_1, null);
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_apartment_black_24dp, 0, 0);
//        tabLayout.getTabAt(0).setCustomView(tabOne).setText("Companii");
//
//        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_layout_v_1, null);
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_newspaper, 0, 0);
//        tabLayout.getTabAt(1).setCustomView(tabTwo).setText("Noutati");

//        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabThree.setText("Tab 3");
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_order, 0, 0);
//        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new Fragment1(), "Companii");
//        adapter.addFrag(new Fragment2(), "Noughtati");
//        adapter.addFrag(new Fragment3(), "Tab 3");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position== 0){
                FragmentCompanies one= new FragmentCompanies();
                return one;
            }else{
                FragmentNews two= new FragmentNews();
                return  two;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
    }
}