package com.example.griphometest.helper;

import com.example.griphometest.model.Pokemon;
import com.example.griphometest.pojomodel.PokemonPojo;

public interface IPokemonParser {

    // Parse object from service to create object type needed for the application
    Pokemon parsePokemon(PokemonPojo pokemonPojo);
}
