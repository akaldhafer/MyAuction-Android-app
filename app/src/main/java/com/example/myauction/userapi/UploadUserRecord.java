package com.example.myauction.userapi;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.myauction.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class UploadUserRecord implements PresentUserData, ViewUserMessage{
    private ViewUserMessage viewUserMessage;
    private static final  String TAG = "Upload User Data";

    public UploadUserRecord(ViewUserMessage viewUserMessage) {
        this.viewUserMessage = viewUserMessage;
    }

    @Override
    public void onUpdateSuccess(String message) {
        viewUserMessage.onUpdateSuccess(message);

    }

    @Override
    public void onUpdateFailure(String message) {
        viewUserMessage.onUpdateFailure(message);
    }

    @Override
    public void onSuccessUpdate(Activity activity, String name, String email, String password, String phone, String address) {
        UserModel userModel= new UserModel(name, email, password, phone, address);

        FirebaseFirestore.getInstance().collection("UserData").document(email)
                .set(userModel, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    viewUserMessage.onUpdateSuccess("Registered Successfully");
                }else {
                    viewUserMessage.onUpdateFailure("Check your internet !");
                    System.err.println(TAG+task.getException().getMessage().toString());
                }
            }
        });
    }

}
