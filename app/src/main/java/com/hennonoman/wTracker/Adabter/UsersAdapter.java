package com.hennonoman.wTracker.Adabter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hennonoman.wTracker.AddGroupActivity;
import com.hennonoman.wTracker.ChatPrivateActivity;
import com.hennonoman.wTracker.HomeActivity;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.User;
import com.mindorks.paracamera.Camera;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;



public class UsersAdapter extends FirebaseRecyclerAdapter<User, UsersAdapter.UserViewHolder> {

    private final Context context;
    private PopupMenu popupMenu;

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
            itemUserParent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    showPop(R.menu.remove_friend, view , getItem(getAdapterPosition()));
                    Toast.makeText(context, getItem(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();

                    return false;
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_user_parent_friend:

                    User u = getItem(getAdapterPosition());
                     Intent thread = new Intent(context, ChatPrivateActivity.class);
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
                    .placeholder(R.drawable.logo_w)
                    .centerCrop()
                    .dontAnimate()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(itemUserImageView);
        }
    }

    public void showPop (int id, final View v , final User user)
    {

        ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.CustomPopupTheme);
        popupMenu = new PopupMenu(ctw, v);
        popupMenu.inflate(id);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {


                removeFriendDialog(user);


                return false;
            }
        });


    }

    void removeFriendDialog(final User user)
    {

        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("WAY TRACKER");
        dialog.setIcon(R.drawable.logo_w);
        dialog.setMessage("Are you sure you want to remove " + user.getName() + " ?");

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        dialog.setButton(AlertDialog.BUTTON_NEUTRAL,"Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                remove_friend(user);

                Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();


            }
        });


        new Dialog(context);
        dialog.show();
    }


    void remove_friend(User user)
    {

         FirebaseAuth mAuth;
         FirebaseUser mUser;

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
         DatabaseReference mDatabase_me , mDatabase_user;
        mDatabase_me = FirebaseDatabase.getInstance().getReference().child("friends").child(mUser.getUid()).child(user.getUserid());
        mDatabase_user = FirebaseDatabase.getInstance().getReference().child("friends").child(user.getUserid()).child(mUser.getUid());
        
        
        
        mDatabase_me.removeValue();
        mDatabase_user.removeValue();


        Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();


    }


}
