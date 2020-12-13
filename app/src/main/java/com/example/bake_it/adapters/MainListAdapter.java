package com.example.bake_it.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bake_it.R;
import com.example.bake_it.data.db.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainListAdapter extends ListAdapter<Recipe, MainListAdapter.ViewHolder> {

    private static final List<Integer> mImages = new ArrayList<Integer>() {{
        add(R.drawable.nutella);
        add(R.drawable.brownies);
        add(R.drawable.yellowcake);
        add(R.drawable.cheesecake);

    }};

    private static final DiffUtil.ItemCallback<Recipe> RESULT_COMPARATOR = new DiffUtil.ItemCallback<Recipe>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem == newItem;
        }
    };


    final private ItemClickListener mItemClickListener;

    // Pass in the listener into the constructor
    public MainListAdapter(ItemClickListener listener) {
        super(RESULT_COMPARATOR);
        mItemClickListener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recipeItem = inflater.inflate(R.layout.main_list_rv_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(recipeItem);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = getItem(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(recipe.getName());
        ImageView imageView = holder.imageView;
        imageView.setImageResource(mImages.get(position));

    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemID);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView nameTextView;
        final ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.recipe_name);
            imageView = itemView.findViewById(R.id.recipe_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemID = getAdapterPosition();
            mItemClickListener.onItemClickListener(itemID);
        }
    }
}
