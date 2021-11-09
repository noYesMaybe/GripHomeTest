package com.example.griphometest.pojomodel;

import com.google.gson.annotations.SerializedName;

//sprites
public class Image {

    @SerializedName("back_default")
    private String backDefaultUrl;

    @SerializedName("back_female")
    private String backFemaleUrl;

    @SerializedName("back_shiny")
    private String backShinyUrl;

    @SerializedName("back_shiny_female")
    private String backShinyFemaleUrl;

    @SerializedName("front_default")
    private String frontDefaultUrl;

    @SerializedName("front_female")
    private String frontFemaleUrl;

    @SerializedName("front_shiny")
    private String frontShinyUrl;

    @SerializedName("front_shiny_female")
    private String frontShinyFemaleUrl;

    public String getBackDefaultUrl() {
        return backDefaultUrl;
    }

    public String getBackFemaleUrl() {
        return backFemaleUrl;
    }

    public String getBackShinyUrl() {
        return backShinyUrl;
    }

    public String getBackShinyFemaleUrl() {
        return backShinyFemaleUrl;
    }

    public String getFrontDefaultUrl() {
        return frontDefaultUrl;
    }

    public String getFrontFemaleUrl() {
        return frontFemaleUrl;
    }

    public String getFrontShinyUrl() {
        return frontShinyUrl;
    }

    public String getFrontShinyFemaleUrl() {
        return frontShinyFemaleUrl;
    }

}
