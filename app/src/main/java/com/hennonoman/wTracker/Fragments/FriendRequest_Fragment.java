package com.hennonoman.wTracker.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hennonoman.wTracker.Adabter.FriendListRecyclerAdapter;
import com.hennonoman.wTracker.Adabter.FriendRequestsRecyclerAdapter;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendRequest_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private FriendRequestsRecyclerAdapter blogRecyclerAdapter;
    private List<User> userList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseReference;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    //  FloatingActionButton add ,logout;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_friends_request,container,false);




        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("requests").child(mUser.getUid());

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseReference.keepSynced(true);


        userList = new ArrayList<>();


        recyclerView =  view.findViewById(R.id.recyclerView_myfriends_request);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));






        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {

                String key= dataSnapshot.getKey();
                DatabaseReference d =mDatabaseUsers.child(key);

                d.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);
                        userList.add(user);
                        Collections.reverse(userList);
                        blogRecyclerAdapter = new FriendRequestsRecyclerAdapter(getContext(), userList);
                        recyclerView.setAdapter(blogRecyclerAdapter);
                        blogRecyclerAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {



            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {



            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;



    }









}
