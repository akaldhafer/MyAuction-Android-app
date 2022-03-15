package com.example.myauction.itemapi;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.myauction.model.ItemModel;
import com.example.myauction.model.UserModel;
import com.example.myauction.userapi.ViewUserFetchMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FetchItemData implements PresentItemFetchData {
    private Context context;
    private ViewItemFetchMessage viewItemFetchMessage;

    public FetchItemData(Context context, ViewItemFetchMessage viewItemFetchMessage) {
        this.context = context;
        this.viewItemFetchMessage = viewItemFetchMessage;
    }

    @Override
    public void onSuccessUpdate(Activity activity) {

        FirebaseFirestore.getInstance().collection("ItemData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(int i=0; i<task.getResult().size();i++){

                        String title = task.getResult().getDocuments().get(i).getString("title");
                        String description= task.getResult().getDocuments().get(i).getString("description");
                        String imageUri= task.getResult().getDocuments().get(i).getString("imageUri");
                        String sellerEmail= task.getResult().getDocuments().get(i).getString("sellerEmail");
                        String buyerEmail= task.getResult().getDocuments().get(i).getString("buyerEmail");
                        String isActive= task.getResult().getDocuments().get(i).getString("isActive");
                        String token= task.getResult().getDocuments().get(i).getString("token");
                        ArrayList<String> bidderEmailList= (ArrayList<String>) task.getResult().getDocuments().get(i).get("bidderEmailList");
                        ArrayList<Integer> bidderPriceList=(ArrayList<Integer>) task.getResult().getDocuments().get(i).get("bidderPriceList");
                        ArrayList<String> bidderReviewList=(ArrayList<String>) task.getResult().getDocuments().get(i).get("bidderReviewList");

                        ItemModel itemModel= new ItemModel(title,  description,  imageUri, sellerEmail,
                                buyerEmail,  isActive,  token, bidderEmailList,
                                bidderPriceList, bidderReviewList);
                        //send data to activity
                        viewItemFetchMessage.onUpdateSuccess(itemModel);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                viewItemFetchMessage.onUpdateFailure(e.getMessage());
            }
        });
    }
}

