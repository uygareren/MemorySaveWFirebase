package com.example.photosaveprojectwfirebase.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.photosaveprojectwfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AddPhotoActivity extends AppCompatActivity {

    ProgressBar progressBar;

    ImageView imgPick;
    EditText editTextLocation, editTextTime, editTextDescription;
    AppCompatButton addBtn;

    String strLocation, strTime, strDescription;

    int permissionCode = 1;
    Uri imgUri;

    Bitmap pickedImageBitmap;

    HashMap<String, Object> mData;

    Intent intent;


    // Firebase

    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firestore;



    public void init(){

        //Progress Bar

        progressBar = findViewById(R.id.progressbar);

        //İmageView
        imgPick = findViewById(R.id.addPhoto_ImageView);

        //Edittext
        editTextLocation = findViewById(R.id.addPhoto_edittextLocation);
        editTextTime = findViewById(R.id.addPhoto_edittextTime);
        editTextDescription = findViewById(R.id.addPhoto_edittextDescription);

        //Button
        addBtn = findViewById(R.id.addPhoto_addBtn);


        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        init();

        getSupportActionBar().hide();




    }

    public void pickImageCLick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Title"), permissionCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            assert data != null;
            Uri uri = data.getData();
            imgUri = uri;

            try {
                if(Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source imgSource = ImageDecoder.createSource(this.getContentResolver(),uri);
                    pickedImageBitmap = ImageDecoder.decodeBitmap(imgSource);
                    imgPick.setImageBitmap(pickedImageBitmap);
                }else{
                    pickedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    imgPick.setImageBitmap(pickedImageBitmap);
                }

                addBtn.setEnabled(true);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    // This method first saves the uri of the picture taken from the firebase storage gallery,
    // then takes the uri of the picture there and saves it to the firestore.

    public void addImageButtonClick(View view) {

        progressBar.setVisibility(View.VISIBLE);
        addBtn.setEnabled(false);
        imgPick.setEnabled(false);
        editTextLocation.setEnabled(false);
        editTextTime.setEnabled(false);
        editTextDescription.setEnabled(false);

        strLocation = editTextLocation.getText().toString();
        strTime = editTextTime.getText().toString();
        strDescription = editTextDescription.getText().toString();

        if(!TextUtils.isEmpty(strLocation) && !TextUtils.isEmpty(strTime) && !TextUtils.isEmpty(strDescription) && (imgUri != null)) {

            String postId = UUID.randomUUID().toString();


            storage.getReference().child("Images").child(postId).putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            FirebaseStorage.getInstance().getReference().child("Images").child(postId)
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String downloadUri = uri.toString();

                                            // Veritabanı işlemleri

                                            user = auth.getCurrentUser();
                                            assert user != null;

                                            mData = new HashMap<>();

                                            mData.put("documentId", postId);
                                            mData.put("location", strLocation);
                                            mData.put("time", strTime);
                                            mData.put("description", strDescription);
                                            mData.put("imgUrl", downloadUri);
                                            mData.put("uid", user.getUid());



                                            firestore.collection("Records").document(postId)
                                                    .set(mData)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                addBtn.setEnabled(true);
                                                                imgPick.setEnabled(true);
                                                                editTextLocation.setEnabled(true);
                                                                editTextTime.setEnabled(true);
                                                                editTextDescription.setEnabled(true);

                                                                resetInput();
                                                                Toast.makeText(AddPhotoActivity.this, "Adding Post Successfull", Toast.LENGTH_SHORT).show();
                                                            }else{
                                                                Toast.makeText(AddPhotoActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPhotoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "Please do not leave blank!", Toast.LENGTH_SHORT).show();
        }





    }


    // this method is used to reset the activity after the image is saved

    public void resetInput(){
        editTextLocation.setText("");
        editTextTime.setText("");
        editTextDescription.setText("");
        imgPick.setImageResource(R.drawable.gorsel_sec);

    }

    @Override
    public void onBackPressed() {
        intent = new Intent(AddPhotoActivity.this, HomepageActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public void goMainActivityImage(View view) {
        intent = new Intent(AddPhotoActivity.this, HomepageActivity.class);
        startActivity(intent);
        finish();

    }
}