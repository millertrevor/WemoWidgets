package com.trevor.wemowidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.List;


/**
 * Implementation of App Widget functionality.
 */
public class example_appwidget_info extends AppWidgetProvider {

    private static final String ACTION_UPDATE_CLICK =
            "com.trevor.wemo.action.UPDATE_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        //TODO: How can we target just the single widget we want to target here, instead of all of them?
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }

      /*  String message = getMessage();

        // Loop for every App Widget instance that belongs to this provider.
        // Noting, that is, a user might have multiple instances of the same
        // widget on
        // their home screen.
        for (int appWidgetID : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.my_widget);

            remoteViews.setTextViewText(R.id.textView_output, message);
            remoteViews.setOnClickPendingIntent(R.id.button_update,
                    getPendingSelfIntent(context,
                            ACTION_UPDATE_CLICK)
            );

            appWidgetManager.updateAppWidget(appWidgetID, remoteViews);

        }*/

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

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        Toast.makeText(context, "From the "+appWidgetId+" widget", Toast.LENGTH_SHORT).show();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_appwidget_info);
       // views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setOnClickPendingIntent(R.id.imageButton,getPendingSelfIntent(context,ACTION_UPDATE_CLICK));
        views.setTextViewText(R.id.textView2,"uuid:Lightswitch-1_0-221346K13004AD");
       // Bundle bundle = new Bundle();
      //  bundle.putCharSequence("ID", "uuid:Lightswitch-1_0-221346K13004AD");

       // bundle.putParcelable("RemoteView", views);
       // bundle.putString("test", "testString");


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


      //  RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.my_widget);

      //  remoteViews.setTextViewText(R.id.textView_output, message);



     //   appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
    }

    private void onUpdateFromReceive(Context context) {
        List<Device> allForTest = Device.listAll(Device.class);
        for (Device device : allForTest) {
           // context
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance
                (context);

        // Uses getClass().getName() rather than MyWidget.class.getName() for
        // portability into any App Widget Provider Class
        ComponentName thisAppWidgetComponentName =
                new ComponentName(context.getPackageName(),getClass().getName()
                );
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // Find the widget id from the intent.
      //  Intent intent = getIntent();

        Bundle extras = intent.getExtras();
       // intent.get
        if (extras != null) {
           int mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int stop=1;
            CharSequence bundlestring = extras.getCharSequence("ID");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), example_appwidget_info.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            stop = 2;
        }

        if (ACTION_UPDATE_CLICK.equals(intent.getAction())) {
            onUpdateFromReceive(context);
        }
    }
    private static PendingIntent getPendingSelfIntent(Context context, String action) {
        // An explicit intent directed at the current class (the "self").
        Intent intent = new Intent(context, example_appwidget_info.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}


