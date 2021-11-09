package com.example.griphometest;

import android.app.Application;
import android.util.Log;

import com.example.griphometest.helper.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PokemonApplication extends Application {

    // Gets the number of available cores
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CORES);

    private static PokemonApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        Log.d(Utils.LOG_TAG, "Starting Pokemon application.");
    }

    public static PokemonApplication getInstance() {
        return instance;
    }
}
