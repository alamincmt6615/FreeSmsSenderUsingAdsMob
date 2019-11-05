package com.example.dreamsocialclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.dreamsocialclub.adapter.RetreivePersonalPost;
import com.example.dreamsocialclub.adapter.RetrievePublicPost;
import com.example.dreamsocialclub.model.UploadPostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PublicPostActivity extends AppCompatActivity {
    private DatabaseReference databaseReference,databaseReference_user;
    private RecyclerView rv_public_post;
    private FirebaseAuth firebaseAuth;
    private RetrievePublicPost retrievePublicPost;
    private ArrayList<UploadPostModel> publicpostlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_post);
        rv_public_post = findViewById(R.id.rv_public_post);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        databaseReference_user = FirebaseDatabase.getInstance().getReference("User");
       //retrieve data for public post
        retreivePublicPost();
    }

    private void retreivePublicPost() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                publicpostlist.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    UploadPostModel uploadPostModel = dataSnapshot1.getValue(UploadPostModel.class);
                    publicpostlist.add(uploadPostModel);
                }
                if (publicpostlist.size()>0){
                    retrievePublicPost = new RetrievePublicPost(publicpostlist,PublicPostActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PublicPostActivity.this);
                    rv_public_post.setLayoutManager(mLayoutManager);
                    rv_public_post.setItemAnimator(new DefaultItemAnimator());
                    rv_public_post.setAdapter(retrievePublicPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
