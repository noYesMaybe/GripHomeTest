package com.example.griphometest.service;

import com.example.griphometest.pojomodel.Ability;
import com.example.griphometest.pojomodel.PokemonPojo;
import com.example.griphometest.pojomodel.PokemonData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokemonServiceApi {

    String BASE_URL = "https://pokeapi.co/api/v2/";

    // limit=10&offset=1
    @GET("pokemon?")
    Call<PokemonData> getPokemons(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{id}")
    Call<PokemonPojo> getPokemonById(@Path("id") String id);

    @GET("{path}")
    Call<PokemonPojo> getPokemon(@Path("path") String pokemonPath);

    @GET("ability/{id}")
    Call<Ability> getAbilityId(@Path("id") String statId);

}
