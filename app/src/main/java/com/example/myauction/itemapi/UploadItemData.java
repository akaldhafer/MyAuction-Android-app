package com.example.myauction.itemapi;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.myauction.model.ItemModel;
import com.example.myauction.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class UploadItemData implements PresentItemData, ViewItemMessage {
    private ViewItemMessage viewItemMessage;
    private static final  String TAG = "Upload User Data";

    public UploadItemData(ViewItemMessage viewUserMessage) {
        this.viewItemMessage = viewUserMessage;
    }

    @Override
    public void onUpdateSuccess(String message) {
        viewItemMessage.onUpdateSuccess(message);

    }

    @Override
    public void onUpdateFailure(String message) {
        viewItemMessage.onUpdateFailure(message);
    }

    @Override
    public void onSuccessUpdate(Activity activity, String id, String title, String description, String imageUri,
                                String sellerEmail, String buyerEmail, String isActive, int startPrice,
                                int soldPrice, String[][] bidderList) {

        ItemModel itemModel= new ItemModel( id,  title,  description,  imageUri,
                sellerEmail,  buyerEmail,  isActive,  startPrice,
                soldPrice,  bidderList);

        FirebaseFirestore.getInstance().collection("ItemData").document(id)
                .set(itemModel, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    viewItemMessage.onUpdateSuccess("item Successfully");
                }else {
                    viewItemMessage.onUpdateFailure("Check your internet !");
                    System.err.println(TAG+task.getException().getMessage());
                }
            }
        });
    }

}
