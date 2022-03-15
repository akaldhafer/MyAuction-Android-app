package com.example.myauction.itemapi;

import com.example.myauction.model.ItemModel;

public interface ViewItemFetchMessage {
    void onUpdateSuccess(ItemModel message);
    void onUpdateFailure(String message);
}
