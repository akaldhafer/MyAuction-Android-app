package com.example.myauction.model;

import java.util.ArrayList;

public class ItemModel {
    private String id, title, description, imageUri, sellerEmail, buyerEmail, isActive;
    private int startPrice, soldPrice;
    private ArrayList<String> bidderEmailList;
    private ArrayList<String> bidderPriceList;

    public ItemModel(String id, String title, String description, String imageUri,
                     String sellerEmail, String buyerEmail, String isActive, int startPrice,
                     int soldPrice, ArrayList<String> bidderEmailList, ArrayList<String> bidderPriceList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
        this.sellerEmail = sellerEmail;
        this.buyerEmail = buyerEmail;
        this.isActive = isActive;
        this.startPrice = startPrice;
        this.soldPrice = soldPrice;
        this.bidderEmailList = bidderEmailList;
        this.bidderPriceList = bidderPriceList;
    }

    public ArrayList<String> getBidderEmailList() {
        return bidderEmailList;
    }

    public void setBidderEmailList(ArrayList<String> bidderEmailList) {
        this.bidderEmailList = bidderEmailList;
    }

    public ArrayList<String> getBidderPriceList() {
        return bidderPriceList;
    }

    public void setBidderPriceList(ArrayList<String> bidderPriceList) {
        this.bidderPriceList = bidderPriceList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }

    public int getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(int soldPrice) {
        this.soldPrice = soldPrice;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }





}
