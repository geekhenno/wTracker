package com.hennonoman.wTracker.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hennonoman.wTracker.Adabter.MessagesAdapter;
import com.hennonoman.wTracker.Adabter.MessagesGroupsAdapter;
import com.hennonoman.wTracker.GroupActivity;
import com.hennonoman.wTracker.HomeActivity;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.Group;
import com.hennonoman.wTracker.model.Message;
import com.hennonoman.wTracker.widgets.EmptyStateRecyclerView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class GroupChat_Fragment extends Fragment implements TextWatcher {


    Toolbar toolbar;

     EmptyStateRecyclerView messagesRecycler;

    FloatingActionButton sendFab;

    TextInputEditText inputEditText;

    TextView emptyView;

    RelativeLayout editorParent;

    ProgressBar progress;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @State
    String groupUid;
    @State
    boolean emptyInput;

    private Group group;
    private FirebaseUser owner;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.activity_chat_group,container,false);


    //    toolbar = view.findViewById(R.id.activity_group_toolbar);
        messagesRecycler = view.findViewById(R.id.activity_group_messages_recycler);
        sendFab = view.findViewById(R.id.activity_group_send_fab);
        inputEditText = view.findViewById(R.id.activity_group_input_edit_text);
        emptyView = view.findViewById(R.id.activity_group_empty_view);
        editorParent = view.findViewById(R.id.activity_group_editor_parent);



//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);







        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(getActivity());




        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (savedInstanceState == null) {
            groupUid = GroupActivity.group_id;
        }
        sendFab.requestFocus();





        loadUserDetails();
        initializeAuthListener();
        initializeInteractionListeners();






            sendFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (group == null || owner == null) {
                        Log.d("@@@@", "onSendClick: User:" + group + " Owner:" + owner);
                        return;
                    }

                    long timestamp = new Date().getTime();
                    long dayTimestamp = getDayTimestamp(timestamp);
                    String body = inputEditText.getText().toString().trim();
                    String ownerUid = owner.getUid();
                    String userUid = group.getGroupid();
                    Message message =
                            new Message(timestamp, -timestamp, dayTimestamp, body, ownerUid, userUid,owner.getDisplayName());
                    mDatabase
                            .child("notifications")
                            .child("messages")
                            .push()
                            .setValue(message);
                    mDatabase
                            .child("messages")
                            .child(userUid)
//                            .child(ownerUid)
                            .push()
                            .setValue(message);
                    if (!userUid.equals(ownerUid)) {
                        mDatabase
                                .child("messages")
                                .child(ownerUid)
                                .child(userUid)
                                .push()
                                .setValue(message);
                    }
                    inputEditText.setText("");
                }


            });






        return view;



    }





    private void initializeInteractionListeners() {
        inputEditText.addTextChangedListener(this);
    }

    private void loadUserDetails() {
        DatabaseReference userReference = mDatabase
                .child("groups")
                .child(groupUid);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                group = dataSnapshot.getValue(Group.class);
                initializeMessagesRecycler();
                displayUserDetails();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), R.string.error_loading_user, Toast.LENGTH_SHORT).show();
               // finish();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



    private void initializeAuthListener() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                owner = firebaseAuth.getCurrentUser();
                if (owner != null) {
                    initializeMessagesRecycler();

                    Log.d("@@@@", "thread:signed_in:" + owner.getUid());
                } else {
                    Log.d("@@@@", "thread:signed_out");
                    //Intent login = new Intent(GroupChat_Fragment.this, HomeActivity.class);
                   // startActivity(login);
                   // finish();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }


    private void initializeMessagesRecycler() {
        if (group == null || owner == null) {
            Log.d("@@@@", "initializeMessagesRecycler: User:" + group + " Owner:" + owner);
            return;
        }
        Query messagesQuery = mDatabase
                .child("messages")
               // .child(owner.getUid())
                .child(group.getGroupid())
                .orderByChild("negatedTimestamp");

        MessagesGroupsAdapter adapter = new MessagesGroupsAdapter(getContext(), group.getGroupid(), messagesQuery);
        messagesRecycler.setAdapter(null);
        messagesRecycler.setAdapter(adapter);
        messagesRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        messagesRecycler.setEmptyView(emptyView);
        messagesRecycler.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                messagesRecycler.smoothScrollToPosition(0);
            }
        });
    }

    @OnClick(R.id.activity_thread_send_fab)
    public void onClick() {
        if (group == null || owner == null) {
            Log.d("@@@@", "onSendClick: User:" + group + " Owner:" + owner);
            return;
        }
        long timestamp = new Date().getTime();
        long dayTimestamp = getDayTimestamp(timestamp);
        String body = inputEditText.getText().toString().trim();
        String ownerUid = owner.getUid();
        String userUid = group.getGroupid();
        Message message =
                new Message(timestamp, -timestamp, dayTimestamp, body, ownerUid, userUid,owner.getDisplayName());
        mDatabase
                .child("notifications")
                .child("messages")
                .push()
                .setValue(message);
        mDatabase
                .child("messages")
                .child(userUid)
                .child(ownerUid)
                .push()
                .setValue(message);
        if (!userUid.equals(ownerUid)) {
            mDatabase
                    .child("messages")
                    .child(ownerUid)
                    .child(userUid)
                    .push()
                    .setValue(message);
        }
        inputEditText.setText("");
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

    private void displayUserDetails() {
        //todo[improvement]: maybe display the picture in the toolbar.. WhatsApp style
     //   toolbar.setTitle(group.getGroupName());
        //toolbar.setSubtitle(user.getEmail());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        emptyInput = s.toString().trim().isEmpty();

    }
}
