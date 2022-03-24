package com.example.myauction.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myauction.R;
import com.example.myauction.model.ItemModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PurchasesItemAdapter extends RecyclerView.Adapter<PurchasesItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemModel> arrayList = new ArrayList<>();

    public PurchasesItemAdapter(Context context, ArrayList<ItemModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PurchasesItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_my_purchases, parent, false);
        PurchasesItemAdapter.ViewHolder holder = new PurchasesItemAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasesItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        holder.vStartPrice.setText(String.valueOf(startPrice));
        holder.vSoldPrice.setText(String.valueOf(soldPrice));
        holder.vSellerEmail.setText(String.valueOf(sellerEmail));
        //set the image
        Picasso.with(this.context).load(imageUri).fit().into(holder.imageView);

        //send email
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] TO ={sellerEmail};
                String[] CC = {buyerEmail};
                String subject = "Buying " + title;
                String body = "I would like to contact you about the following item to finish the payment and delivery process\n\n"
                        + title+"\n"+description+"\n"+soldPrice+"\nFrom the buyer who won the auction on your item\n"+buyerEmail
                        +"\n\nThanks for using MyAuction Platform";
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                try {
                    view.getContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(view.getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView vTitle, vDesc, vStartPrice, vSoldPrice, vSellerEmail;
        private ImageView imageView, send;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vTitle = itemView.findViewById(R.id.prTitle);
            vDesc = itemView.findViewById(R.id.prDesc);
            vStartPrice = itemView.findViewById(R.id.prStartPrice);
            vSoldPrice = itemView.findViewById(R.id.prSoldPrice);
            vSellerEmail = itemView.findViewById(R.id.prSellerEmail);
            send = itemView.findViewById(R.id.prSendEmail);
            imageView = itemView.findViewById(R.id.prImage);
        }
    }
}