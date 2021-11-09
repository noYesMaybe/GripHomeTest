package com.example.griphometest.helper;

import static android.graphics.Bitmap.Config.ARGB_8888;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.griphometest.model.Pokemon;
import com.example.griphometest.model.PokemonImage;

import java.io.IOException;
import java.net.URL;

public class ImageLoader implements IImageLoader {

    private static ImageLoader instance;

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    private ImageLoader() {}

    @Override
    public void loadBitmaps(Pokemon pokemon, PokemonImage.Type imageType) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = ARGB_8888;

        for (PokemonImage pokemonImage: pokemon.getPokemonImages()) {

            // load specified image type
            if (pokemonImage.getType().equals(imageType)) {
                URL newUrl = new URL(pokemonImage.getUrl());
                Bitmap iconBitmap = BitmapFactory.decodeStream(
                        newUrl.openConnection().getInputStream(), null, options);
                pokemonImage.setBitmap(iconBitmap);
                break;
            }

            // load bitmap where is not already loaded
            if (pokemonImage.getBitmap() == null) {
                URL newUrl = new URL(pokemonImage.getUrl());
                Bitmap iconBitmap = BitmapFactory.decodeStream(newUrl.openConnection().getInputStream(), null, options);
                pokemonImage.setBitmap(iconBitmap);
            }
        }
    }
}
