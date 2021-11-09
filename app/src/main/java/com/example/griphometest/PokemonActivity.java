package com.example.griphometest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class PokemonActivity extends AppCompatActivity implements IFragmentNavigation {

    private final String STATE_CURRENT_FRAGMENT = "State current fragment";

    private FragmentType currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        FragmentType fragmentType = FragmentType.List;
        if (savedInstanceState != null) {
            fragmentType = (FragmentType) savedInstanceState.getSerializable(STATE_CURRENT_FRAGMENT);
        }

        showFragment(fragmentType);
    }

    @Override
    public void navigateToFragment(FragmentType type) {
        showFragment(type);
    }

    @Override
    public void onBackPressed() {
        // todo fix navigation
        if (currentFragment.equals(FragmentType.List)) {
            Intent intent = new Intent(PokemonActivity.this, MainActivity.class);
            startActivity(intent);
            PokemonActivity.this.finish();
            return;
        }
        if (currentFragment.equals(FragmentType.Detail)) {
            showFragment(FragmentType.List);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current active fragment in order to bring it back
        outState.putSerializable(STATE_CURRENT_FRAGMENT, currentFragment);
    }

    private void showFragment(FragmentType type) {
        Fragment fragment = type.equals(FragmentType.List)
                //PokemonListFragment.newInstance()
                ? PokemonListFragmentKotlin.Companion.newInstance()
                : PokemonDetailFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        //.setReorderingAllowed(true)
        //.addToBackStack(null)

        currentFragment = type;
    }


}