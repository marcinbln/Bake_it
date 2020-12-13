package com.example.bake_it.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.bake_it.data.db.Recipe;
import com.example.bake_it.data.db.RecipesRepository;

public class MainListViewModel extends AndroidViewModel {


    private final RecipesRepository repository;

    public MainListViewModel(@NonNull Application application) {
        super(application);
        repository = RecipesRepository.getInstance(application);
    }

    public MutableLiveData<Recipe[]> getRecipesVM() {
        return repository.getRecipesRepo();
    }


}
