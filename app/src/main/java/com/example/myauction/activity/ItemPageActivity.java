package com.example.myauction.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ItemPageActivity extends AppCompatActivity implements ViewItemFetchMessage {
    private RecyclerView ListDataView;
    private NewItemAdapter Adapter;
    ArrayList<ItemModel> arrayList = new ArrayList<>();

    String id, title, description, imageUri, sellerEmail, buyerEmail, isActive;
    int startPrice, soldPrice;
    int currentPrice;
    ArrayList<String> bidderEmailList= new ArrayList<String>();
    ArrayList<String> bidderPriceList = new ArrayList<String>();
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
        bidderEmailList =getIntent().getStringArrayListExtra("bidderEmailList");
        bidderPriceList =getIntent().getStringArrayListExtra("bidderPriceList");

        int size = bidderPriceList.size();
        //set the current room view
        vTitle.setText(title);
        vDesc.setText(description);
        if(bidderPriceList != null){
            vBidder.setText(bidderEmailList.size()+" Bidder");
            String cprice = bidderPriceList.get(size-1);
            currentPrice = Integer.parseInt(cprice);

            vCurrentPrice.setText("Current Price: "+currentPrice+" RM");
        }else{
            currentPrice = startPrice;
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
        if(message != null && message.getIsActive().equals("active") && !message.getId().equals(id)){
            ItemModel Model = new ItemModel(message.getId(),message.getTitle(),message.getDescription(),message.getImageUri(),
                    message.getSellerEmail(),message.getBuyerEmail(),message.getIsActive(),message.getStartPrice(),message.getSoldPrice()
                    ,message.getBidderEmailList(), message.getBidderPriceList());
            arrayList.add(Model);

        }
        Adapter.notifyDataSetChanged();
    }


    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(ItemPageActivity.this, message, Toast.LENGTH_LONG).show();

    }

    public void onViewAllItems(View view) {
        Intent intent = new Intent(ItemPageActivity.this, ViewAllItems.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
        int bidPrice = Integer.parseInt(edBidingPrice.getText().toString().trim());
        String bemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if(edBidingPrice.getText() != null && bidPrice > currentPrice && !bemail.equals(sellerEmail)){
            //add the user to the bidding list
            bidderEmailList.add(bemail);
            bidderPriceList.add(edBidingPrice.getText().toString());
            System.out.println(bidderPriceList+"  "+bidderEmailList);
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference record = firebaseFirestore.collection("ItemData").document(id);
            record.update("bidderEmailList",bidderEmailList,"bidderPriceList",bidderPriceList).addOnSuccessListener(new OnSuccessListener< Void >() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(view.getContext(), "Bid Successfully", Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{
            if(bemail.equals(sellerEmail)){
                Toast.makeText(view.getContext(), "You cannot bid on your item !", Toast.LENGTH_LONG).show();
            }
            if(bidPrice <= currentPrice){
                Toast.makeText(view.getContext(), "The bidding price should be greater than the current price", Toast.LENGTH_LONG).show();
            }
            if(edBidingPrice.getText() == null){
                edBidingPrice.setError("Please Enter Bid Price");
            }
        }


    }
}