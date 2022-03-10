package com.example.myauction.userapi;

import com.example.myauction.model.UserModel;

public interface ViewUserFetchMessage {

    void onUpdateSuccess(UserModel message);
    void onUpdateFailure(String message);
}
