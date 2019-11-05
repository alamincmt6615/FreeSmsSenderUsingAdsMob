package com.example.dreamsocialclub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamsocialclub.PreferenceData;
import com.example.dreamsocialclub.R;
import com.example.dreamsocialclub.model.SignUPModel;
import com.example.dreamsocialclub.model.UploadPostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RetreivePersonalPost extends RecyclerView.Adapter<RetreivePersonalPost.ViewHolder> {

    PreferenceData preferenceData;

    private ArrayList<UploadPostModel> list = new ArrayList<>();
    private UploadPostModel uploadPostModel;
    private Context context;

    public RetreivePersonalPost(ArrayList<UploadPostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        uploadPostModel = list.get(position);

        if (preferenceData.getStringValue("profileUrl").length()>0) {
            Picasso.with(context).load(preferenceData.getStringValue("profileUrl")).placeholder(R.drawable.baby).into(holder.iv_profile_pic_post_layout);
        }
        holder.tv_uer_name_post_layout.setText(preferenceData.getStringValue("name"));
        holder.tv_comment_date_post_layout.setText(uploadPostModel.getPost_date()+" , ");
        holder.tv_comment_time_post_layout.setText(uploadPostModel.getPost_time());

        holder.tv_comment_text_post_layout.setText(uploadPostModel.getPost_text());

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
