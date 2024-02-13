package com.example.coolhouse;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Product implements Parcelable {

    public String name;
    public double price;
    public boolean isSelected;

    public Product(){
    }
    protected Product(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        isSelected = in.readByte() != 0;
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
    }
}