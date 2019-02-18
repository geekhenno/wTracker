package com.hennonoman.wTracker;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hennonoman.wTracker.Fragments.Friend_Fragment;
import com.hennonoman.wTracker.Fragments.GroupChat_Fragment;
import com.hennonoman.wTracker.Fragments.GroupList_Fragment;
import com.hennonoman.wTracker.Fragments.GroupTrackFragment;
import com.hennonoman.wTracker.Fragments.MapviewFragment;
import com.hennonoman.wTracker.Fragments.Profile_Fragment;
import com.hennonoman.wTracker.model.Group;

public class GroupActivity extends AppCompatActivity {


    public static String group_id;

    private final Fragment[] PAGES = new Fragment[] {
            new GroupTrackFragment(),
            new GroupChat_Fragment()

    };
    private final String[] PAGE_TITLES = new String[] {
            "Track",
            "Chat"

    };

public static Context context;

    FragmentManager fragmentManager;

    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        context= GroupActivity.this;

        group_id = getIntent().getExtras().getString("group_id");
        fragmentManager = getSupportFragmentManager();
        mViewPager =  findViewById(R.id.viewpager_groups);


        mViewPager.setAdapter(new MyPagerAdapter(fragmentManager));

        tabLayout =  findViewById(R.id.tablayout_groups);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.logo_w);
        tabLayout.getTabAt(1).setIcon(R.drawable.logo_w);

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {



        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }
    }
}


