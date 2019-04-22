package com.example.learningplatform.app;

public class userInfo {
    private String userName;
    private String password;
    private String goodsName;
    private String goodsPrice;
    private String telephone;
    private String description;

    public userInfo(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


    public void setGoodsName(String goodsName){
        this.goodsName = goodsName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsPrice(String goodsPrice){
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setTelephone(String telephone){
        this.telephone = telephone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}


