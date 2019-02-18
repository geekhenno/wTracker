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
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.User;
import com.rahimlis.badgedtablayout.BadgedTabLayout;

import java.util.Calendar;
import java.util.Date;

public class Friend_Fragment extends Fragment {


    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseRequests;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore firestoer;
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
    public static BadgedTabLayout tabLayout;
    FloatingActionButton add_friend;

    boolean check =false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_friend, container, false);


        fragmentManager = getChildFragmentManager();
        mViewPager =  view.findViewById(R.id.viewpager_friends);
        tabLayout =  view.findViewById(R.id.tablayout_friends);
        add_friend= view.findViewById(R.id.add_friend);

        firestoer = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseRequests= FirebaseDatabase.getInstance().getReference().child("requests");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mViewPager.setAdapter(new MyPagerAdapter(fragmentManager));
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setIcon(0,R.drawable.my_friend);
        tabLayout.setIcon(1,R.drawable.add_friend_white);


         tabLayout.setBadgeText(1, "0");


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

                            if(mUser.getEmail().equals(c))
                            {

                                Toast.makeText(getContext(), "Sorry you Can not add your self", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                findFriend(c);
                                alertDialog.dismiss();
                            }



                        }

                    }
                });
            }
        });
        alertDialog.show();








    }


    public void findFriend(final String email)
    {


        check= false;

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren())
                {

                    User user = data.getValue(User.class);
                  // String em = data.child("email").getValue(String.class);
                    if (user.getEmail().equals(email))
                    {
                        long timestamp = new Date().getTime();
                        long dayTimestamp = getDayTimestamp(timestamp);
                        User u = new User(mUser.getPhotoUrl().toString(), mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), 0, 0, timestamp, dayTimestamp);
                        mDatabaseRequests.child(user.getUserid()).child(mUser.getUid()).setValue(u);
                        check=true;
                        break;
                    }
                }


                if(check)
                    Toast.makeText(getContext(), "send request successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "No account with this email", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        mDatabase.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//
//                if(dataSnapshot.exists())
//                {
//                    User user = dataSnapshot.getValue(User.class);
//
//                    dataSnapshot.child("email").
//
//                    if(user.getEmail().equals(email))
//                    {
//
//                        check = true;
//                        long timestamp = new Date().getTime();
//                        long dayTimestamp = getDayTimestamp(timestamp);
//                        User u = new User(mUser.getPhotoUrl().toString(), mUser.getUid(), mUser.getDisplayName(), mUser.getEmail(), 0, 0, timestamp, dayTimestamp);
//                        mDatabaseRequests.child(user.getUserid()).child(mUser.getUid()).setValue(u);
//                        Toast.makeText(getContext(), "send request successfully in", Toast.LENGTH_SHORT).show();
//
//
//
//                    }
//                    dataSnapshot.hasChild()
//
//                }
//
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



    }




    private long getDayTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
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
