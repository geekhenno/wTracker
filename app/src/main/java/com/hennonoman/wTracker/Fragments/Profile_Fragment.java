package com.hennonoman.wTracker.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hennonoman.wTracker.Adabter.FriendListRecyclerAdapter;
import com.hennonoman.wTracker.HomeActivity;
import com.hennonoman.wTracker.MainActivity;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Profile_Fragment extends Fragment {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    RecyclerView recyclerView;
    ProgressBar load_progress;

    CircleImageView image_profilePic;
    Button signout_button;
    TextView show_username , show_email;



    private FriendListRecyclerAdapter blogRecyclerAdapter;
    private List<User> blogList;

    public Profile_Fragment()
    {


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);


         image_profilePic = view.findViewById(R.id.image_profilePic);
         show_username = view.findViewById(R.id.show_username);
         show_email= view.findViewById(R.id.show_email);
        signout_button= view.findViewById(R.id.signout_button);


//        blogList = new ArrayList<>();
//
//        mDatabase = FirebaseDatabase.getInstance();
//        mDatabaseReference = mDatabase.getReference().child("MBlog");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        show_username.setText(mUser.getDisplayName());
        show_email.setText(mUser.getEmail());


        Glide.with(getContext())
                .load(mUser.getPhotoUrl())
                .centerCrop()
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(image_profilePic);




        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signoutDialog();
            }
        });

//
//

      //  mDatabaseReference.keepSynced(true);
//        recyclerView =  view.findViewById(R.id.recyclerView_mypost);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        load_progress = view.findViewById(R.id.load_progress_mypost);
//        load_progress.setVisibility(View.VISIBLE);





//        mDatabaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s)
//            {
//
////                User blog = dataSnapshot.getValue(User.class);
////
////                if(blog.getUserid().equals(mUser.getUid()))
////                {
////                    blogList.add(blog);
////                    Collections.reverse(blogList);
////                    blogRecyclerAdapter = new FriendListRecyclerAdapter(getContext(),blogList);
////                    recyclerView.setAdapter(blogRecyclerAdapter);
////                    blogRecyclerAdapter.notifyDataSetChanged();
////                }
////
////
////
////
////                load_progress.setVisibility(View.INVISIBLE);
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//
//
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                load_progress.setVisibility(View.INVISIBLE);
//            }
//        });


        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
     //   inflater.inflate(R.menu.menu_calls_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    void signoutDialog()
    {

        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle("FEED ME");
        dialog.setIcon(R.drawable.feed_me);
        dialog.setMessage("Are you sure you want to sign out ?");

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        dialog.setButton(AlertDialog.BUTTON_NEUTRAL,"Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                sign_out();


            }
        });


        new Dialog(getContext());
        dialog.show();
    }


    private void sign_out ()
    {


        GoogleSignInClient mGoogleSignInClient ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        mGoogleSignInClient.signOut().addOnCompleteListener( getActivity(),
                new OnCompleteListener<Void>() {  //signout Google
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut(); //signout firebase
                        Intent setupIntent = new Intent(getContext(), MainActivity.class);
                        //  Toast.makeText(getBaseContext(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                        //   setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                      HomeActivity.context.finish();
                    }
                });



    }


}
