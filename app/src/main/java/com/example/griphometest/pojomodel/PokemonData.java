package com.example.griphometest.pojomodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokemonData {

    private String count;

    private String next;

    @SerializedName("results")
    private List<UrlContainer> data;

    public String getCount() {
        return count;
    }

    public List<UrlContainer> getData() {
        return data;
    }

    public int getNextOffset() {
        // example: "https://pokeapi.co/api/v2/pokemon?offset=11&limit=10";
        String findKey = "offset=";
        int startIndex = next.indexOf(findKey);
        int endIndex = next.indexOf("&");
        if (endIndex < startIndex) {
            // in case offset is the last parameter
            endIndex = next.length();
        }
        String nextOffset = next.substring(startIndex + findKey.length(), endIndex);
        return Integer.parseInt(nextOffset);
    }
}
