package com.example.griphometest.model;

import java.util.List;

public class Pokemon {

    private int id;
    private String name;
    private double weight; // kg
    private double height; // cm
    private String baseExperience;
    private List<Stat> stats;
    private List<String> types;

    private List<PokemonImage> pokemonImages;

    public Pokemon(int id, String name, double weight, double height, String baseExperience,
                   List<PokemonImage> pokemonImages, List<Stat> stats, List<String> types) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.baseExperience = baseExperience;
        this.pokemonImages = pokemonImages;
        this.stats = stats;
        this.types = types;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public List<Stat> getStats() {
        return stats;
    }

    public List<String> getTypes() {
        return types;
    }

    public String getBaseExperience() {
        return baseExperience;
    }

    public List<PokemonImage> getPokemonImages() {
        return pokemonImages;
    }

    public PokemonImage getImageByType(PokemonImage.Type type) {
        PokemonImage retValue = null;
        for (PokemonImage image : pokemonImages) {
            if (image.getType().equals(type)) {
                retValue = image;
                break;
            }
        }
        return retValue;
    }
}
