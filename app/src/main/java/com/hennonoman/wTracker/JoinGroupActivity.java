package com.hennonoman.wTracker;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hennonoman.wTracker.model.Group;
import com.hennonoman.wTracker.model.User;

import java.util.HashMap;
import java.util.Map;

public class JoinGroupActivity extends AppCompatActivity {


    EditText join_id_group;
    Button join_button_group;

    private DatabaseReference mDatabaseUser;
    private DatabaseReference mDatabaseGroup;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    String latit="0",longi="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);


        join_id_group = findViewById(R.id.join_id_group);
        join_button_group = findViewById(R.id.join_button_group);


        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("group_users").child(mUser.getUid());



        join_button_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_group();

            }
        });

    }


    private void join_group() {


        final String nGroup = join_id_group.getText().toString().trim();


        if (!nGroup.isEmpty())
        {
            mDatabaseGroup = FirebaseDatabase.getInstance().getReference().child("groups").child(nGroup);

            mDatabaseGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {
                        Group g = dataSnapshot.getValue(Group.class);
                        mDatabaseGroup.child("users").child(mUser.getUid()).setValue(mUser.getDisplayName());
                        mDatabaseUser.child(g.getGroupid()).setValue(g);
                        Toast.makeText(JoinGroupActivity.this, "joined successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(JoinGroupActivity.this, "there is no group with this id", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Joining Group...");
//            progressDialog.show();


        }

    }


//    private void addUserToDatabase(FirebaseUser firebaseUser) {
//        User user = new User(
//                firebaseUser.getPhotoUrl() == null ? "" : firebaseUser.getPhotoUrl().toString(),
//                firebaseUser.getUid(),
//                firebaseUser.getDisplayName(),
//                firebaseUser.getEmail(),
//                latit,
//                longi
//        );
//
//        mDatabase.child("users")
//                .child(user.getUserid()).setValue(user);
//
//        String instanceId = FirebaseInstanceId.getInstance().getToken();
//        if (instanceId != null) {
//            mDatabase.child("users")
//                    .child(firebaseUser.getUid())
//                    .child("instanceId")
//                    .setValue(instanceId);
//        }
//    }
}
