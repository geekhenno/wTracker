package com.hennonoman.wTracker.Fragments;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.hennonoman.wTracker.Adabter.FriendListRecyclerAdapter;
import com.hennonoman.wTracker.Adabter.UsersAdapter;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.User;
import com.hennonoman.wTracker.widgets.EmptyStateRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment {




    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    EmptyStateRecyclerView usersRecycler;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private List<User> userList;
    TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_group,container,false);


        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("friends").child(mUser.getUid());

        mDatabaseReference.keepSynced(true);

        emptyView= view.findViewById(R.id.activity_main_group_empty_view);


        userList = new ArrayList<>();


        usersRecycler =  view.findViewById(R.id.activity_main_groups_recycler);
      //  initializeUsersRecycler();




        return view;



    }






    private void initializeUsersRecycler() {
        UsersAdapter adapter = new UsersAdapter(getContext(), mDatabaseReference);
        usersRecycler.setAdapter(adapter);
        usersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        usersRecycler.setEmptyView(emptyView);
    }






}

