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
        String title, description, location,imageUrl;
        int price;

        title = arrayList.get(holder.getAdapterPosition()).getTitle();
        description =arrayList.get(holder.getAdapterPosition()).getDescription();
        imageUrl = arrayList.get(position).getImageUri();
        price = arrayList.get(holder.getAdapterPosition()).getStartPrice();

        holder.edTitle.setText(title);
        holder.edDesc.setText(description);
        holder.edPrice.setText(String.valueOf(price));
        //set the image
        Picasso.with(this.context).load(imageUrl).fit().into(holder.imageView);
        String id= arrayList.get(holder.getAdapterPosition()).getId();
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

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private EditText edTitle, edDesc, edPrice;
        private Button delete, update;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.vcardTitle);
            edDesc = itemView.findViewById(R.id.vcardDescription);
            edPrice = itemView.findViewById(R.id.vcardPrice);
            update = itemView.findViewById(R.id.vcardUpdate);
            delete = itemView.findViewById(R.id.vcardDelete);
            imageView = itemView.findViewById(R.id.vcardImage);
        }
    }
}