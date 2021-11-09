package com.example.griphometest;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnProceed).setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, PokemonActivity.class);
            MainActivity.this.startActivity(intent);

        });
    }


}