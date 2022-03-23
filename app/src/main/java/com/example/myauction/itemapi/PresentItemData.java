package com.example.myauction.itemapi;

import android.app.Activity;

import java.util.ArrayList;

public interface PresentItemData {
    void  onSuccessUpdate(Activity activity, String id, String title, String description, String imageUri,
                          String sellerEmail, String buyerEmail, String isActive, int startPrice,
                          int soldPrice, String[][] bidderList);

}
