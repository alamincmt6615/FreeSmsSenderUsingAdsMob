package com.example.dreamsocialclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dreamsocialclub.home.HomeActivity;
import com.example.dreamsocialclub.model.SignUPModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    String mobile;
    String phone;
    String name;
    String email;
    String password;
    String current_time;
    String current_date;
    String profile_pic_url = "profile pic";
    private PreferenceData preferenceData;
    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        this.preferenceData = new PreferenceData(VerifyPhoneActivity.this);

        //getvalue from shard prifrienced
        name = preferenceData.getStringValue("name");
        email = preferenceData.getStringValue("email");
        password = preferenceData.getStringValue("password");
        phone = preferenceData.getStringValue("phone");

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);
        databaseReference = FirebaseDatabase.getInstance().getReference("user_info");

        //current date and time
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyy");
        current_date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        current_time = currentTime.format(calForTime.getTime());

        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
         mobile = intent.getStringExtra("mobile");
         //name = intent.getStringExtra("name");
         //mobile = preferenceData.getStringValue("mobile");
//        name = preferenceData.getStringValue("name");


        sendVerificationCode(mobile);


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

    }
    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88" + mobile,
                30,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };
    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(VerifyPhoneActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
//                            String current_user_uid = mAuth.getCurrentUser().getUid();
//
//                            SignUPModel signUPModel = new SignUPModel(current_user_uid,preferenceData.getStringValue("name"),mobile,profile_pic_url,current_date,current_time);
//                            databaseReference.child(current_user_uid).setValue(signUPModel);
                           // databaseReference.child(current_user_uid).child("name").setValue(preferenceData.getStringValue("name"));
                            sigUpWithGmailAndPassword();
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
    private void sigUpWithGmailAndPassword(){
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String uid = mAuth.getCurrentUser().getUid();
                    if (task.isSuccessful()){
                       SignUPModel signUPModel = new SignUPModel(uid,name,phone,profile_pic_url,current_date,current_time);
                    databaseReference.child(uid).setValue(signUPModel);
                        Toast.makeText(VerifyPhoneActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(VerifyPhoneActivity.this, "Error : "+task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
    }

}