package com.example.bake_it.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class RecipeViewModel extends AndroidViewModel {

    private int selectedPosition = -1;
    private boolean playWhenReady = false;
    private long playbackPosition;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.playWhenReady = playWhenReady;
    }

    public Boolean isPlayWhenReady() {

        return playWhenReady;
    }

    public long getPlaybackPosition() {
        return playbackPosition;
    }

    public void setPlaybackPosition(long playbackPosition) {
        this.playbackPosition = playbackPosition;
    }


}
