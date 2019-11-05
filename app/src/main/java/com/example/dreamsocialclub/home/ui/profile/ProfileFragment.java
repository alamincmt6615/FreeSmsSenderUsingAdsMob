package com.example.dreamsocialclub.home.ui.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.dreamsocialclub.PreferenceData;
import com.example.dreamsocialclub.R;
import com.example.dreamsocialclub.adapter.RetreivePersonalPost;
import com.example.dreamsocialclub.model.UploadPostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private EditText et_post_text;
    private TextView tv_profile_name;
    private final int PIC_IMAGE_REQUEST = 420;
    private Uri filepath;
    private ProgressDialog progressDialog;
    private Spinner sp_privacy;
    private Button post_btn;
    private PreferenceData preferenceData;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferencePost;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private RecyclerView rv_profile_fragment;
    private RetreivePersonalPost retreivePersonalPost;
    private ImageView choose_image;
    private CircularImageView profile_picture;

    String uid;
    private ArrayList<UploadPostModel> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        this.preferenceData = new PreferenceData(getActivity());

        //current user uid
        firebaseAuth = FirebaseAuth.getInstance();
         uid = firebaseAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        databaseReferencePost = FirebaseDatabase.getInstance().getReference("user_info");
        et_post_text = root.findViewById(R.id.et_post_text);
        tv_profile_name = root.findViewById(R.id.tv_profile_name);
        choose_image = root.findViewById(R.id.choose_image);
        choose_image.setOnClickListener(this);
        profile_picture = root.findViewById(R.id.profile_picture);


        rv_profile_fragment = root.findViewById(R.id.rv_profile_fragment);
        tv_profile_name.setText(preferenceData.getStringValue("name"));
        user_post_retrieve(uid);
        post_btn = root.findViewById(R.id.post_btn);
        post_btn.setOnClickListener(this);
//        post_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                user_post(uid);
//            }
//        });

        return root;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.choose_image:
                chooseimage();
                break;
            case R.id.post_btn:
                user_post(uid);
                break;

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Picasso.with(getActivity()).load(preferenceData.getStringValue("profileUrl")).placeholder(R.drawable.baby).into(profile_picture);

    }
    private void chooseimage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PIC_IMAGE_REQUEST);
        Toast.makeText(getActivity(), "plz choose image first", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null){
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filepath);
                profile_picture.setImageBitmap(bitmap);
                if (bitmap != null){
                    uploadimage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadimage() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("profile_pic");
        firebaseAuth = FirebaseAuth.getInstance();
        final String user_uid = firebaseAuth.getCurrentUser().getUid();

        if (filepath != null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child(user_uid+".jpg");
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReferencePost.child(user_uid).child("profile_pic_url").setValue(uri.toString());
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading "+(int)progress+"%");
                        }
                    });

        }
    }
    private void user_post(String uid) {


        //pic up current date
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
        String saveCurrentDate = currentDate.format(calForDate.getTime());

        // pic up current time
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime = currentTime.format(calForTime.getTime());

        String random_key = uid + saveCurrentDate + saveCurrentTime;

        String post_text = et_post_text.getText().toString().trim();

        if (TextUtils.isEmpty(post_text)){
            Toast.makeText(getActivity(), "Write your post first", Toast.LENGTH_SHORT).show();
            return;
        }else {
            UploadPostModel uploadPostModel = new UploadPostModel(post_text,uid,saveCurrentTime,saveCurrentDate);
            databaseReference.child(random_key).setValue(uploadPostModel);
            et_post_text.setText("");
        }
    }
    private void user_post_retrieve(final String uid){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    UploadPostModel uploadPostModel = dataSnapshot1.getValue(UploadPostModel.class);
                    if (uid.equals(uploadPostModel.getUser_uid())){
                        list.add(uploadPostModel);
                        String nam = uploadPostModel.getPost_text();
                        Log.d("text",nam);
                    }
                }
                if (list.size()>0){
                    retreivePersonalPost = new RetreivePersonalPost(list,getActivity());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rv_profile_fragment.setLayoutManager(mLayoutManager);
                    rv_profile_fragment.setItemAnimator(new DefaultItemAnimator());
                    rv_profile_fragment.setAdapter(retreivePersonalPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}