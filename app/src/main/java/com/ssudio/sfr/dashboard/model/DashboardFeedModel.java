package com.ssudio.sfr.dashboard.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;

public class DashboardFeedModel {
    @SerializedName("id_feed")
    private String id;
    private String feed;
    @SerializedName("tgl_feed")
    private String feedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getFeedDate() {
        return feedDate;
    }

    public void setFeedDate(String feedDate) {
        this.feedDate = feedDate;
    }
}
