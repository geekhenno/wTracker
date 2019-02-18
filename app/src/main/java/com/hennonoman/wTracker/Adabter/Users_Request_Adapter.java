package com.hennonoman.wTracker.Adabter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hennonoman.wTracker.ChatPrivateActivity;
import com.hennonoman.wTracker.Fragments.FriendRequest_Fragment;
import com.hennonoman.wTracker.Fragments.Friend_Fragment;
import com.hennonoman.wTracker.HomeActivity;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class Users_Request_Adapter extends FirebaseRecyclerAdapter<User, Users_Request_Adapter.UserViewHolder> {

    private final Context context;


    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference_other;
    private DatabaseReference mDatabaseReference_requests;
    private DatabaseReference mDatabaseReference_requests_count;
    User user;
    User uu;
    public Users_Request_Adapter(Context context, Query ref) {
        super(User.class, R.layout.frineds_request, UserViewHolder.class, ref);
        this.context = context;

    }

    @Override
    protected void populateViewHolder(UserViewHolder holder, User user, int position) {

        holder.setUser(user);

        Friend_Fragment.tabLayout.setBadgeText(1, getItemCount()+"");
        this.user=user;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frineds_request, parent, false);


        return new UserViewHolder(itemView);
    }

    
    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_user_image_view_request)
        ImageView itemUserImageView;
        @BindView(R.id.item_friend_name_text_view_request)
        TextView itemFriendNameTextView;
        @BindView(R.id.item_friend_email_text_view_request)
        TextView itemFriendEmailTextView;
        @BindView(R.id.item_user_parent_request)
        CardView itemUserParent;
        @BindView(R.id.accept_friend_button)
        ImageButton accept_friend_button;
        @BindView(R.id.reject_friend_button)
        ImageButton reject_friend_button;

        UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemUserParent.setOnClickListener(this);
            accept_friend_button.setOnClickListener(this);
            reject_friend_button.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            final FirebaseAuth mAuth =FirebaseAuth.getInstance();
            final FirebaseUser mUser = mAuth.getCurrentUser();

            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mDatabase.getReference().child("friends").child(mUser.getUid());
            mDatabaseReference_other = mDatabase.getReference().child("friends").child(getItem(getAdapterPosition()).getUserid());
            mDatabaseReference_requests = mDatabase.getReference().child("requests").child(mUser.getUid());
            mDatabaseReference_requests_count = mDatabase.getReference().child("requests");
            mDatabaseReference.keepSynced(true);
            mDatabaseReference_requests.keepSynced(true);
            mDatabaseReference_other.keepSynced(true);

            switch (v.getId()) {

                case R.id.accept_friend_button:



                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                              uu =getItem(getAdapterPosition());


                            Toast.makeText(context, uu.getName(), Toast.LENGTH_SHORT).show();
                            dataSnapshot.getRef().child(uu.getUserid()).setValue(uu);



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    mDatabaseReference_other.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            long timestamp = new Date().getTime();
                            long dayTimestamp = getDayTimestamp(timestamp);

                            User user = new User(mUser.getPhotoUrl().toString(),mUser.getUid(),mUser.getDisplayName(),mUser.getEmail(),0,0,timestamp,dayTimestamp);


                            dataSnapshot.getRef().child(user.getUserid()).setValue(user);




                            mDatabaseReference_requests.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Toast.makeText(context, "Accept friend Successfully" , Toast.LENGTH_SHORT).show();
                                    dataSnapshot.getRef().child(uu.getUserid()).removeValue();
                                    notifyDataSetChanged();

                                    mDatabaseReference_requests_count.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            Friend_Fragment.tabLayout.setBadgeText(1, dataSnapshot.getChildrenCount()+"");


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });






                    break;




                case R.id.reject_friend_button:


                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            mDatabaseReference_requests.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().child(user.getUserid()).removeValue();
                                    Toast.makeText(context, "Reject friend Successfully", Toast.LENGTH_SHORT).show();
                                    mDatabaseReference_requests_count.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            Friend_Fragment.tabLayout.setBadgeText(1, dataSnapshot.getChildrenCount()+"");


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                    break;
            }
        }

        void setUser(User user) {

            itemFriendNameTextView.setText(user.getName());
            itemFriendEmailTextView.setText(user.getEmail());
            Glide.with(context)
                    .load(user.getProfileImg())
                    .placeholder(R.drawable.logo_w)
                    .centerCrop()
                    .dontAnimate()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(itemUserImageView);




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


    }
}
