package com.example.dreamsocialclub.home.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamsocialclub.CasinoActivity;
import com.example.dreamsocialclub.FreeSmsActivity;
import com.example.dreamsocialclub.PublicPostActivity;
import com.example.dreamsocialclub.R;
import com.example.dreamsocialclub.adapter.RetrievePublicPost;
import com.example.dreamsocialclub.model.UploadPostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private RecyclerView rv_public_post;
    private RetrievePublicPost retrievePublicPost;
    private ArrayList<UploadPostModel> publicpostlist = new ArrayList<>();
   DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        rv_public_post = root.findViewById(R.id.rv_public_post);
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");

        retreivePublicPost();

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){



        }

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
                    retrievePublicPost = new RetrievePublicPost(publicpostlist,getActivity());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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