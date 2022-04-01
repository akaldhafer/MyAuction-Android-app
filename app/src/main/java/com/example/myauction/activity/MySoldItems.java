package com.example.myauction.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myauction.R;
import com.example.myauction.adapter.PurchasesItemAdapter;
import com.example.myauction.adapter.SoldItemAdapter;
import com.example.myauction.itemapi.FetchItemData;
import com.example.myauction.itemapi.ViewItemFetchMessage;
import com.example.myauction.model.ItemModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class MySoldItems extends AppCompatActivity implements ViewItemFetchMessage {
    private RecyclerView ListDataView;
    private SoldItemAdapter Adapter;
    TextView title;
    ArrayList<ItemModel> arrayList = new ArrayList<>();
    String email;
    ImageView menu, profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.view_list);
        title = findViewById(R.id.pageTitle);
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        title.setText("My Sold Item List");

        menu = findViewById(R.id.onMenu);
        profile= findViewById(R.id.onProfile);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySoldItems.this, UserMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySoldItems.this, UserProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
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

        Adapter = new SoldItemAdapter(this, arrayList);
        ListDataView.setAdapter(Adapter);
        ListDataView.invalidate();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdateSuccess(ItemModel message) {
        if(message != null && message.getSellerEmail().equals(email) && message.getIsActive().equals("sold")){

            ItemModel Model = new ItemModel(message.getId(),message.getTitle(),message.getDescription(),message.getImageUri(),
                    message.getSellerEmail(),message.getBuyerEmail(),message.getIsActive(),message.getStartPrice(),message.getSoldPrice()
                    ,message.getBidderEmailList(), message.getBidderPriceList());
            arrayList.add(Model);
        }
        Adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MySoldItems.this, UserMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(MySoldItems.this, message, Toast.LENGTH_LONG).show();

    }


}