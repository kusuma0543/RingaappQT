package com.ringaapp.ringauser;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 12/8/2017.
 */

public class Bannerlist implements Parcelable{
    String rid;
    String promotion_title;
    String promotion_fullimage;
    String promotion_thumbnail;
String promotion_res;


    protected Bannerlist(Parcel in) {
        rid = in.readString();
        promotion_title = in.readString();
        promotion_fullimage = in.readString();
        promotion_thumbnail = in.readString();
        promotion_res = in.readString();
    }

    public static final Creator<Bannerlist> CREATOR = new Creator<Bannerlist>() {
        @Override
        public Bannerlist createFromParcel(Parcel in) {
            return new Bannerlist(in);
        }

        @Override
        public Bannerlist[] newArray(int size) {
            return new Bannerlist[size];
        }
    };

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getPromotion_title() {
        return promotion_title;
    }

    public void setPromotion_title(String promotion_title) {
        this.promotion_title = promotion_title;
    }

    public String getPromotion_fullimage() {
        return promotion_fullimage;
    }

    public void setPromotion_fullimage(String promotion_fullimage) {
        this.promotion_fullimage = promotion_fullimage;
    }

    public String getPromotion_thumbnail() {
        return promotion_thumbnail;
    }

    public void setPromotion_thumbnail(String promotion_thumbnail) {
        this.promotion_thumbnail = promotion_thumbnail;
    }

    public String getPromotion_res() {
        return promotion_res;
    }

    public void setPromotion_res(String promotion_res) {
        this.promotion_res = promotion_res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(rid);
        parcel.writeString(promotion_title);
        parcel.writeString(promotion_fullimage);
        parcel.writeString(promotion_thumbnail);
        parcel.writeString(promotion_res);
    }
}
