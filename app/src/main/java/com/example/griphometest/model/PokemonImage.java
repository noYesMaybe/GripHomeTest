package com.example.griphometest.model;

import android.graphics.Bitmap;

public class PokemonImage {

    private Type type;
    private String url;
    private Bitmap bitmap;

    public PokemonImage(Type type, String url) {
        this.type = type;
        this.url = url;
    }

    public Type getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public enum Type {
        Back_default,
        Back_female,
        Back_shiny,
        Back_shiny_female,
        Front_default,
        Front_female,
        Front_shiny,
        Front_shiny_female,
    }


}
