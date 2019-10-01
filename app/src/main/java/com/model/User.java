package com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Parcelable {

    public String href;
    public String country;
    public String display_name;
    public String email;
    public String id;
    public Images[] images;

    protected User(Parcel in) {
        href = in.readString();
        country = in.readString();
        display_name = in.readString();
        email = in.readString();
        id = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(href);
        parcel.writeString(country);
        parcel.writeString(display_name);
        parcel.writeString(email);
        parcel.writeString(id);
        parcel.writeArray(images);
    }
}
