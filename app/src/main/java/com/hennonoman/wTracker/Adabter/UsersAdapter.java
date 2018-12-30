package com.hennonoman.wTracker.Adabter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.hennonoman.wTracker.HomeActivity;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.ThreadActivity;
import com.hennonoman.wTracker.model.User;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;



public class UsersAdapter extends FirebaseRecyclerAdapter<User, UsersAdapter.UserViewHolder> {

    private final Context context;


    public UsersAdapter(Context context, Query ref) {
        super(User.class, R.layout.frineds_list, UserViewHolder.class, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(UserViewHolder holder, User user, int position) {

        holder.setUser(user);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frineds_list, parent, false);

        return new UserViewHolder(itemView);
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_user_image_view)
        ImageView itemUserImageView;
        @BindView(R.id.item_friend_name_text_view)
        TextView itemFriendNameTextView;
        @BindView(R.id.item_friend_email_text_view)
        TextView itemFriendEmailTextView;
        @BindView(R.id.item_user_parent_friend)
        CardView itemUserParent;

        UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemUserParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_user_parent_friend:

                    User u = getItem(getAdapterPosition());
                //    EventBus.getDefault().post(getRef(getLayoutPosition()));
               //     Toast.makeText(context, u.getName(), Toast.LENGTH_SHORT).show();

                    Intent thread = new Intent(context, ThreadActivity.class);
                   thread.putExtra(HomeActivity.Constants.USER_ID_EXTRA,u.getUserid());
                    context.startActivity(thread);

                    break;
            }
        }

        void setUser(User user) {

            itemFriendNameTextView.setText(user.getName());
            itemFriendEmailTextView.setText(user.getEmail());
            Glide.with(context)
                    .load(user.getProfileImg())
                    .placeholder(R.drawable.feed_me)
                    .centerCrop()
                    .dontAnimate()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(itemUserImageView);
        }
    }
}
