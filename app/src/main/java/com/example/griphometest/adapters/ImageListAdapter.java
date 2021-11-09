package com.example.griphometest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.griphometest.R;
import com.example.griphometest.model.PokemonImage;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {

    private final List<PokemonImage> imageList;

    public ImageListAdapter(List<PokemonImage> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListAdapter.ViewHolder holder, int position) {
        PokemonImage pokemonImage = imageList.get(position);

        if (pokemonImage.getBitmap() != null) {
            String textType = pokemonImage.getType().toString().replace("_", " ");
            holder.getTxtTitle().setText(textType);

            holder.getImageItem().setImageBitmap(pokemonImage.getBitmap());
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private ImageView imageItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.item_title);
            imageItem = (ImageView) itemView.findViewById(R.id.item_image);
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public ImageView getImageItem() {
            return imageItem;
        }
    }
}
