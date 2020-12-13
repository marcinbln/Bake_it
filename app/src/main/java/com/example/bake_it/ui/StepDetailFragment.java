package com.example.bake_it.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bake_it.R;
import com.example.bake_it.data.db.Recipe;
import com.example.bake_it.data.db.RecipesRepository;
import com.example.bake_it.models.Step;
import com.example.bake_it.viewModels.RecipeViewModel;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static com.example.bake_it.ui.MainListActivity.tabletFlag;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link StepDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RECIPE_OPEN = "recipe_open";
    private static final String RECIPE_STEP = "recipe_step";
    Guideline guideline5;
    Guideline guidelineFullScreen;

    boolean fullscreen = false;
    ImageView fullscreenButton;
    private int mOpenRecipe;
    private int mOpenStep;
    private RecipeViewModel recipeViewModel;
    private PlayerView exoPlayerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady;
    private long playbackPosition;
    private Uri uri;
    private boolean mLandscapeFlag;
    private View rootView;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newInstance(Integer recipeOpen, Integer stepOpen) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_OPEN, recipeOpen);
        args.putInt(RECIPE_STEP, stepOpen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
        }
        if (getArguments() != null) {
            mOpenRecipe = getArguments().getInt(RECIPE_OPEN);
            mOpenStep = getArguments().getInt(RECIPE_STEP);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            if (!uri.toString().equals("")) {
                initializePlayer();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT < 24 || player == null)) {
            if (!uri.toString().equals("")) {
                initializePlayer();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.step_detail_fragment, container, false);
        exoPlayerView = rootView.findViewById(R.id.exo_player);
        guideline5 = rootView.findViewById(R.id.player_height);

        guidelineFullScreen = getActivity().findViewById(R.id.guideline_tablet);

        fullscreenButton = rootView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sizeBef = (int) getContext().getResources().getDimension(R.dimen.player_height);

                // Fullscreen off
                if (fullscreen) {
                    exoPlayerFullscreen(false);

                    // Fullscreen on
                } else {
                    exoPlayerFullscreen(true);

                }
            }
        });

        if (rootView.findViewById(R.id.step_detail_landscape) != null) {
            mLandscapeFlag = true;
        }

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        Recipe recipe = RecipesRepository.getInstance(getContext()).getRecipesArray()[mOpenRecipe];

        Step step = recipe.getSteps().get(mOpenStep);
        final int stepsNumber = recipe.getSteps().size() - 1;
        if (getActivity() != null) {
            getActivity().setTitle(recipe.getName() + " - " + mOpenStep + "/" + (stepsNumber));
        }


        TextView tv = rootView.findViewById(R.id.description_short);
        if (mOpenStep != 0) {
            tv.setText(String.format("%d. %s", mOpenStep, step.getShortDescription()));
        } else {
            tv.setText(step.getShortDescription());
        }


        TextView tv2 = rootView.findViewById(R.id.step_description);
        String description = step.getDescription();
        description = description.replaceAll("\\d+\\.\\s", "");
        tv2.setText(description);

        Button nextButton = rootView.findViewById(R.id.next);
        Button previousButton = rootView.findViewById(R.id.previous);

        uri = Uri.parse(step.getVideoURL());

        //set exoplayer height to screens height
        if (mLandscapeFlag && !uri.toString().equals("")) {
            int height = getResources().getDisplayMetrics().heightPixels;
            ViewGroup.LayoutParams params = exoPlayerView.getLayoutParams();
            params.height = height;
        }

        if (uri.toString().equals("")) {
            exoPlayerView.setVisibility(View.GONE);

            if (guideline5 != null) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline5.getLayoutParams();
                params.guidePercent = 0f; // 45% // range: 0 <-> 1
                guideline5.setLayoutParams(params);
            }
        }


        if (!tabletFlag) {
            nextButton.setOnClickListener(v -> {
                        if (mOpenStep != stepsNumber) {
                            Intent intent = new Intent(getContext(), StepDetailActivity.class);
                            Bundle extras = new Bundle();
                            extras.putInt(StepDetailActivity.EXTRA_RECIPE_ID, mOpenRecipe);
                            extras.putInt(StepDetailActivity.EXTRA_RECIPE_ID_STEP, mOpenStep + 1);
                            intent.putExtras(extras);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
            );

            previousButton.setOnClickListener(v -> {
                if (mOpenStep != 0) {
                    //open as new activity as fragments don't handle showing/hiding ui elements very well
                    Intent intent = new Intent(getContext(), StepDetailActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(StepDetailActivity.EXTRA_RECIPE_ID, mOpenRecipe);
                    extras.putInt(StepDetailActivity.EXTRA_RECIPE_ID_STEP, mOpenStep - 1);
                    intent.putExtras(extras);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

        } else {
            nextButton.setVisibility(View.GONE);
            previousButton.setVisibility(View.GONE);

        }
        return rootView;
    }

    private void hideSystemUi() {

        //only hide UI in landscape mode
        if (mLandscapeFlag) {

            if (getActivity() != null) {

                getActivity().getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    }

    private void initializePlayer() {

        hideSystemUi();

        if (getContext() != null) {
            player = ExoPlayerFactory.newSimpleInstance(getContext());
            exoPlayerView.setPlayer(player);
            MediaSource mediaSource = buildMediaSource(uri);
            playWhenReady = recipeViewModel.isPlayWhenReady();
            playbackPosition = recipeViewModel.getPlaybackPosition();

            player.setPlayWhenReady(playWhenReady);
            player.seekTo(playbackPosition);
            player.prepare(mediaSource, false, false);
        }

    }

    private MediaSource buildMediaSource(Uri uri) {

        if (getContext() != null) {

            DataSource.Factory dataSourceFactory =
                    new DefaultDataSourceFactory(getContext(), "exoplayer");
            return new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);
        }
        return null;
    }

    private void releasePlayer() {

        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            playWhenReady = player.getPlayWhenReady();
            recipeViewModel.setPlaybackPosition(playbackPosition);
            recipeViewModel.setPlayWhenReady(playWhenReady);
            player.release();
            player = null;
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    private void exoPlayerFullscreen(Boolean booleanFullsceen) {

        //Fullscreen off
        if (!booleanFullsceen) {
            rootView.setBackgroundColor(getResources().getColor(R.color.background));
            fullscreenButton.setImageResource(R.drawable.exo_controls_fullscreen_enter);
            ConstraintLayout.LayoutParams paramsGuidline = (ConstraintLayout.LayoutParams) guidelineFullScreen.getLayoutParams();
            paramsGuidline.guidePercent = ResourcesCompat.getFloat(getResources(), R.dimen.guideline_perc);
            guidelineFullScreen.setLayoutParams(paramsGuidline);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) exoPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = (int) getContext().getResources().getDimension(R.dimen.player_height);
            exoPlayerView.setLayoutParams(params);
            fullscreen = false;

        } else {
            //Fullscreen on
            rootView.setBackgroundColor(Color.BLACK);
            fullscreenButton.setImageResource(R.drawable.exo_controls_fullscreen_exit);
            ConstraintLayout.LayoutParams paramsGuidline = (ConstraintLayout.LayoutParams) guidelineFullScreen.getLayoutParams();
            paramsGuidline.guidePercent = 0f;
            guidelineFullScreen.setLayoutParams(paramsGuidline);
            guidelineFullScreen.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) exoPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            exoPlayerView.setLayoutParams(params);
            fullscreen = true;

        }

    }

}
