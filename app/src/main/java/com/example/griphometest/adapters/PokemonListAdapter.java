package com.example.griphometest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.griphometest.R;
import com.example.griphometest.model.Pokemon;
import com.example.griphometest.model.PokemonImage;

import java.util.List;

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.ViewHolder> {

    private List<Pokemon> pokemonDataSet;
    private ItemClickListener itemClickListener;

    public PokemonListAdapter(List<Pokemon> pokemonDataSet) {
        this.pokemonDataSet = pokemonDataSet;
    }

    @NonNull
    @Override
    public PokemonListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonListAdapter.ViewHolder viewHolder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Pokemon pokemon = pokemonDataSet.get(position);

        // Update UI
        viewHolder.getTextViewPokemonName().setText(pokemon.getName());

        String baseExperience = String.format("#%s", pokemon.getId());
        viewHolder.getTextPokemonId().setText(baseExperience);

        StringBuilder builder = new StringBuilder();
        for (String type: pokemon.getTypes()) {
            builder.append(type);
            builder.append("\n");
        }
        String textTypes = builder.toString().trim();
        viewHolder.getTextPokemonType().setText(textTypes);

        //Images
        viewHolder.getImageViewPokemon().setImageBitmap(
                pokemon.getImageByType(PokemonImage.Type.Front_default).getBitmap());
    }

    @Override
    public int getItemCount() {
        return pokemonDataSet.size();
    }

    // get data at click position
    Pokemon getPokemon(int position) {
        return pokemonDataSet.get(position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewPokemonName;
        private final TextView textPokemonId;
        private final TextView textPokemonType;
        private final ImageView imagePokemon;

        public ViewHolder(View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View

            textViewPokemonName = (TextView) itemView.findViewById(R.id.txtPokemonName);
            textPokemonId = (TextView) itemView.findViewById(R.id.txtPokemonId);
            textPokemonType = (TextView) itemView.findViewById(R.id.txtPokemonType);
            imagePokemon = (ImageView) itemView.findViewById(R.id.imgPokemon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        itemClickListener.onItemClick(getPokemon(position));
                    }
                }
            });
        }

        public TextView getTextViewPokemonName() {
            return textViewPokemonName;
        }

        public TextView getTextPokemonId() {
            return textPokemonId;
        }

        public TextView getTextPokemonType() {
            return textPokemonType;
        }

        public ImageView getImageViewPokemon() {
            return imagePokemon;
        }
    }


    public interface ItemClickListener {
        void onItemClick(Pokemon pokemon);
    }

}
