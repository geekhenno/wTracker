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
import com.hennonoman.wTracker.Adabter.FriendRequestsRecyclerAdapter;
import com.hennonoman.wTracker.Adabter.UsersAdapter;
import com.hennonoman.wTracker.Adabter.Users_Request_Adapter;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.User;
import com.hennonoman.wTracker.widgets.EmptyStateRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendRequest_Fragment extends Fragment {


    private EmptyStateRecyclerView usersRecycler;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private DatabaseReference mDatabaseReference_req;

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
        mDatabaseReference.keepSynced(true);




        usersRecycler =  view.findViewById(R.id.recyclerView_myfriends_request);







        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {


                Users_Request_Adapter adapter = new Users_Request_Adapter(getContext(), mDatabaseReference );
                usersRecycler.setAdapter(adapter);
                usersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));



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
