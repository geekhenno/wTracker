package com.hennonoman.wTracker.Fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hennonoman.wTracker.Adabter.UsersAdapter;
import com.hennonoman.wTracker.HomeActivity;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.ChatPrivateActivity;
import com.hennonoman.wTracker.model.User;
import com.hennonoman.wTracker.widgets.EmptyStateRecyclerView;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FriendList_Fragment extends Fragment {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    EmptyStateRecyclerView usersRecycler;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    TextView emptyView;
    //  FloatingActionButton add ,logout;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_friends_list,container,false);




        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("friends").child(mUser.getUid());

        mDatabaseReference.keepSynced(true);

        emptyView= view.findViewById(R.id.activity_main_empty_view);




        usersRecycler =  view.findViewById(R.id.activity_main_users_recycler);
        initializeUsersRecycler();


        return view;



    }


    private void initializeUsersRecycler() {
        UsersAdapter adapter = new UsersAdapter(getContext(), mDatabaseReference);
        usersRecycler.setAdapter(adapter);
        usersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        usersRecycler.setEmptyView(emptyView);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserSelected(DatabaseReference selectedRef) {
        Intent thread = new Intent(getContext(), ChatPrivateActivity.class);
        thread.putExtra(HomeActivity.Constants.USER_ID_EXTRA, selectedRef.getKey());
        startActivity(thread);
    }






}
