package com.example.myauction.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myauction.R;
import com.example.myauction.auth.RegisterActivity;
import com.example.myauction.auth.VerifyEmail;
import com.example.myauction.itemapi.UploadItemData;
import com.example.myauction.itemapi.ViewItemMessage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

public class SellItemActivity extends AppCompatActivity implements ViewItemMessage {
    EditText edTitle, edDesc, edPrice;
    UploadItemData uploadItemData;

    private String sImageUri;
    private ImageView imageView;
    public Uri imageUri;
    private StorageReference storageReference;
    private StorageTask uploadtask;
    private static final String TAG = "additem";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide(); //This Line hides the action bar
        setContentView(R.layout.activity_sell_item);

        edTitle = findViewById(R.id.edTitle);
        edDesc =findViewById(R.id.edDesc);
        edPrice =  findViewById(R.id.edPrice);

        uploadItemData = new UploadItemData(this);
        imageView = (ImageView) findViewById(R.id.pickImage);
        storageReference = FirebaseStorage.getInstance().getReference("ItemImages");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading..");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SellItemActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SellItemActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {
                    selectImage();
                }
            }
        });
    }

    private void uploadFile(){

        if(imageUri != null){
            Log.d(TAG, "uploadfile: getLastPathSegment type " + imageUri.getLastPathSegment());
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading the image...");
            pd.show();
            StorageReference fileReference = storageReference.child(imageUri.getLastPathSegment());
            uploadtask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pd.dismiss();
                                    sImageUri = uri.toString();
                                    Log.d(TAG, "uploadFile: url will be upload " + sImageUri);
                                    Toast.makeText(SellItemActivity.this, "Image Upload successful", Toast.LENGTH_SHORT).show();
                                    Validate(sImageUri);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SellItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new com.google.firebase.storage.OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            pd.setMessage("Progress: "+ (int) progress + "%");
                        }
                    });


        }else{
            Toast.makeText(this, "No Image selected", Toast.LENGTH_LONG).show();
        }
    }
    private void Validate(String imageuri) {
        String isActive = "active";
        String title = edTitle.getText().toString().trim();
        String description = edDesc.getText().toString().trim();
        int startPrice =Integer.parseInt(edPrice.getText().toString().trim());
        int soldPrice = 0;
        String sellerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String buyerEmail = "";
        ArrayList<String> bidderEmailList= new ArrayList<>();
        ArrayList<String> bidderPriceList= new ArrayList<>();
        bidderEmailList.add(sellerEmail);
        bidderPriceList.add(edPrice.getText().toString());
        Log.d(TAG, "checkdetails: url before upload " + imageuri);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String id = timestamp.toString().trim();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(imageuri)){

            uploadItemData.onSuccessUpdate(this,id,  title,  description,  imageuri,
                    sellerEmail,  buyerEmail,  isActive,  startPrice,
                    soldPrice,  bidderEmailList, bidderPriceList);

        }else{
            if(TextUtils.isEmpty(title)){
                edTitle.setError("Title is required");
                return;
            }if (TextUtils.isEmpty(description)){
                edDesc.setError("Description is required");
                return;
            }
            if (edPrice.getText() == null){
                edPrice.setError("Price is required");
                return;
            }
            if (TextUtils.isEmpty(imageuri)){
                Toast.makeText(SellItemActivity.this, "Image is Required", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){

            imageUri = data.getData();
            Picasso.with(SellItemActivity.this).load(imageUri).fit().into(imageView);
        }
    }
    public void onSellClick(View view) {
        if(uploadtask != null && uploadtask.isInProgress()){
            Toast.makeText(SellItemActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
        }
        else{
            uploadFile();
        }
    }

    @Override
    public void onUpdateSuccess(String message) {
        Toast.makeText(SellItemActivity.this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SellItemActivity.this, UserMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(SellItemActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SellItemActivity.this, UserMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onViewItemClick(View view) {
        Intent intent = new Intent(SellItemActivity.this, ViewMyItems.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}