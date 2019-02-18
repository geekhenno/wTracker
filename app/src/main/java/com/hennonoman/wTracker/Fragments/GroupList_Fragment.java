package com.hennonoman.wTracker.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hennonoman.wTracker.Adabter.GroupsAdapter;
import com.hennonoman.wTracker.Adabter.UsersAdapter;
import com.hennonoman.wTracker.AddGroupActivity;
import com.hennonoman.wTracker.JoinGroupActivity;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.User;
import com.hennonoman.wTracker.widgets.EmptyStateRecyclerView;

import java.util.List;

public class GroupList_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> userList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference,mDatabaseReferenceGroup;


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    TextView emptyView;
    EmptyStateRecyclerView groupsRecycler;



    FloatingActionButton create_group ,join_group;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_groups_list,container,false);

        create_group = view.findViewById(R.id.create_group);
        join_group = view.findViewById(R.id.join_group);



        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("group_users").child(mUser.getUid());
     //   mDatabaseReferenceGroup = mDatabase.getReference().child("group_users").child(mUser.getUid());

        mDatabaseReference.keepSynced(true);

        emptyView= view.findViewById(R.id.activity_main_group_empty_view);



        groupsRecycler =  view.findViewById(R.id.activity_main_groups_recycler);
        initializeUsersRecycler();

        create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), AddGroupActivity.class));

            }
        });

        join_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), JoinGroupActivity.class));

            }
        });



        return view;



    }

    private void initializeUsersRecycler() {


        GroupsAdapter adapter = new GroupsAdapter(getContext(), mDatabaseReference);
        groupsRecycler.setAdapter(adapter);
        groupsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        groupsRecycler.setEmptyView(emptyView);
    }






}
