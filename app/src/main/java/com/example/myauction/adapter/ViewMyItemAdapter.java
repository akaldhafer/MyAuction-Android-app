package com.example.myauction.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myauction.R;
import com.example.myauction.activity.ViewMyItems;
import com.example.myauction.model.ItemModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewMyItemAdapter extends RecyclerView.Adapter<ViewMyItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemModel> arrayList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;

    public ViewMyItemAdapter(Context context, ArrayList<ItemModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewMyItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_my_items, parent, false);
        ViewMyItemAdapter.ViewHolder holder = new ViewMyItemAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMyItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        String getBuyerEmail = bidderEmailList.get(bidderEmailList.size()-1);
        int getSoldPrice = Integer.parseInt(bidderPriceList.get(bidderPriceList.size()-1));

        holder.edTitle.setText(title);
        holder.edDesc.setText(description);
        holder.edPrice.setText(String.valueOf(startPrice));
        //set the image
        Picasso.with(this.context).load(imageUri).fit().into(holder.imageView);
        //delete
        holder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("ItemData").document(id);
                record.delete().addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(v.getContext(), "Item Deleted", Toast.LENGTH_LONG).show();
                        //delete from the ui
                        arrayList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), arrayList.size());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //update
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = holder.edTitle.getText().toString();
                String description =holder.edDesc.getText().toString();
                int price = Integer.parseInt(holder.edPrice.getText().toString());

                if(!TextUtils.isEmpty(title)
                        && !TextUtils.isEmpty(description)){

                    firebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference record = firebaseFirestore.collection("ItemData").document(id);
                    record.update("title",title, "description",description, "startPrice", price).addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(view.getContext(), "Item Updated", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    if(TextUtils.isEmpty(title)){
                        holder.edTitle.setError("Title is required");
                        return;
                    }if (TextUtils.isEmpty(description)){
                        holder.edDesc.setError("Description is required");
                        return;
                    }
                    if (holder.edPrice.getText() == null){
                        holder.edPrice.setError("Price is required");
                    }
                }

            }
        });

        //stop the auction of this item
        holder.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("ItemData").document(id);
                record.update("isActive","sold", "buyerEmail",getBuyerEmail, "soldPrice", getSoldPrice).addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(view.getContext(), "Auction Stopped and item sold", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private EditText edTitle, edDesc, edPrice;
        private Button delete, update, stop;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.vcardTitle);
            edDesc = itemView.findViewById(R.id.vcardDescription);
            edPrice = itemView.findViewById(R.id.vcardPrice);
            update = itemView.findViewById(R.id.vcardUpdate);
            delete = itemView.findViewById(R.id.vcardDelete);
            stop = itemView.findViewById(R.id.vcardStop);
            imageView = itemView.findViewById(R.id.vcardImage);
        }
    }
}