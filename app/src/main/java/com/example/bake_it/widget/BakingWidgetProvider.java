package com.example.bake_it.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.core.app.TaskStackBuilder;

import com.example.bake_it.R;
import com.example.bake_it.ui.MainListActivity;
import com.example.bake_it.ui.StepsListActivity;

import static com.example.bake_it.Constants.EXTRA_RECIPE_ID;
import static com.example.bake_it.Constants.EXTRA_RECIPE_NAME;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    private static int mRecipeOpen = -1;
    private static String mRecipeName;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        // Click event handler for widget,
        // When list is empty launch MainListActivity, otherwise start StepsListActivity
        if (mRecipeOpen == -1) {
            Intent mainActivityIntent = new Intent(context, MainListActivity.class);
            PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_main, mainActivityPendingIntent);
        }
        Intent titleIntent = new Intent(context, StepsListActivity.class);
        titleIntent.putExtra(EXTRA_RECIPE_ID, mRecipeOpen);

        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.recipe_name, titlePendingIntent);

        remoteViews.setOnClickPendingIntent(R.id.widgetItemContainer, titlePendingIntent);

        Intent itemClickIntent = new Intent(context, StepsListActivity.class);
        itemClickIntent.putExtra(EXTRA_RECIPE_ID, mRecipeOpen);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(itemClickIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.list_view, clickPendingIntentTemplate);


        // Intent to launch MyWidgetRemoteViewsService that serves as adapter
        // In an intent pass recipe number for which list has to be loaded
        Intent remoteViewServiceIntent = new Intent(context, MyWidgetRemoteViewsService.class);
        remoteViewServiceIntent.setData(Uri.parse(String.valueOf(mRecipeOpen)));

        if (mRecipeName == null) {
            mRecipeName = "Bake_it";
        }
        remoteViews.setTextViewText(R.id.recipe_name, mRecipeName);
        remoteViews.setRemoteAdapter(R.id.list_view, remoteViewServiceIntent);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);

        //empty view
        remoteViews.setEmptyView(R.id.list_view, R.id.empty_view);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // This method is called when recipe is selected in the app
        // Recipe number is sent in the intent's extras
        // Need check for when receiver is first created and there is no extras

        if (intent.hasExtra(EXTRA_RECIPE_ID) && intent.getExtras() != null) {
            mRecipeOpen = intent.getExtras().getInt(EXTRA_RECIPE_ID);
            mRecipeName = intent.getExtras().getString(EXTRA_RECIPE_NAME);
        }

        super.onReceive(context, intent);
    }
}

