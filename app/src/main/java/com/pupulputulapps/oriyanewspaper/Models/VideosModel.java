package com.pupulputulapps.oriyanewspaper.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class VideosModel implements Parcelable {

    private int id;
    private String title;
    private String description;
    private String pub_date;
    private String yt_video_id;
    private String timestamp;
    private String source_channel_name;

    public VideosModel(int id, String title, String description, String pub_date, String yt_video_id, String timestamp, String source_channel_name) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pub_date = pub_date;
        this.yt_video_id = yt_video_id;
        this.timestamp = timestamp;
        this.source_channel_name = source_channel_name;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPub_date() {
        return pub_date;
    }

    public String getYt_video_id() {
        return yt_video_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSource_channel_name() {
        return source_channel_name;
    }

    protected VideosModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        pub_date = in.readString();
        yt_video_id = in.readString();
        timestamp = in.readString();
        source_channel_name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(pub_date);
        dest.writeString(yt_video_id);
        dest.writeString(timestamp);
        dest.writeString(source_channel_name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VideosModel> CREATOR = new Parcelable.Creator<VideosModel>() {
        @Override
        public VideosModel createFromParcel(Parcel in) {
            return new VideosModel(in);
        }

        @Override
        public VideosModel[] newArray(int size) {
            return new VideosModel[size];
        }
    };
}