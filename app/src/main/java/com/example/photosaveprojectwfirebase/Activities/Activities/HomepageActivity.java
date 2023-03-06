package com.example.photosaveprojectwfirebase.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.photosaveprojectwfirebase.Activities.Adapter.PostAdapter;
import com.example.photosaveprojectwfirebase.Activities.Holder.PostDetailsModel;
import com.example.photosaveprojectwfirebase.Activities.Holder.PostModel;
import com.example.photosaveprojectwfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {



    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    FirebaseUser user;

    List<PostModel> postModelList;
    RecyclerView recyclerView;
    PostAdapter postAdapter;

    static PostDetailsModel postDetails;



    public void init(){


        // Model
        postModelList = new ArrayList<>();

        // Firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.main_RecyclerView);
        postAdapter = new PostAdapter(this, postModelList);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(postAdapter);


    }



    // Menu for signing out and adding photo
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addPhoto){
            Intent intent = new Intent(getApplicationContext(), AddPhotoActivity.class);
            startActivity(intent);
            finish();
        }
        if(item.getItemId() == R.id.exit){
            exitApp();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // call the Initialize method
        init();

        // edit text in action bar
        getSupportActionBar().setTitle("Our Memories");


        getData();



        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PostModel postModel) {

                postDetails = new PostDetailsModel(postModel.getLocation(), postModel.getTime(), postModel.getDescription(), postModel.getImgUrl());
                Intent intent = new Intent(getApplicationContext(), PostDetailsActivity.class);
                startActivity(intent);

            }
        });


      swipeToDelete();



    }

    public void getData(){

        // we get user information
        user = auth.getCurrentUser();

        // We get posts that are equal to the uid of the user logged in from a collection under the name of records.

        firebaseFirestore.collection("Records").whereEqualTo("uid", user.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException exception) {

                        if(exception != null){
                            Toast.makeText(HomepageActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            if(snapshot != null){
                                if(!snapshot.isEmpty()){

                                    for (DocumentSnapshot snapshots: snapshot.getDocuments()){

                                        String documentId = (String) snapshots.get("documentId");
                                        String location = (String) snapshots.get("location");
                                        String time = (String) snapshots.get("time");
                                        String description = (String) snapshots.get("description");
                                        String imgUrl = (String) snapshots.get("imgUrl");
                                        String uid = (String) snapshots.get("uid");


                                        // We put the data we get into the postmodel class and put the object in the postModeList
                                        PostModel postModel = new PostModel(documentId,location, time, description, imgUrl, uid);
                                        postModelList.add(postModel);

                                        }

                                    postAdapter.notifyDataSetChanged();


                                    }

                                }
                            }
                        }


                });

    }

    // In this method, when we move the item to the left, it deletes it.

    public void swipeToDelete(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                postAdapter.deleteItem(viewHolder.getAdapterPosition());

                Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
                startActivity(intent);
                finish();

            }
        }).attachToRecyclerView(recyclerView);

    }

    // When exit is clicked on the menu, what needs to be done is written to the method.
    // it asks yes and no that you want to log out with an alert dialog

    public void exitApp(){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);

        alertdialog.setTitle("Exit");
        alertdialog.setMessage("Are you sure you want to sign out?");
        alertdialog.setCancelable(false);

        alertdialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertdialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);

            }
        });

        alertdialog.show();


    }



}