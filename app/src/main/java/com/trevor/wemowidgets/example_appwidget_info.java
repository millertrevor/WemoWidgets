package com.trevor.wemowidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.List;


/**
 * Implementation of App Widget functionality.
 */
public class example_appwidget_info extends AppWidgetProvider {

    private static final String ACTION_UPDATE_CLICK =
            "com.trevor.multiplewidgettest.action.UPDATE_CLICK";
    private static final String SeperatorCharacter = "|";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            AppWidgetConfigure.deleteTitlePref(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = AppWidgetConfigure.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_appwidget_info);
        //  views.setOnClickPendingIntent(R.id.widgetID,getPendingSelfIntent(context,ACTION_UPDATE_CLICK, appWidgetId));
        String modifiedAction = ACTION_UPDATE_CLICK+SeperatorCharacter+Integer.toString(appWidgetId);
        Intent intent = new Intent(context, example_appwidget_info.class);
        intent.setAction(modifiedAction);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.imageButton,pendingIntent);
        views.setTextViewText(R.id.appwidget_text, widgetText);

       // views.setTextViewText(R.id.widgetID,Integer.toString(appWidgetId));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String string = intent.getAction();
        String ActionMode;
        String incomingWidgetID;

        if (string.contains(SeperatorCharacter)) {
            // Split it.
            String[] parts = string.split("\\"+SeperatorCharacter);
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556
            ActionMode = part1;
            incomingWidgetID = part2;
        } else {
            //throw new IllegalArgumentException("String " + string + " does not contain "+SeperatorCharacter);
            ActionMode="NONE";
            incomingWidgetID="";
        }
        if (ACTION_UPDATE_CLICK.equals(ActionMode)) {
            //onUpdateFromReceive(context);
            //  updateAppWidget(context,appWidgetManager,mAppWidgetId);

            Toast.makeText(context, "From the "+incomingWidgetID+" widget (do special work)", Toast.LENGTH_SHORT).show();
        }
    }


}


