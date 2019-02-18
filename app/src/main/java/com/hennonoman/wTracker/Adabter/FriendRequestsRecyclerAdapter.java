package com.hennonoman.wTracker.Adabter;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hennonoman.wTracker.Fragments.Friend_Fragment;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.User;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class FriendRequestsRecyclerAdapter extends RecyclerView.Adapter<FriendRequestsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference_other;
    private DatabaseReference mDatabaseReference_requests;

    User user;
    public FriendRequestsRecyclerAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frineds_request, parent, false);


        return new ViewHolder(view, context);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FirebaseAuth mAuth =FirebaseAuth.getInstance();
        final FirebaseUser mUser = mAuth.getCurrentUser();
        user = userList.get(position);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("friends").child(mUser.getUid());
        mDatabaseReference_other = mDatabase.getReference().child("friends").child(user.getUserid());
        mDatabaseReference_requests = mDatabase.getReference().child("requests").child(mUser.getUid());
        mDatabaseReference.keepSynced(true);
        mDatabaseReference_requests.keepSynced(true);
        mDatabaseReference_other.keepSynced(true);




                holder.itemFriendEmailTextView.setText(user.getEmail());
                holder.itemFriendNameTextView.setText(user.getName());





        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();




            holder.accept_friend_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            Toast.makeText(context, user.getUserid(), Toast.LENGTH_SHORT).show();

                               dataSnapshot.getRef().child(user.getUserid()).setValue(user);

                                mDatabaseReference_requests.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().child(user.getUserid()).removeValue();


                                        Friend_Fragment.tabLayout.setBadgeText(1, getItemCount()+"");


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

                    mDatabaseReference_other.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            long timestamp = new Date().getTime();
                            long dayTimestamp = getDayTimestamp(timestamp);
                            User user = new User(mUser.getPhotoUrl().toString(),mUser.getUid(),mUser.getDisplayName(),mUser.getEmail(),0,0,timestamp,dayTimestamp);


                            userList.remove(position);
                            notifyDataSetChanged();
                           dataSnapshot.getRef().child(user.getUserid()).setValue(user);
                            Toast.makeText(context, "Accept friend Successfully", Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }

            });

            holder.reject_friend_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            mDatabaseReference_requests.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   dataSnapshot.getRef().child(userList.get(holder.getLayoutPosition()).getUserid()).removeValue();
                                    userList.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Reject friend Successfully", Toast.LENGTH_SHORT).show();
                                    Friend_Fragment.tabLayout.setBadgeText(1, getItemCount()+"");

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
            });







        //TODO: Use Picasso library to load image



        Glide.with(context)
                .load(user.getProfileImg())
                .centerCrop()
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.itemUserImageView);






    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemUserImageView;
        TextView itemFriendNameTextView;
        TextView itemFriendEmailTextView;
        ImageButton accept_friend_button;
        ImageButton reject_friend_button;
        CardView itemUserParent;

        public ViewHolder(View view, Context ctx)
        {
            super(view);

            context = ctx;

            itemUserImageView = view.findViewById(R.id.item_user_image_view_request);
            itemFriendNameTextView = view.findViewById(R.id.item_friend_name_text_view_request);
            itemFriendEmailTextView = view.findViewById(R.id.item_friend_email_text_view_request);
            itemUserParent = view.findViewById(R.id.item_user_parent_request);
            accept_friend_button = view.findViewById(R.id.accept_friend_button);
            reject_friend_button = view.findViewById(R.id.reject_friend_button);




        }
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
