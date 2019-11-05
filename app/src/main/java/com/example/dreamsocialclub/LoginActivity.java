package com.example.dreamsocialclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dreamsocialclub.home.HomeActivity;
import com.example.dreamsocialclub.model.SignUPModel;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
   private TextView tv_login_need_account;
   private Button btn_login;
   private FirebaseAuth firebaseAuth;
   private DatabaseReference databaseReference;
   private PreferenceData preferenceData;
   private EditText user_phoneNumber,user_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.preferenceData = new PreferenceData(LoginActivity.this);
        databaseReference = FirebaseDatabase.getInstance().getReference("user_info");
        intview();
        tv_login_need_account.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    private void intview() {
        user_phoneNumber = findViewById(R.id.et_user_name);
        user_pass = findViewById(R.id.et_user_password);
        btn_login = findViewById(R.id.btn_login);
        tv_login_need_account = findViewById(R.id.tv_login_need_account);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_login_need_account:
                Intent gotosignup = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(gotosignup);
                break;
            case R.id.btn_login:
                user_login();
                break;
        }

    }

    private void user_login() {
        firebaseAuth = FirebaseAuth.getInstance();
        String phone = user_phoneNumber.getText().toString().trim();
        String pass = user_pass.getText().toString().trim();

        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Phone Number and Pass. is required", Toast.LENGTH_SHORT).show();
            return;
        }else {
            String email = phone+"@gmail.com";
            firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //retreiveCurrientUserData();
                        Toast.makeText(LoginActivity.this, "Log In Successfully", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                   startActivity(intent);
                    }else {
                        Toast.makeText(LoginActivity.this, "Error Occurred : "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            })
            .addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toast.makeText(LoginActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
