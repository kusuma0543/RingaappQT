package com.ringaapp.ringauser;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 11/6/2017.
 */

public class subcatelist implements Parcelable{
    String rid;
    String 	service_categ_uid;
    String service_subcateg_uid;
    String service_subcateg_name;
    String service_subcateg_fullimage;

    protected subcatelist(Parcel in) {
        rid = in.readString();
        service_categ_uid = in.readString();
        service_subcateg_uid = in.readString();
        service_subcateg_name = in.readString();
        service_subcateg_fullimage = in.readString();
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

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getService_categ_uid() {
        return service_categ_uid;
    }

    public void setService_categ_uid(String service_categ_uid) {
        this.service_categ_uid = service_categ_uid;
    }

    public String getService_subcateg_uid() {
        return service_subcateg_uid;
    }

    public void setService_subcateg_uid(String service_subcateg_uid) {
        this.service_subcateg_uid = service_subcateg_uid;
    }

    public String getService_subcateg_name() {
        return service_subcateg_name;
    }

    public void setService_subcateg_name(String service_subcateg_name) {
        this.service_subcateg_name = service_subcateg_name;
    }

    public String getService_subcateg_fullimage() {
        return service_subcateg_fullimage;
    }

    public void setService_subcateg_fullimage(String service_subcateg_fullimage) {
        this.service_subcateg_fullimage = service_subcateg_fullimage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rid);
        dest.writeString(service_categ_uid);
        dest.writeString(service_subcateg_uid);
        dest.writeString(service_subcateg_name);
        dest.writeString(service_subcateg_fullimage);
    }
}