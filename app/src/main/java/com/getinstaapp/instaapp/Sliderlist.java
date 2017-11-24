package com.getinstaapp.instaapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 11/20/2017.
 */

public class Sliderlist {
    String fid;
    String bpid;
    String banner_images;

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
}
