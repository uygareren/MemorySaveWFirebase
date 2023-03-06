package com.example.photosaveprojectwfirebase.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photosaveprojectwfirebase.Activities.Holder.PostModel;
import com.example.photosaveprojectwfirebase.R;
import com.squareup.picasso.Picasso;

public class PostDetailsActivity extends AppCompatActivity {

    ImageView detail_img;
    TextView detail_location, detail_time, detail_description;

    String str_location, str_time, str_description, str_ImgUrl;



    PostModel postModel = null;

    public void init() {
        detail_img = findViewById(R.id.activityDetails_Resim);
        detail_location = findViewById(R.id.activityDetails_Konum);
        detail_time = findViewById(R.id.activityDetails_Zaman);
        detail_description = findViewById(R.id.activityDetails_Aciklama);

        str_location = HomepageActivity.postDetails.getLocation();
        str_time = HomepageActivity.postDetails.getTime();
        str_description = HomepageActivity.postDetails.getDescription();
        str_ImgUrl = HomepageActivity.postDetails.getImg_url();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);


        init();
        getSupportActionBar().hide();



        detail_location.setText(str_location);
        detail_time.setText(str_time);
        detail_description.setText(str_description);
        Picasso.get().load(str_ImgUrl).into(detail_img);




        }


    public void onBackPressed() {
        Intent intent = new Intent(this, HomepageActivity.class);
        finish();
        startActivity(intent);
        super.onBackPressed();

    }


    public void goBackHomeIcon(View view) {
        Intent intent = new Intent(this, HomepageActivity.class);
        finish();
        startActivity(intent);
        super.onBackPressed();

    }
}

