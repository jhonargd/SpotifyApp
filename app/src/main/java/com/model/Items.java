package com.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {

    public String id;
    public Images[] images;
    public String name;


    protected Items(Parcel in) {
        id = in.readString();
        images = in.createTypedArray(Images.CREATOR);
        name = in.readString();
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeTypedArray(images, i);
        parcel.writeString(name);
    }
}
