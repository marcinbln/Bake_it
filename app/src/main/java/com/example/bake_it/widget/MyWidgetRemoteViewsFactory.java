package com.example.bake_it.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bake_it.R;
import com.example.bake_it.data.db.AppDatabase;
import com.example.bake_it.data.db.Recipe;

import java.util.List;

import static com.example.bake_it.Constants.EXTRA_ITEM;
import static com.example.bake_it.Constants.EXTRA_RECIPE_ID;

public class MyWidgetRemoteViewsFactory extends BroadcastReceiver implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private List<Recipe> recipeList;
    private int mRecipeOpen;

    public MyWidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        if (intent.getData() != null) {
            mRecipeOpen = Integer.valueOf(intent.getData().getSchemeSpecificPart());
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        if (mRecipeOpen == -1) {
            recipeList = null;
        } else {
            recipeList = AppDatabase.getInstance(mContext).recipeDao().getAll();
        }
    }

    @Override
    public void onDestroy() {
        Log.v("xxx destroy", "");
        //   unRegisterReceiver();
    }

    @Override
    public int getCount() {

        int count = 0;

        if (!(recipeList == null)) {
            count = recipeList.get(mRecipeOpen).ingredients.size();
        }
        return count;

    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        if (mRecipeOpen == -1) {
            recipeList = null;
        } else {
            rv.setTextViewText(R.id.quantity, recipeList.get(mRecipeOpen).ingredients.get(position).getQuantity());
            rv.setTextViewText(R.id.measurement, recipeList.get(mRecipeOpen).ingredients.get(position).getMeasure());
            rv.setTextViewText(R.id.ingredient, recipeList.get(mRecipeOpen).ingredients.get(position).getIngredient());

            Bundle extras = new Bundle();
            extras.putInt(EXTRA_ITEM, mRecipeOpen);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve recipe number received from BakingWidgetProvider
        mRecipeOpen = intent.getIntExtra(EXTRA_RECIPE_ID, 0);
    }
}





