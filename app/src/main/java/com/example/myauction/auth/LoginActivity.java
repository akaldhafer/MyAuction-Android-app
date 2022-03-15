package com.example.myauction.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myauction.R;
import com.example.myauction.activity.HomePageActivity;
import com.example.myauction.activity.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText email , password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar

        setContentView(R.layout.activity_login);
        email = findViewById(R.id.uemail);
        password = findViewById(R.id.upassword);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onLoginActivity(View view) {
        validate();

    }
    void validate(){
        String uEmail = email.getText().toString().trim();
        String uPassword = password.getText().toString().trim();

        if (!TextUtils.isEmpty(uEmail)
                && !TextUtils.isEmpty(uPassword)) {
            SignInWithEmailAndPassword(uEmail, uPassword);
        } else {
            if (TextUtils.isEmpty(uEmail)) {
                email.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(uPassword)) {
                password.setError("Password is required");
            }

        }
    }

    void SignInWithEmailAndPassword(String uEmail, String uPassword){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(uEmail,uPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        boolean isVerified = Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified();
                        if(isVerified){
                            Toast.makeText(LoginActivity.this, "Welcome Back", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "Please verify your email first", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, VerifyEmail.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    public void onRegisterActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}