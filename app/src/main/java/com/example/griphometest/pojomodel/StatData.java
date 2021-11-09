package com.example.griphometest.pojomodel;

import com.google.gson.annotations.SerializedName;

public class StatData {

    @SerializedName("base_stat")
    private String baseStat;

    @SerializedName("stat")
    private UrlContainer data;

    public String getBaseStat() {
        return baseStat;
    }

    public UrlContainer getData() {
        return data;
    }
}
