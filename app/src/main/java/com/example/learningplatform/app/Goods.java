package com.example.learningplatform.app;

import android.os.Parcel;
import android.os.Parcelable;

public class Goods implements Parcelable {
    private int id;
    private String name;
    private Double price;
    private String description;
    private String faceUrl; // todo
    private int userId;

    public Goods(){

    }

    public Goods(int id, String name, Double price, String description, String faceUrl, int userId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.faceUrl = faceUrl;
        this.userId = userId;
    }

    protected Goods(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        description = in.readString();
        faceUrl = in.readString(); // todo
        userId = in.readInt();
    }

    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel in) {
            return new Goods(in);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String desc) {
        this.faceUrl = faceUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        dest.writeString(description);
        dest.writeString(faceUrl);
        dest.writeInt(userId);
    }
}
