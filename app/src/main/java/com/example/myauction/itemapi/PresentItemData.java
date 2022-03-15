package com.example.myauction.itemapi;

import android.app.Activity;

import java.util.ArrayList;

public interface PresentItemData {
    void  onSuccessUpdate(Activity activity, String title, String description, String imageUri, String sellerEmail,
                          String buyerEmail, String isActive, String token, ArrayList<String> bidderEmailList,
                          ArrayList<Integer> bidderPriceList, ArrayList<String> bidderReviewList);

}
