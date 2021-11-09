package com.example.griphometest.helper;

import com.example.griphometest.model.Pokemon;
import com.example.griphometest.model.PokemonImage;
import com.example.griphometest.model.Stat;
import com.example.griphometest.pojomodel.Image;
import com.example.griphometest.pojomodel.PokemonPojo;
import com.example.griphometest.pojomodel.StatData;
import com.example.griphometest.pojomodel.TypeData;

import java.util.ArrayList;
import java.util.List;

public class PokemonParser implements IPokemonParser {

    private static PokemonParser instance;

    public static PokemonParser getInstance() {
        if (instance == null) {
            instance = new PokemonParser();
        }
        return instance;
    }

    private PokemonParser() {}

    @Override
    public Pokemon parsePokemon(PokemonPojo pokemonPojo) {
        if (pokemonPojo == null) {
            return null;
        }
        // convert
        double weighInHectograms = Double.parseDouble(pokemonPojo.getWeight());
        double heightInDecimetres = Double.parseDouble(pokemonPojo.getHeight());
        double weight = weighInHectograms / 10; //kg
        double height = heightInDecimetres * 10; //cm

        // stats
        List<Stat> stats = new ArrayList<>();
        for (StatData statData : pokemonPojo.getStats()) {
            Stat stat = new Stat(Integer.parseInt(statData.getBaseStat()), statData.getData().getName());
            stats.add(stat);
        }

        // types
        List<String> types = new ArrayList<>();
        for (TypeData typeData : pokemonPojo.getTypes()) {
            types.add(typeData.getData().getName());
        }

        // images
        List<PokemonImage> pokemonImages = getImages(pokemonPojo.getImage());

        // create new object
        return new Pokemon(pokemonPojo.getId(), pokemonPojo.getName(), weight, height,
                pokemonPojo.getBaseExperience(), pokemonImages, stats, types);
    }

    private List<PokemonImage> getImages(Image imageData) {
        List<PokemonImage> pokemonImages = new ArrayList<>();

        if (imageData.getFrontDefaultUrl() != null && !imageData.getFrontDefaultUrl().isEmpty()) {
            pokemonImages.add(new PokemonImage(PokemonImage.Type.Front_default,
                    imageData.getFrontDefaultUrl()));
        }
        if (imageData.getBackDefaultUrl() != null && !imageData.getBackDefaultUrl().isEmpty()) {
            pokemonImages.add(new PokemonImage(PokemonImage.Type.Back_default,
                    imageData.getBackDefaultUrl()));
        }
        if (imageData.getFrontFemaleUrl() != null && !imageData.getFrontFemaleUrl().isEmpty()) {
            pokemonImages.add(new PokemonImage(PokemonImage.Type.Front_female,
                    imageData.getFrontFemaleUrl()));
        }
        if (imageData.getBackFemaleUrl() != null && !imageData.getBackFemaleUrl().isEmpty()) {
            pokemonImages.add(new PokemonImage(PokemonImage.Type.Back_female,
                    imageData.getBackFemaleUrl()));
        }
        if (imageData.getFrontShinyUrl() != null && !imageData.getFrontShinyUrl().isEmpty()) {
            pokemonImages.add(new PokemonImage(PokemonImage.Type.Front_shiny,
                    imageData.getFrontShinyUrl()));
        }
        if (imageData.getBackShinyUrl() != null && !imageData.getBackShinyUrl().isEmpty()) {
            pokemonImages.add(new PokemonImage(PokemonImage.Type.Back_shiny,
                    imageData.getBackShinyUrl()));
        }
        if (imageData.getFrontShinyFemaleUrl() != null && !imageData.getFrontShinyFemaleUrl().isEmpty()) {
            pokemonImages.add(new PokemonImage(PokemonImage.Type.Front_shiny_female,
                    imageData.getFrontShinyFemaleUrl()));
        }
        if (imageData.getBackShinyFemaleUrl() != null && !imageData.getBackShinyFemaleUrl().isEmpty()) {
            pokemonImages.add(new PokemonImage(PokemonImage.Type.Back_shiny_female,
                    imageData.getBackShinyFemaleUrl()));
        }

        return pokemonImages;
    }
}
