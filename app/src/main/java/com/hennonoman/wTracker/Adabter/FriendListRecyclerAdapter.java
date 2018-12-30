package com.hennonoman.wTracker.Adabter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.User;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;



public class FriendListRecyclerAdapter extends RecyclerView.Adapter<FriendListRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferencePost;
    private DatabaseReference mDatabaseReferenceLikes;

    User user;
    public FriendListRecyclerAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frineds_list, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FirebaseAuth mAuth =FirebaseAuth.getInstance();
        final FirebaseUser mUser = mAuth.getCurrentUser();

        user = userList.get(position);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("friends").child(mUser.getUid());

                holder.itemFriendEmailTextView.setText(user.getEmail());
                holder.itemFriendNameTextView.setText(user.getName());





        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();










        //TODO: Use Picasso library to load image



        Glide.with(context)
                .load(user.getProfileImg())
                .centerCrop()
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.itemUserImageView);



        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

              //  Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();
                User user = dataSnapshot.getValue(User.class);

                holder.itemFriendEmailTextView.setText(user.getEmail());
                holder.itemFriendNameTextView.setText(user.getName());
              
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

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemUserImageView;
        TextView itemFriendNameTextView;
        TextView itemFriendEmailTextView;
        CardView itemUserParent;

        public ViewHolder(View view, Context ctx)
        {
            super(view);

            context = ctx;

            itemUserImageView = view.findViewById(R.id.item_user_image_view);
            itemFriendNameTextView = view.findViewById(R.id.item_friend_name_text_view);
            itemFriendEmailTextView = view.findViewById(R.id.item_friend_email_text_view);
            itemUserParent = view.findViewById(R.id.item_user_parent_friend);



        }
    }
}
