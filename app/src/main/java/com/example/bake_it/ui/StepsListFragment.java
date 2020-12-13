package com.example.bake_it.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bake_it.R;
import com.example.bake_it.adapters.StepsListAdapter;
import com.example.bake_it.data.db.Recipe;
import com.example.bake_it.data.db.RecipesRepository;
import com.example.bake_it.models.Ingredient;
import com.example.bake_it.models.Step;
import com.example.bake_it.viewModels.RecipeViewModel;

import java.util.List;

import static com.example.bake_it.Constants.SPAN_COUNT_ONE;


public class StepsListFragment extends Fragment implements StepsListAdapter.ItemClickListener {
    private static final String OPEN_RECIPE = "open_recipe";

    private int mRecipeOpen;
    private RecipeViewModel recipeViewModel;

    private OnFragmentInteractionListener mListener;

    public StepsListFragment() {
        // Required empty public constructor
    }

    public static StepsListFragment newInstance(int openRecipe) {
        StepsListFragment fragment = new StepsListFragment();
        Bundle args = new Bundle();
        args.putInt(OPEN_RECIPE, openRecipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeOpen = getArguments().getInt(OPEN_RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        Recipe[] recipeList = RecipesRepository.getInstance(getContext()).getRecipesArray();
        View rootView = inflater.inflate(R.layout.steps_list_fragment, container, false);

        //if detail activity is launched from intent but apps had been killed - open mainactivity
        if (recipeList != null) {

            // Inflate the layout for this fragment
            RecyclerView rvContacts = rootView.findViewById(R.id.steps_rv);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), SPAN_COUNT_ONE);

            if (MainListActivity.tabletFlag) {
                //if tablet - set to gridlayout to three columns
                mLayoutManager = new GridLayoutManager(this.getContext(), SPAN_COUNT_ONE);
                rvContacts.setLayoutManager(mLayoutManager);
            }

            rvContacts.setLayoutManager(mLayoutManager);


            Recipe recipe = recipeList[mRecipeOpen];

            if (getActivity() != null) {

                getActivity().setTitle(recipe.getName());
            }

            List<Ingredient> ingredientsAll = recipe.getIngredients();

            IngredientFragment ingredientFragment = new IngredientFragment();
            ingredientFragment.setIngredients(ingredientsAll);
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.ingredients_list, ingredientFragment)
                    .commit();

            List<Step> steps = recipe.getSteps();

            StepsListAdapter adapter = new StepsListAdapter(steps, this, recipeViewModel.getSelectedPosition());
            rvContacts.setAdapter(adapter);

        } else {

            if (getActivity() != null) {
                getActivity().finish();
            }
            Intent intent = new Intent(getContext(), MainListActivity.class);
            startActivity(intent);
        }
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClickListener(int itemID) {
        recipeViewModel.setSelectedPosition(itemID);
        mListener.onFragmentInteraction(itemID);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Integer integer);
    }


}
