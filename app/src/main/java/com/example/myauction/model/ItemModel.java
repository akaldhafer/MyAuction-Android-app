package com.example.myauction.model;

import java.util.ArrayList;

public class ItemModel {
    private String title, description, imageUri, sellerEmail, buyerEmail, isActive,token;
    ArrayList<String> bidderEmailList = new ArrayList<>();
    ArrayList<Integer> bidderPriceList = new ArrayList<>();
    ArrayList<String> bidderReviewList = new ArrayList<>();

    public ItemModel(String title, String description, String imageUri, String sellerEmail,
                     String buyerEmail, String isActive, String token, ArrayList<String> bidderEmailList,
                     ArrayList<Integer> bidderPriceList, ArrayList<String> bidderReviewList) {
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
        this.sellerEmail = sellerEmail;
        this.buyerEmail = buyerEmail;
        this.isActive = isActive;
        this.token = token;
        this.bidderEmailList = bidderEmailList;
        this.bidderPriceList = bidderPriceList;
        this.bidderReviewList = bidderReviewList;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<String> getBidderEmailList() {
        return bidderEmailList;
    }

    public void setBidderEmailList(ArrayList<String> bidderEmailList) {
        this.bidderEmailList = bidderEmailList;
    }

    public ArrayList<Integer> getBidderPriceList() {
        return bidderPriceList;
    }

    public void setBidderPriceList(ArrayList<Integer> bidderPriceList) {
        this.bidderPriceList = bidderPriceList;
    }

    public ArrayList<String> getBidderReviewList() {
        return bidderReviewList;
    }

    public void setBidderReviewList(ArrayList<String> bidderReviewList) {
        this.bidderReviewList = bidderReviewList;
    }
}
