package com.example.griphometest.helper;

import com.example.griphometest.model.Pokemon;
import com.example.griphometest.model.PokemonImage;

import java.io.IOException;

public interface IImageLoader {

    // Load bitmaps from urls, if type == null, all images will be loaded
    void loadBitmaps(Pokemon pokemon, PokemonImage.Type imageType) throws IOException;
}
