package com.hennonoman.wTracker;


import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import  android.support.v4.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;
import com.hennonoman.wTracker.Fragments.Friend_Fragment;
import com.hennonoman.wTracker.Fragments.GroupList_Fragment;
import com.hennonoman.wTracker.Fragments.Profile_Fragment;
import com.hennonoman.wTracker.Fragments.MapviewFragment;

import icepick.Icepick;
import icepick.State;

public class HomeActivity extends AppCompatActivity {



    public static Activity context;

    private final Fragment[] PAGES = new Fragment[] {
            new MapviewFragment(),
            new GroupList_Fragment(),
            new Friend_Fragment(),
            new Profile_Fragment()

    };
    private final String[] PAGE_TITLES = new String[] {
            "Home",
            "Groups",
            "Friends",
            "Profile"

    };




    FragmentManager fragmentManager;

    private ViewPager mViewPager;
    private   TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


         context= this;
         fragmentManager = getSupportFragmentManager();
         mViewPager =  findViewById(R.id.viewpager);


        mViewPager.setAdapter(new MyPagerAdapter(fragmentManager));


         tabLayout =  findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.home);
        tabLayout.getTabAt(1).setIcon(R.drawable.group_icon2);
        tabLayout.getTabAt(2).setIcon(R.drawable.groups_icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.man_icon);








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

    public static class Application extends android.app.Application {

        @Override
        public void onCreate() {
            super.onCreate();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

    }

    public abstract static class BaseActivity extends AppCompatActivity {

        @State
        protected boolean isLoading;
        @State
        protected boolean userInteracted;

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            Icepick.saveInstanceState(this, outState);
        }

        public void showProgress() {
            isLoading = true;
            displayLoadingState();
        }

        public void hideProgress() {
            isLoading = false;
            displayLoadingState();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Icepick.restoreInstanceState(this, savedInstanceState);
            if (savedInstanceState == null) {
                isLoading = false;
                userInteracted = false;
            }
        }

        @Override
        protected void onResume() {
            super.onResume();
            displayLoadingState();
        }

        protected abstract void displayLoadingState();

        @Override
        public boolean onSupportNavigateUp() {
            finish();
            return true;
        }

    }

    public static class Constants {

        public static final String USER_ID_EXTRA = "userIdExtra";

        private Constants() {
        }

    }
}


