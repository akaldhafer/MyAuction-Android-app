package com.example.myauction.userapi;

import android.app.Activity;

public interface PresentUserData {
    void  onSuccessUpdate(Activity activity, String name, String email, String password, String phone, String address);
}
