package com.getinstaapp.instaapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 11/6/2017.
 */

public class subcatelist implements Parcelable{
    String fid;
    String subid;
    String subcategory_name;
    String subcategory_thumbnail;
    String cid_categ_sub;

    protected subcatelist(Parcel in) {
        fid = in.readString();
        subid = in.readString();
        subcategory_name = in.readString();
        subcategory_thumbnail = in.readString();
        cid_categ_sub = in.readString();
    }

    public static final Creator<subcatelist> CREATOR = new Creator<subcatelist>() {
        @Override
        public subcatelist createFromParcel(Parcel in) {
            return new subcatelist(in);
        }

        @Override
        public subcatelist[] newArray(int size) {
            return new subcatelist[size];
        }
    };

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public String getSubcategory_thumbnail() {
        return subcategory_thumbnail;
    }

    public void setSubcategory_thumbnail(String subcategory_thumbnail) {
        this.subcategory_thumbnail = subcategory_thumbnail;
    }

    public String getCid_categ_sub() {
        return cid_categ_sub;
    }

    public void setCid_categ_sub(String cid_categ_sub) {
        this.cid_categ_sub = cid_categ_sub;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fid);
        dest.writeString(subid);
        dest.writeString(subcategory_name);
        dest.writeString(subcategory_thumbnail);
        dest.writeString(cid_categ_sub);
    }
}
