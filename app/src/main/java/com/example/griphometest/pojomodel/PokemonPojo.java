package com.example.griphometest.pojomodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokemonPojo {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("height")
    private String height; //decimetres

    @SerializedName("weight")
    private String weight; //hectograms

    @SerializedName("base_experience")
    private String baseExperience;

    @SerializedName("abilities")
    private List<AbilityData> abilities;

    @SerializedName("sprites")
    private Image images;

    private List<StatData> stats;

    @SerializedName("types")
    private List<TypeData> types;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getBaseExperience() {
        return baseExperience;
    }

    public List<AbilityData> getAbilities() {
        return abilities;
    }

    public Image getImage() {
        return images;
    }

    public List<StatData> getStats() {
        return stats;
    }

    public List<TypeData> getTypes() {
        return types;
    }
}
