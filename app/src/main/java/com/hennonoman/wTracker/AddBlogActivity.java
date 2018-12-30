package com.hennonoman.wTracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.mindorks.paracamera.Camera;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddBlogActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 0;
    private final int CAMERA_REQUEST_CODE = 1;
    private Uri resultUri = null;

    private ImageView mPostImage;
    private EditText mPostDesc;
    private Button mSubmitButton;


    private StorageReference mStorage;
    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    private Camera camera;


    private ProgressDialog mProgress;
    private Uri mImageUri;


    private PopupMenu popupMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);



        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();


        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("MBlog");
//        mPostImage = findViewById(R.id.imageButton);
//        mPostDesc = findViewById(R.id.descriptionEt);
//        mSubmitButton =  findViewById(R.id.submitPost);


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Posting to our database
                uploadImage();


            }
        });
    }



    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
        startActivity(new Intent(AddBlogActivity.this, HomeActivity.class));
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Camera.REQUEST_TAKE_PHOTO)

        {
            Bitmap bitmap = camera.getCameraBitmap();

            if (bitmap != null)
            {

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "Title", null);
                mImageUri = Uri.parse(path);



            CropImage.activity(mImageUri)
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
          }



        }



        else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK)
        {

            mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);



            if (resultCode == RESULT_OK)
            {

                resultUri = result.getUri();
                mPostImage.setImageURI(resultUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();


            }
        }


    }


    private void uploadImage() {

        final String descVal = mPostDesc.getText().toString().trim();


        if(resultUri != null && !TextUtils.isEmpty(descVal))
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = mStorage.child("images").child(resultUri.getLastPathSegment());
            ref.putFile(resultUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            String downloadurl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            DatabaseReference newPost = mPostDatabase.push();


                            Map<String, String> dataToSave = new HashMap<>();



                            dataToSave.put("postID",newPost.getKey());
                            dataToSave.put("desc", descVal);
                            dataToSave.put("image", resultUri.getLastPathSegment());
                            dataToSave.put("timestamp", String.valueOf(System.currentTimeMillis()));
                            dataToSave.put("userid", mUser.getUid());
                            dataToSave.put("nickname", mUser.getDisplayName());
                            dataToSave.put("email", mUser.getEmail());
                            dataToSave.put("profileImg", mUser.getPhotoUrl().toString());
                            dataToSave.put("liksCounter","0");


                            newPost.setValue(dataToSave);
                            mProgress.dismiss();

                            //onBackPressed();

                            startActivity(new Intent(AddBlogActivity.this, HomeActivity.class));
                            finish();

                            Toast.makeText(AddBlogActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddBlogActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }



    public void getImage(View v)
    {

        showPop(R.menu.choose_image,v);



    }


    public void showPop(int id, View v)
    {

        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.CustomPopupTheme);
        popupMenu = new PopupMenu(ctw,v);
        popupMenu.inflate(id);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId())
                {

                    case R.id.from_camera:


                        camera = new Camera.Builder()
                                .setDirectory("pics")
                                .setName("pic_" + System.currentTimeMillis())
                                .setImageFormat(Camera.IMAGE_JPEG)
                                .setCompression(75)
                                .setImageHeight(1000)
                                .build(AddBlogActivity.this);

                        try {
                            camera.takePicture();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;


                    case R.id.from_gallery:

                        Intent galleryIntent = new Intent();
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                        break;

                }


                return false;
            }
        });



    }

}
