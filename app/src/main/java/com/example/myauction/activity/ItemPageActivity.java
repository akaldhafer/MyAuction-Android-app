package com.example.myauction.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myauction.R;
import com.example.myauction.adapter.NewItemAdapter;
import com.example.myauction.itemapi.FetchItemData;
import com.example.myauction.itemapi.ViewItemFetchMessage;
import com.example.myauction.model.ItemModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ItemPageActivity extends AppCompatActivity implements ViewItemFetchMessage {
    private RecyclerView ListDataView;
    private NewItemAdapter Adapter;
    ArrayList<ItemModel> arrayList = new ArrayList<>();

    String id, title, description, imageUri, sellerEmail, buyerEmail, isActive;
    int startPrice, soldPrice, currentPrice, size;
    String[][] bidderList;
    TextView vTitle, vDesc, vPrice, vBidder, vCurrentPrice;
    EditText edBidingPrice;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.activity_item_page);
        vTitle = findViewById(R.id.pTitle);
        vDesc = findViewById(R.id.pDesc);
        vPrice = findViewById(R.id.pPrice);
        vBidder = findViewById(R.id.pBiddingNo);
        vCurrentPrice = findViewById(R.id.pCurrentPrice);
        edBidingPrice = findViewById(R.id.pBiddingPrice);
        imageView = findViewById(R.id.pImage);


        //get current room
        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");
        sellerEmail = getIntent().getStringExtra("sellerEmail");
        buyerEmail = getIntent().getStringExtra("buyerEmail");
        isActive = getIntent().getStringExtra("isActive");
        description = getIntent().getStringExtra("description");
        imageUri = getIntent().getStringExtra("imageUri");
        startPrice = getIntent().getIntExtra("startPrice",0);
        soldPrice = getIntent().getIntExtra("soldPrice",0);
        bidderList =(String[][]) getIntent().getSerializableExtra("bidderList");
        //set the current room view
        vTitle.setText(title);
        vDesc.setText(description);
        if(bidderList != null){
            vBidder.setText(bidderList.length+" Bidder");
            for(int i = 0; i< bidderList.length;i++){
                currentPrice = Integer.parseInt(bidderList[i][1]);
            }
            vCurrentPrice.setText("Current Price: "+currentPrice+" RM");
        }else{
            vBidder.setText("0 Bidder");
            vCurrentPrice.setText("Current Price: "+startPrice+" RM");
        }
        vPrice.setText("Start Price: "+startPrice+" RM");
        Picasso.with(this).load(imageUri).fit().into(imageView);


        ListDataView = findViewById(R.id.SimilarListView);
        FetchItemData FetchData = new FetchItemData(this, this);

        RecyclerViewMethod();
        FetchData.onSuccessUpdate(this);
    }
    public void RecyclerViewMethod() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ListDataView.setLayoutManager(manager);
        ListDataView.setItemAnimator(new DefaultItemAnimator());
        ListDataView.setHasFixedSize(true);

        Adapter = new NewItemAdapter(this, arrayList);
        ListDataView.setAdapter(Adapter);
        ListDataView.invalidate();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdateSuccess(ItemModel message) {
        if(message != null && message.getIsActive().equals("yes") && !message.getId().equals(id)){
            ItemModel Model = new ItemModel(message.getId(),message.getTitle(),message.getDescription(),message.getImageUri(),
                    message.getSellerEmail(),message.getBuyerEmail(),message.getIsActive(),message.getStartPrice(),message.getSoldPrice()
                    ,message.getBidderList());
            arrayList.add(Model);

        }
        Adapter.notifyDataSetChanged();
    }


    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(ItemPageActivity.this, message, Toast.LENGTH_LONG).show();

    }

    public void onViewAllItems(View view) {
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ItemPageActivity.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onHomeClick(View view) {
        Intent intent = new Intent(ItemPageActivity.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onBidding(View view) {
    }
}