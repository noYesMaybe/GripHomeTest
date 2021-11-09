package com.example.griphometest.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.griphometest.helper.Utils;
import com.example.griphometest.model.PokemonImage;
import com.example.griphometest.repository.PokemonRepository;
import com.example.griphometest.repository.Result;
import com.example.griphometest.model.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class PokemonViewModel extends ViewModel {

    private final int MAX_POKEMON_NUMBER = 50; // max number of pokemons in the list

    private PokemonRepository pokemonRepository;

    private MutableLiveData<List<Pokemon>> pokemonList = new MutableLiveData<>();
    private final MutableLiveData<Pokemon> selectedPokemon = new MutableLiveData<>();
    private MutableLiveData<List<PokemonImage>> imageList;

    private int totalNumberOfLoaded = 0;

    public PokemonViewModel(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
        this.pokemonList.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<Pokemon>> getPokemonList() {
        if (pokemonList.getValue() == null || pokemonList.getValue().isEmpty()) {
            loadPokemons();
        }
        return pokemonList;
    }

    public LiveData<Pokemon> getSelectedPokemon() {
        return selectedPokemon;
    }

    public void selectPokemon(Pokemon pokemon) {
        //in order to save memory reset loaded images from previous pokemon
        resetImages(selectedPokemon.getValue(), pokemon);
        // select new pokemon
        selectedPokemon.setValue(pokemon);
        // load images for new pokemon
        loadImages(selectedPokemon.getValue());
    }

    public MutableLiveData<List<PokemonImage>> getImageList() {
        return imageList;
    }

    // Do an asynchronous operation to fetch pokemons
    public void loadPokemons() {

        // limited number of pokemons to be loaded
        if (totalNumberOfLoaded >= MAX_POKEMON_NUMBER) {
            Log.w(Utils.LOG_TAG, "Maximum loaded number of pokemon is reached.");
            return;
        }

        Log.d(Utils.LOG_TAG, "Started loading pokemon list...");

        // fetch data
        pokemonRepository.getAllPokemonsAsync(result -> {
            if (result instanceof Result.Success) {
                // still is a background thread
                List<Pokemon> responseList = ((Result.Success<List<Pokemon>>) result).data;

                // keep track of total loaded items
                totalNumberOfLoaded += responseList.size();

                // append new result set to existing one
                pokemonList.getValue().addAll(responseList);

                // notify
                pokemonList.postValue(pokemonList.getValue());

                Log.d(Utils.LOG_TAG, String.format("Finished loading pokemon list. "
                                + "Loaded %s pokemons. Total number of pokemons in the list is: %s",
                        responseList.size(), pokemonList.getValue().size()));
            } else {
                // TODO Add Error handling
                Exception ex = ((Result.Error<List<Pokemon>>) result).exception;
                Log.e(Utils.LOG_TAG, "Error loading pokemon list! " + ex.getStackTrace());
            }
        });
    }

    // not in use
    public void loadPokemon(int id) {
        pokemonRepository.getPokemon(id, result -> {
            if (result instanceof Result.Success) {
                Pokemon pokemon = ((Result.Success<Pokemon>) result).data;

                selectedPokemon.postValue(pokemon);
            } else {
                // error
                Exception ex = ((Result.Error<Pokemon>) result).exception;
                Log.e(Utils.LOG_TAG, String.format("Error loading pokemon with id: %s. %s",
                        id, ex.getStackTrace()));
            }
        });
    }

    private void loadImages(Pokemon pokemon) {
        // initialize new list
        imageList = new MutableLiveData<List<PokemonImage>>();

        Log.d(Utils.LOG_TAG, "Started loading images...");

        // fetch images
        pokemonRepository.getPokemonImages(pokemon, result -> {
            if (result instanceof Result.Success) {
                Log.d(Utils.LOG_TAG, "Finished loading images...");

                // TODO pokemon and responsePokemon are the same reference, use only one
                Pokemon responsePokemon = ((Result.Success<Pokemon>) result).data;

                // notify
                imageList.postValue(responsePokemon.getPokemonImages());
            } else {
                // TODO Add Error handling
                Exception ex = ((Result.Error<Pokemon>) result).exception;
                Log.e(Utils.LOG_TAG, String.format("Error loading image list for pokemonId: %s. %s",
                        pokemon.getId(), ex.getStackTrace()));
            }

        });
    }

    private void resetImages(Pokemon previousPokemon, Pokemon newPokemon) {
        // keep images of the last viewed pokemon
        if (previousPokemon == null || previousPokemon == newPokemon) {
            return;
        }
        for (PokemonImage image : previousPokemon.getPokemonImages()) {
            // Leave only front default image - it is needed for list and toolbar image
            if (!image.getType().equals(PokemonImage.Type.Front_default)) {
                image.setBitmap(null);
            }
        }
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final PokemonRepository pokemonRepository;

        public Factory(@NonNull PokemonRepository pokemonRepository) {
            this.pokemonRepository = pokemonRepository;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PokemonViewModel(pokemonRepository);
        }
    }
}
