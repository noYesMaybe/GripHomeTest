package com.example.griphometest.pojomodel;

import com.google.gson.annotations.SerializedName;

public class AbilityData {

    private String slot;

    @SerializedName("is_hidden")
    private String isHidden;

    @SerializedName("ability")
    private UrlContainer data;

}
