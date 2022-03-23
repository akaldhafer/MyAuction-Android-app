package com.example.myauction.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myauction.R;
import com.example.myauction.auth.LoginActivity;
import com.example.myauction.encryption.SymmtCrypto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    TextView vEmail;
    EditText edName, edPhone, edAddress;
    String email, name, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide(); //This Line hides the action bar
        setContentView(R.layout.activity_user_profile);

        vEmail  =findViewById(R.id.profile_Email);
        edAddress = findViewById(R.id.profileAddress);
        edName = findViewById(R.id.profile_Name);
        edPhone = findViewById(R.id.profilePhone);
        //fetch current user information
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()).trim();
        vEmail.setText(email);
        //fetch user name
        FirebaseFirestore.getInstance().collection("UserData").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    SymmtCrypto crypto = new SymmtCrypto();
                    try {
                        name =crypto.decrypt(task.getResult().getString("name"));
                        phone = crypto.decrypt(task.getResult().getString("phone"));
                        address = crypto.decrypt(task.getResult().getString("address"));

                        edName.setText(name);
                        edAddress.setText(address);
                        edPhone.setText(phone);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this, "Check your internet Connection !", Toast.LENGTH_LONG).show();
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validation(){
        String Name = edName.getText().toString().trim();
        String Address=  edAddress.getText().toString().trim();
        String Phone = edPhone.getText().toString().trim();

        if(!TextUtils.isEmpty(Name)
                && !TextUtils.isEmpty(Address)
                && !TextUtils.isEmpty(Phone)){
            //send info
            updateUserInfo(Name,Address,Phone);

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
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void updateUserInfo(String Name, String Address, String Phone){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference record = firebaseFirestore.collection("UserData").document(email);
        SymmtCrypto d = new SymmtCrypto();

        try {
            String updatedName = d.encrypt(Name);
            String updatedAddress = d.encrypt(Address);
            String updatedPhone = d.encrypt(Phone);

            record.update("name",updatedName, "address", updatedAddress, "phone", updatedPhone)
                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UserProfile.this, "Profile updated !", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserProfile.this, "Could not update now !",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void onClickLogOutButton(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserProfile.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickUpdateButton(View view) {
        validation();
    }

    public void onMenuClick(View view) {
        Intent intent = new Intent(UserProfile.this, UserMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfile.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onHomePage(View view) {
        Intent intent = new Intent(UserProfile.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}