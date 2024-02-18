package com.example.coolhouse;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Product implements Parcelable {

    public String name;
    public double price;
    public boolean isSelected;
    public String imgUrl;

    public Product(){
    }
    protected Product(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        isSelected = in.readByte() != 0;
        imgUrl = in.readString();
    }

    public Product(String name, double price, boolean isSelected,String imgUrl) {
        this.name = name;
        this.price = price;
        this.isSelected = isSelected;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
        parcel.writeString(imgUrl);
    }
}