package com.example.griphometest.repository;

import android.util.Log;

import com.example.griphometest.database.IPokemonPersistenceSource;
import com.example.griphometest.helper.IImageLoader;
import com.example.griphometest.helper.IPokemonParser;
import com.example.griphometest.helper.Utils;
import com.example.griphometest.model.Pokemon;
import com.example.griphometest.model.PokemonImage;
import com.example.griphometest.pojomodel.PokemonData;
import com.example.griphometest.pojomodel.PokemonPojo;
import com.example.griphometest.pojomodel.UrlContainer;
import com.example.griphometest.service.PokemonServiceApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Responsible for fetching the data
public class PokemonRepository {

    private final int LIMIT = 10; // batch size

    private PokemonServiceApi pokemonServiceApi; // service
    private IPokemonPersistenceSource persistenceSource; // database
    private IPokemonParser pokemonParser;
    private IImageLoader imageLoader;
    private final Executor executor;

    private int nextOffset = 1;

    // not used
    private static Map<Integer, Pokemon> pokemonCache;

    static {
        pokemonCache = new HashMap<>();
    }

    public PokemonRepository(PokemonServiceApi pokemonServiceApi,
                             IPokemonPersistenceSource persistenceSource,
                             IPokemonParser pokemonParser, IImageLoader imageLoader,
                             Executor executor) {
        this.pokemonServiceApi = pokemonServiceApi;
        this.persistenceSource = persistenceSource;
        this.pokemonParser = pokemonParser;
        this.imageLoader = imageLoader;
        this.executor = executor;
    }

    public void getAllPokemonsAsync(RepositoryCallback<List<Pokemon>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<PokemonData> pokemonCall = pokemonServiceApi.getPokemons(LIMIT, nextOffset);
                    Response<PokemonData> responseData = pokemonCall.execute();
                    PokemonData pokemonData = responseData.body();

                    // next batch offset
                    nextOffset = pokemonData.getNextOffset();

                    List<Pokemon> response = new ArrayList<>();

                    // Go through pokemon list and fetch data for each in the batch
                    for (UrlContainer rawData : pokemonData.getData()) {

                        String url = rawData.getUrl();
                        String path = url.replace(PokemonServiceApi.BASE_URL, "");

                        // Get Pokemon
                        Call<PokemonPojo> pokemonPojoCall = pokemonServiceApi.getPokemon(path);
                        Response<PokemonPojo> responsePokemon = pokemonPojoCall.execute();
                        PokemonPojo pokemonPojo = responsePokemon.body();

                        // Parse
                        Pokemon pokemon = pokemonParser.parsePokemon(pokemonPojo);

                        // Load only front default image bitmap, other images will be loaded later
                        imageLoader.loadBitmaps(pokemon, PokemonImage.Type.Front_default);

                        response.add(pokemon);
                    }

                    callback.onComplete(new Result.Success<>(response));
                } catch (Exception ex) {
                    Log.e("error", ex.getMessage());
                    callback.onComplete(new Result.Error<>(ex));
                }
            }
        });
    }

    public void getPokemonImages(Pokemon pokemon, RepositoryCallback<Pokemon> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // get images int pokemon object
                    imageLoader.loadBitmaps(pokemon, null);

                    callback.onComplete(new Result.Success<>(pokemon));
                } catch (IOException ex) {
                    Log.e("error", ex.getMessage());
                    callback.onComplete(new Result.Error<>(ex));
                }
            }
        });
    }

    // not used for now
    public void getPokemon(int id, RepositoryCallback<Pokemon> callback) {
        Pokemon pokemon = pokemonCache.get(id);
        if (pokemon != null) {
            callback.onComplete(new Result.Success<Pokemon>(pokemon));
            return;
        }

        Call<PokemonPojo> call = pokemonServiceApi.getPokemonById(Integer.toString(id));
        call.enqueue(new Callback<PokemonPojo>() {
            @Override
            public void onResponse(Call<PokemonPojo> call, Response<PokemonPojo> response) {
                PokemonPojo pokemonPojo = response.body();

                if (callback != null) {
                    Pokemon pokemon = pokemonParser.parsePokemon(pokemonPojo);
                    pokemonCache.put(id, pokemon);
                    callback.onComplete(new Result.Success<Pokemon>(pokemon));
                }

                Log.d(Utils.LOG_TAG, "onResponse: getPokemon! Name: "
                        + (pokemonPojo != null ? pokemonPojo.getName() : "NULL"));
            }

            @Override
            public void onFailure(Call<PokemonPojo> call, Throwable th) {
                Log.e("error", th.getMessage());
                new Result.Error<Pokemon>(new Exception(th));
            }
        });
    }

}
