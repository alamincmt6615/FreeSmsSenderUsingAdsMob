package com.example.dreamsocialclub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamsocialclub.PreferenceData;
import com.example.dreamsocialclub.R;
import com.example.dreamsocialclub.model.SignUPModel;
import com.example.dreamsocialclub.model.UploadPostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RetrievePublicPost extends RecyclerView.Adapter<RetrievePublicPost.ViewHolder> {

    PreferenceData preferenceData;
    DatabaseReference databaseReference;

    private ArrayList<UploadPostModel> list = new ArrayList<>();
    private UploadPostModel uploadPostModel;
    private Context context;

    public RetrievePublicPost(ArrayList<UploadPostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RetrievePublicPost.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
        return new RetrievePublicPost.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RetrievePublicPost.ViewHolder holder, int position) {
        uploadPostModel = list.get(position);

        databaseReference = FirebaseDatabase.getInstance().getReference("user_info");
        databaseReference.child(uploadPostModel.getUser_uid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // SignUPModel signUPModel = dataSnapshot.getValue(SignUPModel.class);
                holder.tv_uer_name_post_layout.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                Picasso.with(context).load(String.valueOf(dataSnapshot.child("profile_pic_url").getValue())).placeholder(R.drawable.baby).into(holder.iv_profile_pic_post_layout);
                holder.tv_comment_date_post_layout.setText(uploadPostModel.getPost_date()+" , ");
                holder.tv_comment_time_post_layout.setText(uploadPostModel.getPost_time());
                holder.tv_comment_text_post_layout.setText(uploadPostModel.getPost_text());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_uer_name_post_layout;
        TextView tv_comment_date_post_layout;
        TextView tv_comment_time_post_layout;

        TextView tv_comment_text_post_layout;
        //TextView tv_like_count__post_layout;
        //EditText tv_post_comment_post_layout;
        ImageView iv_profile_pic_post_layout;
        //ImageButton btn_like_post_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            preferenceData = new PreferenceData(context);
            tv_uer_name_post_layout = itemView.findViewById(R.id.tv_uer_name_post_layout);
            tv_comment_date_post_layout = itemView.findViewById(R.id.tv_comment_date_post_layout);
            tv_comment_time_post_layout = itemView.findViewById(R.id.tv_comment_time_post_layout);
            tv_comment_text_post_layout = itemView.findViewById(R.id.tv_comment_text_post_layout);
            // tv_like_count__post_layout = itemView.findViewById(R.id.tv_like_count__post_layout);
            // tv_post_comment_post_layout = itemView.findViewById(R.id.tv_post_comment_post_layout);
            iv_profile_pic_post_layout = itemView.findViewById(R.id.iv_profile_pic_post_layout);
            //btn_like_post_layout = itemView.findViewById(R.id.btn_like_post_layout);
        }
    }
}
