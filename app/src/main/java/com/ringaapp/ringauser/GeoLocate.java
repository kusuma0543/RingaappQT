package com.ringaapp.ringauser;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 12/21/2017.
 */

public class GeoLocate implements Parcelable{
String partner_uid;
String partner_name;
String partner_address;
String partner_cityname;
String partner_locality;
String partner_latitude;
String partner_longitude;
String partner_budget;
String partner_suscription_type;
String user_feedback;
String user_ratings;

    protected GeoLocate(Parcel in) {
        partner_uid = in.readString();
        partner_name = in.readString();
        partner_address = in.readString();
        partner_cityname = in.readString();
        partner_locality = in.readString();
        partner_latitude = in.readString();
        partner_longitude = in.readString();
        partner_budget = in.readString();
        partner_suscription_type = in.readString();
        user_feedback = in.readString();
        user_ratings = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(partner_uid);
        dest.writeString(partner_name);
        dest.writeString(partner_address);
        dest.writeString(partner_cityname);
        dest.writeString(partner_locality);
        dest.writeString(partner_latitude);
        dest.writeString(partner_longitude);
        dest.writeString(partner_budget);
        dest.writeString(partner_suscription_type);
        dest.writeString(user_feedback);
        dest.writeString(user_ratings);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GeoLocate> CREATOR = new Creator<GeoLocate>() {
        @Override
        public GeoLocate createFromParcel(Parcel in) {
            return new GeoLocate(in);
        }

        @Override
        public GeoLocate[] newArray(int size) {
            return new GeoLocate[size];
        }
    };

    public String getPartner_uid() {
        return partner_uid;
    }

    public void setPartner_uid(String partner_uid) {
        this.partner_uid = partner_uid;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getPartner_address() {
        return partner_address;
    }

    public void setPartner_address(String partner_address) {
        this.partner_address = partner_address;
    }

    public String getPartner_cityname() {
        return partner_cityname;
    }

    public void setPartner_cityname(String partner_cityname) {
        this.partner_cityname = partner_cityname;
    }

    public String getPartner_locality() {
        return partner_locality;
    }

    public void setPartner_locality(String partner_locality) {
        this.partner_locality = partner_locality;
    }

    public String getPartner_latitude() {
        return partner_latitude;
    }

    public void setPartner_latitude(String partner_latitude) {
        this.partner_latitude = partner_latitude;
    }

    public String getPartner_longitude() {
        return partner_longitude;
    }

    public void setPartner_longitude(String partner_longitude) {
        this.partner_longitude = partner_longitude;
    }

    public String getPartner_budget() {
        return partner_budget;
    }

    public void setPartner_budget(String partner_budget) {
        this.partner_budget = partner_budget;
    }

    public String getPartner_suscription_type() {
        return partner_suscription_type;
    }

    public void setPartner_suscription_type(String partner_suscription_type) {
        this.partner_suscription_type = partner_suscription_type;
    }

    public String getUser_feedback() {
        return user_feedback;
    }

    public void setUser_feedback(String user_feedback) {
        this.user_feedback = user_feedback;
    }

    public String getUser_ratings() {
        return user_ratings;
    }

    public void setUser_ratings(String user_ratings) {
        this.user_ratings = user_ratings;
    }
}

