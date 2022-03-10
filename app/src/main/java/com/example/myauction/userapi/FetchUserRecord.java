package com.example.myauction.userapi;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.myauction.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FetchUserRecord implements PresentUserFetchData{
    private Context context;
    private ViewUserFetchMessage viewUserFetchMessage;

    public FetchUserRecord(Context context, ViewUserFetchMessage viewUserFetchMessage) {
        this.context = context;
        this.viewUserFetchMessage = viewUserFetchMessage;
    }

    @Override
    public void onSuccessUpdate(Activity activity) {

        FirebaseFirestore.getInstance().collection("UserData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(int i=0; i<task.getResult().size();i++){
                        String name = task.getResult().getDocuments().get(i).getString("name");
                        String email= task.getResult().getDocuments().get(i).getString("email");
                        String password= task.getResult().getDocuments().get(i).getString("password");
                        String phone= task.getResult().getDocuments().get(i).getString("phone");
                        String address= task.getResult().getDocuments().get(i).getString("address");

                        UserModel userModel= new UserModel(name,email,password,phone,address);
                        //send data to activity
                        viewUserFetchMessage.onUpdateSuccess(userModel);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                viewUserFetchMessage.onUpdateFailure(e.getMessage().toString());
            }
        });
    }
}
