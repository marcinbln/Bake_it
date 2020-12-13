package com.example.bake_it.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bake_it.R;
import com.example.bake_it.adapters.MyIngredientRecyclerViewAdapter;
import com.example.bake_it.models.Ingredient;

import java.util.List;


public class IngredientFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private List<Ingredient> mIngredients;

    public IngredientFragment() {
    }

    public static IngredientFragment newInstance(int columnCount) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredients_rv, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyIngredientRecyclerViewAdapter(mIngredients));
        }
        return view;
    }


    public void setIngredients(List<Ingredient> mIngredients) {
        this.mIngredients = mIngredients;
    }


}
