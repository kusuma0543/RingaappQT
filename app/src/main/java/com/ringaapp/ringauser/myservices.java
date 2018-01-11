package com.ringaapp.ringauser;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 12/31/2017.
 */

public class myservices implements Parcelable{
    String booking_uid;
    String partner_uid;
    String partner_name;
    String partner_cityname;
    String partner_locality;
    String service_subcateg_name;
    String service_booking_status;
    String user_uid;
    String service_subcateg_uid;

    protected myservices(Parcel in) {
        booking_uid = in.readString();
        partner_uid = in.readString();
        partner_name = in.readString();
        partner_cityname = in.readString();
        partner_locality = in.readString();
        service_subcateg_name = in.readString();
        service_booking_status = in.readString();
        user_uid = in.readString();
        service_subcateg_uid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(booking_uid);
        dest.writeString(partner_uid);
        dest.writeString(partner_name);
        dest.writeString(partner_cityname);
        dest.writeString(partner_locality);
        dest.writeString(service_subcateg_name);
        dest.writeString(service_booking_status);
        dest.writeString(user_uid);
        dest.writeString(service_subcateg_uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<myservices> CREATOR = new Creator<myservices>() {
        @Override
        public myservices createFromParcel(Parcel in) {
            return new myservices(in);
        }

        @Override
        public myservices[] newArray(int size) {
            return new myservices[size];
        }
    };

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

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
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

    public String getService_subcateg_name() {
        return service_subcateg_name;
    }

    public void setService_subcateg_name(String service_subcateg_name) {
        this.service_subcateg_name = service_subcateg_name;
    }

    public String getService_booking_status() {
        return service_booking_status;
    }

    public void setService_booking_status(String service_booking_status) {
        this.service_booking_status = service_booking_status;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getService_subcateg_uid() {
        return service_subcateg_uid;
    }

    public void setService_subcateg_uid(String service_subcateg_uid) {
        this.service_subcateg_uid = service_subcateg_uid;
    }
}
