package com.getinstaapp.instaapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 10/27/2017.
 */

public class Categorieslist implements Parcelable{
    String fid;
    String cid;
    String category_name;
    String category_thumbnail_image;

    protected Categorieslist(Parcel in) {
        fid = in.readString();
        cid = in.readString();
        category_name = in.readString();
        category_thumbnail_image = in.readString();
    }

    public static final Creator<Categorieslist> CREATOR = new Creator<Categorieslist>() {
        @Override
        public Categorieslist createFromParcel(Parcel in) {
            return new Categorieslist(in);
        }

        @Override
        public Categorieslist[] newArray(int size) {
            return new Categorieslist[size];
        }
    };

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_thumbnail_image() {
        return category_thumbnail_image;
    }

    public void setCategory_thumbnail_image(String category_thumbnail_image) {
        this.category_thumbnail_image = category_thumbnail_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fid);
        dest.writeString(cid);
        dest.writeString(category_name);
        dest.writeString(category_thumbnail_image);
    }
}
