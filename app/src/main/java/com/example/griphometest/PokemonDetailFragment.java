package com.example.griphometest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.griphometest.helper.ImageLoader;
import com.example.griphometest.helper.PokemonParser;
import com.example.griphometest.helper.Utils;
import com.example.griphometest.model.Pokemon;
import com.example.griphometest.model.PokemonImage;
import com.example.griphometest.model.Stat;
import com.example.griphometest.repository.PokemonRepository;
import com.example.griphometest.service.RetrofitClient;
import com.example.griphometest.adapters.ImageListAdapter;
import com.example.griphometest.viewmodel.PokemonViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class PokemonDetailFragment extends Fragment {

    private PokemonViewModel pokemonViewModel;
    private IFragmentNavigation fragmentNavigation;

    private TextView txtToolbarId;
    private TextView txtToolbarTypes;
    private ImageView toolbarImage;
    private ProgressBar loadingBar;

    private CollapsingToolbarLayout toolBarLayout;
    private TextView txtAboutDetails;
    private TextView txtBaseStatDetails;
    private RecyclerView recyclerView;
    private ImageListAdapter imageListAdapter;

    public static PokemonDetailFragment newInstance() {
        return new PokemonDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootFragmentView = inflater.inflate(R.layout.pokemon_detail_fragment, container,
                false);

        initViews(rootFragmentView);

        recyclerView = rootFragmentView.findViewById(R.id.imageList);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        return rootFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PokemonRepository repository =
                new PokemonRepository(RetrofitClient.getInstance().getPokemonService(), null,
                        PokemonParser.getInstance(), ImageLoader.getInstance(),
                        PokemonApplication.getInstance().executorService);
        PokemonViewModel.Factory factory = new PokemonViewModel.Factory(repository);

        pokemonViewModel = new ViewModelProvider(requireActivity(), factory).get(PokemonViewModel.class);
        pokemonViewModel.getSelectedPokemon().observe(getViewLifecycleOwner(), pokemon -> {

            Log.d(Utils.LOG_TAG, "View notified, selected pokemon changed : " + pokemon.getName());

            // populate toolbar layout
            toolBarLayout.setTitle(pokemon.getName());

            String textId = String.format("#%s", pokemon.getId());
            txtToolbarId.setText(textId);

            StringBuilder builder = new StringBuilder();
            for (String type: pokemon.getTypes()) {
                builder.append(type);
                builder.append("\n");
            }
            txtToolbarTypes.setText(builder.toString().trim());

            toolbarImage.setImageBitmap(pokemon.getImageByType(
                    PokemonImage.Type.Front_default).getBitmap());

            // Populate views with data
            populateDetails(pokemon);
        });

        pokemonViewModel.getImageList().observe(getViewLifecycleOwner(), pokemonImages -> {
            Log.d(Utils.LOG_TAG,
                    "View notified, pokemon images are loaded: " + pokemonImages.size());

            loadingBar.setVisibility(View.GONE);

            imageListAdapter = new ImageListAdapter(pokemonImages);
            recyclerView.setAdapter(imageListAdapter);
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


    private void initViews(View rootView) {
        toolBarLayout = rootView.findViewById(R.id.toolbar_layout);
        txtToolbarId = rootView.findViewById(R.id.txtToolbarId);
        txtToolbarTypes = rootView.findViewById(R.id.txtTypes);
        toolbarImage = rootView.findViewById(R.id.toolbarImgPokemon);
        loadingBar = rootView.findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.VISIBLE);

        txtAboutDetails = rootView.findViewById(R.id.txtAboutDetails);
        txtBaseStatDetails = rootView.findViewById(R.id.txtBaseStatDetails);
    }

    private void populateDetails(Pokemon pokemon) {
        txtAboutDetails.setText(getAboutDetails(pokemon));

        txtBaseStatDetails.setText(getBaseStatDetails(pokemon));
    }

    private String getAboutDetails(Pokemon pokemon) {

        String textAbout = String.format("name: %s\nbase experience: %s\n"
                        + "weight: %s Kg\nheight: %s cm", pokemon.getName(),
                pokemon.getBaseExperience(), pokemon.getWeight(), pokemon.getHeight());

        return textAbout;
    }

    private String getBaseStatDetails(Pokemon pokemon) {
        StringBuilder builder = new StringBuilder();
        for (Stat stat: pokemon.getStats()) {
            String textStat = String.format("%s:     %s", stat.getName(), stat.getBaseStat());
            builder.append(textStat);
            builder.append("\n");
        }

        return builder.toString().trim();
    }

}