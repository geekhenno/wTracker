package com.hennonoman.wTracker.Adabter;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hennonoman.wTracker.ChatPrivateActivity;
import com.hennonoman.wTracker.GroupActivity;
import com.hennonoman.wTracker.HomeActivity;
import com.hennonoman.wTracker.R;
import com.hennonoman.wTracker.model.Group;
import com.hennonoman.wTracker.model.Message;
import com.hennonoman.wTracker.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.content.Context.CLIPBOARD_SERVICE;


public class GroupsAdapter extends FirebaseRecyclerAdapter<Group, GroupsAdapter.UserViewHolder> {

    private final Context context;
    Group group;
    private PopupMenu popupMenu;
    public GroupsAdapter(Context context, Query ref) {
        super(Group.class, R.layout.groups_list, UserViewHolder.class, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(UserViewHolder holder, Group group, int position) {

        holder.setGroup(group);
        this.group=group;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.groups_list, parent, false);

        return new UserViewHolder(itemView);
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_group_image_view)
        CircleImageView itemGroupImageView;
        @BindView(R.id.item_group_name_text_view)
        TextView itemGroupNameTextView;
        @BindView(R.id.item_group_parent_friend)
        CardView itemGroupParent;

        UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemGroupParent.setOnClickListener(this);

            itemGroupParent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {


                    showPop(R.menu.leave_group, view , getItem(getAdapterPosition()));

                    return false;
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_group_parent_friend:




                    Intent i = new Intent(context, GroupActivity.class);
                    i.putExtra("group_id",getItem( getAdapterPosition()).getGroupid());
                    context.startActivity(i);


                    break;
            }
        }

        void setGroup(Group group) {

            itemGroupNameTextView.setText(group.getGroupName());

            FirebaseStorage storage;
            StorageReference gsReference;
            storage = FirebaseStorage.getInstance();

            //////
            gsReference = storage.getReferenceFromUrl("gs://wtracker-ad766.appspot.com/images/"+group.getGroupImg());
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(gsReference)
                    .into(itemGroupImageView);


        }

        public void showPop (int id, final View v , final Group group )
        {

            ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.CustomPopupTheme);
            popupMenu = new PopupMenu(ctw, v);
            popupMenu.inflate(id);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {


                    switch (menuItem.getItemId())
                    {
                        case R.id.leave_group:

                            leaveGroupDialog(group);
                            break;


                        case R.id.copy_id:

                            copy_ID(group);

                            break;


                    }



                    return false;
                }
            });


        }

        void leaveGroupDialog(final Group group) {

            AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle("WAY TRACKER");
            dialog.setIcon(R.drawable.logo_w);
            dialog.setMessage("Are you sure you want to leave " + group.getGroupName() + " group ?");

            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });


            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    leave_group(group);

                    Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();


                }
            });


            new Dialog(context);
            dialog.show();
        }

            void leave_group(Group group)
            {

                FirebaseAuth mAuth;
                FirebaseUser mUser;

                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                DatabaseReference mDatabase_me , mDatabase_group;
                mDatabase_me = FirebaseDatabase.getInstance().getReference().child("group_users").child(mUser.getUid()).child(group.getGroupid());
                mDatabase_group = FirebaseDatabase.getInstance().getReference().child("groups").child(group.getGroupid()).child("users").child(mUser.getUid());



                mDatabase_me.removeValue();
                mDatabase_group.removeValue();


                Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();


            }



            void copy_ID(Group group)
            {


                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(
                            context.getString(R.string.clipboard_title_copied_message),
                            group.getGroupid());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, R.string.message_message_copied, Toast.LENGTH_SHORT).show();



        }

    }

}
