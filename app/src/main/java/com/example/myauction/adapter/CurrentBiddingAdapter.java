package com.example.myauction.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myauction.R;
import com.example.myauction.model.ItemModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CurrentBiddingAdapter extends RecyclerView.Adapter<CurrentBiddingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemModel> arrayList = new ArrayList<>();

    public CurrentBiddingAdapter(Context context, ArrayList<ItemModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CurrentBiddingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_current_bidding_item, parent, false);
        CurrentBiddingAdapter.ViewHolder holder = new CurrentBiddingAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentBiddingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String id, title, description, imageUri, sellerEmail, buyerEmail, isActive;
        int startPrice, soldPrice;
        ArrayList<String> bidderEmailList = new ArrayList<>();
        ArrayList<String> bidderPriceList = new ArrayList<>();

        isActive = arrayList.get(holder.getAdapterPosition()).getIsActive();
        id= arrayList.get(holder.getAdapterPosition()).getId();
        title = arrayList.get(holder.getAdapterPosition()).getTitle();
        description =arrayList.get(holder.getAdapterPosition()).getDescription();
        imageUri = arrayList.get(position).getImageUri();
        startPrice = arrayList.get(holder.getAdapterPosition()).getStartPrice();
        sellerEmail= arrayList.get(holder.getAdapterPosition()).getSellerEmail();
        buyerEmail= arrayList.get(holder.getAdapterPosition()).getBuyerEmail();
        soldPrice= arrayList.get(holder.getAdapterPosition()).getSoldPrice();
        bidderEmailList.addAll(arrayList.get(holder.getAdapterPosition()).getBidderEmailList());
        bidderPriceList.addAll(arrayList.get(holder.getAdapterPosition()).getBidderPriceList());

        holder.vTitle.setText(title);
        holder.vDesc.setText(description);
        int size = bidderPriceList.size();
        int currentPrice;
        //set the current room view
        if(bidderPriceList != null){
            holder.vBidder.setText(bidderEmailList.size()+" Bidder");
            String cprice = bidderPriceList.get(size-1);
            currentPrice = Integer.parseInt(cprice);
            holder.vCurrentPrice.setText("Current Price: "+cprice+" RM");
        }else{
            currentPrice = startPrice;
            holder.vBidder.setText("0 Bidder");
            holder.vCurrentPrice.setText("Current Price: "+startPrice+" RM");
        }
        holder.vStartPrice.setText("Start Price: "+startPrice+" RM");
        //set the image
        Picasso.with(this.context).load(imageUri).fit().into(holder.imageView);

        //bid
        holder.btBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bidPrice = Integer.parseInt(holder.edBidingPrice.getText().toString().trim());
                String bemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                if(holder.edBidingPrice.getText() != null && bidPrice > currentPrice && !bemail.equals(sellerEmail)){
                    //add the user to the bidding list
                    bidderEmailList.add(bemail);
                    bidderPriceList.add(holder.edBidingPrice.getText().toString());
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
                    if(holder.edBidingPrice.getText() == null){
                        holder.edBidingPrice.setError("Please Enter Bid Price");
                    }
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView vTitle, vDesc, vStartPrice, vBidder, vCurrentPrice;
        EditText edBidingPrice;
        Button btBid;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vTitle = itemView.findViewById(R.id.cTitle);
            vDesc = itemView.findViewById(R.id.cDesc);
            vStartPrice = itemView.findViewById(R.id.cstartPrice);
            vCurrentPrice = itemView.findViewById(R.id.cCurrentPrice);
            vBidder = itemView.findViewById(R.id.cBiddingNo);
            edBidingPrice = itemView.findViewById(R.id.cBiddingPrice);
            imageView = itemView.findViewById(R.id.cImage);
            btBid = itemView.findViewById(R.id.cbtBid);
        }
    }
}