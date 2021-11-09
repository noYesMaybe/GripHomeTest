package com.example.griphometest.pojomodel;

import com.google.gson.annotations.SerializedName;

public class TypeData {

    private String slot;

    @SerializedName("type")
    private UrlContainer data;

    public String getSlot() {
        return slot;
    }

    public UrlContainer getData() {
        return data;
    }
}
