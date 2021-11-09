package com.example.griphometest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.griphometest.adapters.PokemonListAdapter;
import com.example.griphometest.helper.ImageLoader;
import com.example.griphometest.helper.PokemonParser;
import com.example.griphometest.helper.Utils;
import com.example.griphometest.repository.PokemonRepository;
import com.example.griphometest.service.RetrofitClient;
import com.example.griphometest.viewmodel.PokemonViewModel;

public class PokemonListFragment extends Fragment {

    private PokemonViewModel pokemonViewModel;
    private PokemonListAdapter pokemonListAdapter;
    private IFragmentNavigation fragmentNavigation;

    private RecyclerView recyclerView;
    private NestedScrollView scrollView;
    private ProgressBar loadingBar;

    public static PokemonListFragmentKotlin newInstance() {
        return new PokemonListFragmentKotlin();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootFragmentView = inflater.inflate(R.layout.pokemon_list_fragment, container, false);
        recyclerView = (RecyclerView) rootFragmentView.findViewById(R.id.listView);
        //        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));

        scrollView = rootFragmentView.findViewById(R.id.idNestedScrollView);
        loadingBar = rootFragmentView.findViewById(R.id.idLoadingBar);

        setScroll();

        return rootFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init dependencies
        PokemonRepository repository =
                new PokemonRepository(RetrofitClient.getInstance().getPokemonService(), null,
                        PokemonParser.getInstance(),ImageLoader.getInstance(),
                        PokemonApplication.getInstance().executorService);

        PokemonViewModel.Factory factory = new PokemonViewModel.Factory(repository);
        pokemonViewModel = new ViewModelProvider(requireActivity(), factory).get(PokemonViewModel.class);
        pokemonViewModel.getPokemonList().observe(getViewLifecycleOwner(), pokemonList -> {
            Log.d(Utils.LOG_TAG, "View notified, list size: " + pokemonList.size());

            if (pokemonList.isEmpty()) {
                return;
            }

            loadingBar.setVisibility(View.INVISIBLE);

            pokemonListAdapter = new PokemonListAdapter(pokemonList);
            recyclerView.setAdapter(pokemonListAdapter);

            // set listener
            pokemonListAdapter.setItemClickListener(pokemon -> {
                Log.d(Utils.LOG_TAG, String.format("Clicked on pokemon: %s (Id: %s)",
                        pokemon.getName(), pokemon.getId()));
                pokemonViewModel.selectPokemon(pokemon);
                goToPokemonDetails();
            });

        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            fragmentNavigation = (IFragmentNavigation) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " must implement IFragmentNavigation!");
        }
    }

    private void setScroll() {
        // adding on scroll change listener method for our nested scroll view.
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {

                    loadingBar.setVisibility(View.VISIBLE);
                    pokemonViewModel.loadPokemons();
                }
            }
        });
    }

    private void goToPokemonDetails() {
        if (fragmentNavigation != null) {
            fragmentNavigation.navigateToFragment(FragmentType.Detail);
        }
    }

}