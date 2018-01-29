package com.ringaapp.ringauser;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andriod on 29/1/18.
 */

public class showfav implements Parcelable{

    String fid;
    String user_uid;
    String booking_uid;
    String partner_uid;
    String service_booking_createddate;
    String service_subcateg_uid;
    String partner_name;
    String partner_mobilenumber;
    String service_subcateg_name;
    String service_categ_name;
    String service_booking_status;
    String service_categ_uid;
    String user_ratings;
    String user_rating_count;
    String partner_budget;

    protected showfav(Parcel in) {
        fid = in.readString();
        user_uid = in.readString();
        booking_uid = in.readString();
        partner_uid = in.readString();
        service_booking_createddate = in.readString();
        service_subcateg_uid = in.readString();
        partner_name = in.readString();
        partner_mobilenumber = in.readString();
        service_subcateg_name = in.readString();
        service_categ_name = in.readString();
        service_booking_status = in.readString();
        service_categ_uid = in.readString();
        user_ratings = in.readString();
        user_rating_count = in.readString();
        partner_budget = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fid);
        dest.writeString(user_uid);
        dest.writeString(booking_uid);
        dest.writeString(partner_uid);
        dest.writeString(service_booking_createddate);
        dest.writeString(service_subcateg_uid);
        dest.writeString(partner_name);
        dest.writeString(partner_mobilenumber);
        dest.writeString(service_subcateg_name);
        dest.writeString(service_categ_name);
        dest.writeString(service_booking_status);
        dest.writeString(service_categ_uid);
        dest.writeString(user_ratings);
        dest.writeString(user_rating_count);
        dest.writeString(partner_budget);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<showfav> CREATOR = new Creator<showfav>() {
        @Override
        public showfav createFromParcel(Parcel in) {
            return new showfav(in);
        }

        @Override
        public showfav[] newArray(int size) {
            return new showfav[size];
        }
    };

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getBooking_uid() {
        return booking_uid;
    }

    public void setBooking_uid(String booking_uid) {
        this.booking_uid = booking_uid;
    }

    public String getPartner_uid() {
        return partner_uid;
    }

    public void setPartner_uid(String partner_uid) {
        this.partner_uid = partner_uid;
    }

    public String getService_booking_createddate() {
        return service_booking_createddate;
    }

    public void setService_booking_createddate(String service_booking_createddate) {
        this.service_booking_createddate = service_booking_createddate;
    }

    public String getService_subcateg_uid() {
        return service_subcateg_uid;
    }

    public void setService_subcateg_uid(String service_subcateg_uid) {
        this.service_subcateg_uid = service_subcateg_uid;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getPartner_mobilenumber() {
        return partner_mobilenumber;
    }

    public void setPartner_mobilenumber(String partner_mobilenumber) {
        this.partner_mobilenumber = partner_mobilenumber;
    }

    public String getService_subcateg_name() {
        return service_subcateg_name;
    }

    public void setService_subcateg_name(String service_subcateg_name) {
        this.service_subcateg_name = service_subcateg_name;
    }

    public String getService_categ_name() {
        return service_categ_name;
    }

    public void setService_categ_name(String service_categ_name) {
        this.service_categ_name = service_categ_name;
    }

    public String getService_booking_status() {
        return service_booking_status;
    }

    public void setService_booking_status(String service_booking_status) {
        this.service_booking_status = service_booking_status;
    }

    public String getService_categ_uid() {
        return service_categ_uid;
    }

    public void setService_categ_uid(String service_categ_uid) {
        this.service_categ_uid = service_categ_uid;
    }

    public String getUser_ratings() {
        return user_ratings;
    }

    public void setUser_ratings(String user_ratings) {
        this.user_ratings = user_ratings;
    }

    public String getUser_rating_count() {
        return user_rating_count;
    }

    public void setUser_rating_count(String user_rating_count) {
        this.user_rating_count = user_rating_count;
    }

    public String getPartner_budget() {
        return partner_budget;
    }

    public void setPartner_budget(String partner_budget) {
        this.partner_budget = partner_budget;
    }
}
