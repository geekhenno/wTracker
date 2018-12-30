package com.hennonoman.wTracker.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.User;

public class Friend_Fragment extends Fragment {


    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseRequests;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    TextView emptyView;


    EditText friendEmail;




    private final Fragment[] PAGES = new Fragment[] {
            new FriendList_Fragment(),
            new FriendRequest_Fragment()

    };
    private final String[] PAGE_TITLES = new String[] {
            "Friends",
            "Requests"


    };




    FragmentManager fragmentManager;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    FloatingActionButton add_friend;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_friend, container, false);


        fragmentManager = getChildFragmentManager();
        mViewPager =  view.findViewById(R.id.viewpager_friends);
        tabLayout =  view.findViewById(R.id.tablayout_friends);
        add_friend= view.findViewById(R.id.add_friend);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseRequests= FirebaseDatabase.getInstance().getReference().child("requests");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mViewPager.setAdapter(new MyPagerAdapter(fragmentManager));
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.add_friend_white);
        tabLayout.getTabAt(1).setIcon(R.drawable.add_friend_white);




        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                showDialog();
            }
        });


       // initializeUsersRecycler();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    public void showDialog()
    {

        friendEmail = new EditText(getContext());
        friendEmail.setHint("Enter the email of your friend");
        friendEmail.setText("@gmail.com");
        friendEmail.setTextColor(getResources().getColor(R.color.white));
//        InputFilter[] FilterArray = new InputFilter[1];
//        FilterArray[0] = new InputFilter.LengthFilter(6);
//        friendEmail.setFilters(FilterArray);
        friendEmail.setMaxLines(1);


        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Add Friend")
                .setIcon(R.drawable.add_friend_white)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .setNegativeButton("cancel", null)
                .setView(friendEmail)
                .create();


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String c =friendEmail.getText().toString();
                        if(c.isEmpty())
                            friendEmail.setError("fill blank");
                        else
                        {

                            findFriend(c);
                            alertDialog.dismiss();
                        }

                    }
                });
            }
        });
        alertDialog.show();








    }


    public void findFriend(final String email)
    {

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                User user = dataSnapshot.getValue(User.class);
                if(email.equals(user.getEmail()))
                {

                    mDatabaseRequests.child(user.getUserid()).child(mUser.getUid()).setValue("status");
                  //  dataSnapshot.child("requests").child(mUser.getUid()).getRef().setValue("status");
                    Toast.makeText(getContext(), "done!!!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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
