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
    public void onSuccessUpdate(Activity activity, String title, String description, String imageUri, String sellerEmail,
                                String buyerEmail, String isActive, String token, ArrayList<String> bidderEmailList,
                                ArrayList<Integer> bidderPriceList, ArrayList<String> bidderReviewList) {

        ItemModel itemModel= new ItemModel( title,  description,  imageUri, sellerEmail,
                 buyerEmail,  isActive,  token, bidderEmailList,
                bidderPriceList, bidderReviewList);

        FirebaseFirestore.getInstance().collection("ItemData").document(token)
                .set(itemModel, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    viewItemMessage.onUpdateSuccess("Posted Successfully");
                }else {
                    viewItemMessage.onUpdateFailure("Check your internet !");
                    System.err.println(TAG+task.getException().getMessage());
                }
            }
        });
    }

}
