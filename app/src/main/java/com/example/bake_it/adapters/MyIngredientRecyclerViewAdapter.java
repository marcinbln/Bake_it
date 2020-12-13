package com.example.bake_it.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bake_it.R;
import com.example.bake_it.models.Ingredient;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display rv_image_placeholder {@link Ingredient} and makes rv_image_placeholder call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class MyIngredientRecyclerViewAdapter extends RecyclerView.Adapter<MyIngredientRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> mValues;

    public MyIngredientRecyclerViewAdapter(List<Ingredient> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mQuantity.setText(mValues.get(position).getQuantity());
        holder.mMeasure.setText(mValues.get(position).getMeasure());
        holder.mIngredient.setText(mValues.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mQuantity;
        final TextView mMeasure;
        final TextView mIngredient;
        Ingredient mItem;

        ViewHolder(View view) {
            super(view);
            mQuantity = view.findViewById(R.id.quantity);
            mMeasure = view.findViewById(R.id.measure);
            mIngredient = view.findViewById(R.id.ingredient);
        }


    }
}
