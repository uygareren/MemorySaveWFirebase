package com.example.photosaveprojectwfirebase.Activities.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosaveprojectwfirebase.Activities.Holder.PostModel;
import com.example.photosaveprojectwfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    Context context;
    List<PostModel> postModelList;
    OnItemClickListener listener;

    FirebaseFirestore firestore;
    FirebaseAuth auth;



    public PostAdapter(Context context, List<PostModel> postModelList) {
        this.context = context;
        this.postModelList = postModelList;

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.home_img_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        PostModel postModel = postModelList.get(position);
        holder.location.setText(postModel.getLocation());
        holder.time.setText(postModel.getTime());
        holder.description.setText(postModel.getDescription());
        Picasso.get().load(postModel.getImgUrl()).into(holder.postImg);




    }

    public void deleteItem(int position){
        firestore.collection("Records").document(postModelList.get(position).getDocumentId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            postModelList.remove(postModelList.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();



                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView postImg;
        TextView location, time, description;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postImg = itemView.findViewById(R.id.home_img_item_ImageView);
            location = itemView.findViewById(R.id.home_img_item_location);
            time = itemView.findViewById(R.id.home_img_item_time);
            description = itemView.findViewById(R.id.home_img_item_description);

            postImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(postModelList.get(position));
                    }
                }
            });






        }


    }

    public interface OnItemClickListener{
        void onItemClick(PostModel postModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


}
