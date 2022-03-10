package com.example.myauction.auth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myauction.R;
import com.example.myauction.encryption.SymmtCrypto;
import com.example.myauction.userapi.UploadUserRecord;
import com.example.myauction.userapi.ViewUserMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements ViewUserMessage {

    EditText edName, edEmail, edPhone, edAddress, edPassword;
    UploadUserRecord uploadUserRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar ().hide (); //This Line hides the action bar
        setContentView(R.layout.activity_register);

        edName = findViewById(R.id.text_name);
        edEmail = findViewById(R.id.text_email);
        edAddress = findViewById(R.id.text_address);
        edPassword = findViewById(R.id.password);
        edPhone = findViewById(R.id.text_phone);

        uploadUserRecord = new UploadUserRecord(this);

    }

    private void validation(){
        String Name = edName.getText().toString().trim();
        String Email= edEmail.getText().toString().trim();
        String Address=  edAddress.getText().toString().trim();
        String Password= edPassword.getText().toString().trim();
        String Phone = edPhone.getText().toString().trim();

        if(!TextUtils.isEmpty(Name)
                && !TextUtils.isEmpty(Password)
                && !TextUtils.isEmpty(Email)
                && !TextUtils.isEmpty(Address)
                && !TextUtils.isEmpty(Phone)){
            //send info

            RegisterWithEmailAndPassword(Name,Address,Phone,Email,Password);

        }else{
            if(TextUtils.isEmpty(Name)){
                edName.setError("Name is required");
                return;
            }
            if (TextUtils.isEmpty(Phone)){
                edPhone.setError("Phone is required");
                return;
            }
            if(TextUtils.isEmpty(Address)){
                edAddress.setError("Address is required");
                return;
            }
            if (TextUtils.isEmpty(Email)){
                edEmail.setError("Email is required");
                return;
            }if (TextUtils.isEmpty(Password)){
                edPassword.setError("password is required");
                return;
            }


        }
    }
    private void RegisterWithEmailAndPassword(String name, String address, String phone, String email, String password)
    {
        FirebaseAuth auth=  FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(AuthResult authResult)
                    {
                        FirebaseUser user = auth.getCurrentUser();
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                SymmtCrypto symmtCrypto = new SymmtCrypto();
                                try {

                                    uploadUserRecord.onSuccessUpdate(RegisterActivity.this,symmtCrypto.encrypt(name),email, symmtCrypto.encrypt(password), symmtCrypto.encrypt(phone), symmtCrypto.encrypt(address));
                                    //send verification email
                                    //call verify ui
                                    Toast.makeText(RegisterActivity.this, "Verification email has been sent !",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterActivity.this, VerifyEmail.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(RegisterActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Try again later !",Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(RegisterActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onRegisterActivity(View view) {
        validation();
    }

    public void onLoginActivity(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateSuccess(String message) {
        Toast.makeText(RegisterActivity.this, message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(RegisterActivity.this, message,Toast.LENGTH_LONG).show();
    }
}