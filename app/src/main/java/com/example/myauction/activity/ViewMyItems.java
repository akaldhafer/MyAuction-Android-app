package com.example.myauction.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myauction.R;
import com.example.myauction.adapter.ViewMyItemAdapter;
import com.example.myauction.itemapi.FetchItemData;
import com.example.myauction.itemapi.ViewItemFetchMessage;
import com.example.myauction.model.ItemModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class ViewMyItems extends AppCompatActivity implements ViewItemFetchMessage {
    private RecyclerView ListDataView;
    private ViewMyItemAdapter Adapter;
    TextView title;
    ArrayList<ItemModel> arrayList = new ArrayList<>();
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.view_list);
        title = findViewById(R.id.pageTitle);
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        title.setText("View My Items");
        //get the similar room
        ListDataView = findViewById(R.id.ListView);

        FetchItemData FetchData = new FetchItemData(this, this);

        RecyclerViewMethod();
        FetchData.onSuccessUpdate(this);
    }
    public void RecyclerViewMethod() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ListDataView.setLayoutManager(manager);
        ListDataView.setItemAnimator(new DefaultItemAnimator());
        ListDataView.setHasFixedSize(true);

        Adapter = new ViewMyItemAdapter(this, arrayList);
        ListDataView.setAdapter(Adapter);
        ListDataView.invalidate();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdateSuccess(ItemModel message) {
        if(message != null && message.getSellerEmail().equals(email)){
            ItemModel Model = new ItemModel(message.getId(),message.getTitle(),message.getDescription(),message.getImageUri(),
                    message.getSellerEmail(),message.getBuyerEmail(),message.getIsActive(),message.getStartPrice(),message.getSoldPrice()
            ,message.getBidderList());
            arrayList.add(Model);

        }
        Adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ViewMyItems.this, UserMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(ViewMyItems.this, message, Toast.LENGTH_LONG).show();

    }


}