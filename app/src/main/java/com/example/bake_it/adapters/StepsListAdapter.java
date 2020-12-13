package com.example.bake_it.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bake_it.R;
import com.example.bake_it.models.Step;

import java.util.List;

import static com.example.bake_it.ui.MainListActivity.tabletFlag;


public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.ViewHolder> {

    final private ItemClickListener mItemClickListener;
    private final List<Step> mSteps;
    private int mSelectedPosition;

    public StepsListAdapter(List<Step> steps, ItemClickListener listener, int selectedPosition) {
        mSteps = steps;
        mItemClickListener = listener;
        mSelectedPosition = selectedPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recipeItem = inflater.inflate(R.layout.steps_rv_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(recipeItem);

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //highlight active step only on tablet
        if (tabletFlag) {
            if (mSelectedPosition == -1 & position == 0) {
                //highlight 1st step on tablets on recipe opening
                holder.itemView.setBackgroundColor(Color.parseColor("#F3C6A2"));
            } else {
                //highlight open step and unhighlight all other
                if (mSelectedPosition == position) {
                    holder.itemView.setBackgroundColor(Color.parseColor("#F3C6A2"));
                } else {
                    holder.itemView.setBackgroundColor(Color.parseColor("#F3E1D2"));
                }
            }
        }

        Step step = mSteps.get(position);
        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        if (position == 0) {
            textView.setText(step.getShortDescription());
        } else {
            textView.setText(String.format("%d. %s", position, step.getShortDescription()));
        }

    }

    @Override
    public int getItemCount() {
        return mSteps.size();

    }

    public interface ItemClickListener {
        void onItemClickListener(int itemID);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView nameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.recipe_step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemID = getAdapterPosition();
            mSelectedPosition = itemID;
            mItemClickListener.onItemClickListener(itemID);
            notifyDataSetChanged();
        }
    }

}
