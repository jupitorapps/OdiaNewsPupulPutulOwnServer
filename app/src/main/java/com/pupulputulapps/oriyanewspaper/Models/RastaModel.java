package com.pupulputulapps.oriyanewspaper.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class RastaModel implements Parcelable {

    private String y_key;
    private int is_active;

    public RastaModel(String y_key, int is_active) {
        this.y_key = y_key;
        this.is_active = is_active;
    }

    public String getY_key() {
        return y_key;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    protected RastaModel(Parcel in) {
        y_key = in.readString();
        is_active = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(y_key);
        dest.writeInt(is_active);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RastaModel> CREATOR = new Parcelable.Creator<RastaModel>() {
        @Override
        public RastaModel createFromParcel(Parcel in) {
            return new RastaModel(in);
        }

        @Override
        public RastaModel[] newArray(int size) {
            return new RastaModel[size];
        }
    };
}