package com.getinstaapp.instaapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 11/20/2017.
 */

public class Sliderlist implements Parcelable{
    String fid;
    String bpid;
    String banner_images;

    protected Sliderlist(Parcel in) {
        fid = in.readString();
        bpid = in.readString();
        banner_images = in.readString();
    }

    public static final Creator<Sliderlist> CREATOR = new Creator<Sliderlist>() {
        @Override
        public Sliderlist createFromParcel(Parcel in) {
            return new Sliderlist(in);
        }

        @Override
        public Sliderlist[] newArray(int size) {
            return new Sliderlist[size];
        }
    };

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getBpid() {
        return bpid;
    }

    public void setBpid(String bpid) {
        this.bpid = bpid;
    }

    public String getBanner_images() {
        return banner_images;
    }

    public void setBanner_images(String banner_images) {
        this.banner_images = banner_images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fid);
        dest.writeString(bpid);
        dest.writeString(banner_images);
    }
}
