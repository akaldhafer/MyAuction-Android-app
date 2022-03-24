package com.example.myauction.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myauction.R;
import com.example.myauction.activity.ItemPageActivity;
import com.example.myauction.model.ItemModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewItemAdapter extends RecyclerView.Adapter<NewItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemModel> arrayList = new ArrayList<>();


    public NewItemAdapter(Context context, ArrayList<ItemModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public NewItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        NewItemAdapter.ViewHolder holder = new NewItemAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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

        holder.edTitle.setText(title);
        holder.edPrice.setText(String.valueOf(startPrice)+" RM");
        if(bidderPriceList != null){
            holder.bidder.setText(bidderPriceList.size()+" Bidder");
        }else{
            holder.bidder.setText("0 Bidder");
        }
        //set the image
        Picasso.with(this.context).load(imageUri).fit().into(holder.imageView);

        //call item page
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo open product page
                System.out.println("you clicked the view !"+id);
                Intent intent = new Intent(view.getContext(), ItemPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("startPrice", startPrice);
                intent.putExtra("imageUri",imageUri);
                intent.putExtra("sellerEmail",sellerEmail);
                intent.putExtra("buyerEmail",buyerEmail);
                intent.putExtra("isActive",isActive);
                intent.putExtra("soldPrice",soldPrice);
                intent.putStringArrayListExtra("bidderPriceList",bidderPriceList);
                intent.putStringArrayListExtra("bidderEmailList",bidderEmailList);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView edTitle, edPrice, bidder;
        private CardView cardView;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.citemTitle);
            edPrice = itemView.findViewById(R.id.citemPrice);
            cardView = itemView.findViewById(R.id.itemCardView);
            bidder = itemView.findViewById(R.id.cBiddingNo);
            imageView = itemView.findViewById(R.id.citemImage);
        }
    }
}