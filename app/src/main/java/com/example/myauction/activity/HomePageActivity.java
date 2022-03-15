package com.example.myauction.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.myauction.R;

import java.util.Objects;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.activity_home_page);
    }

    public void onViewAllItems(View view) {
    }

    public void onProfilePage(View view) {
    }

    public void onMenuClick(View view) {
    }
}