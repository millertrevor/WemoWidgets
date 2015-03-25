package com.trevor.wemowidgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;


public class AppWidgetConfigure extends ActionBarActivity implements View.OnClickListener {

    int mAppWidgetId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_widget_configure);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_widget_configure, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.refresh_button) {
           // refresh();
            // Intent intent = new Intent(getActivity(), WemoService.class);
            // getActivity().startService(_rememberedIntent);
            // getActivity().stopService(intent);
        }
        Context context = view.getContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.example_appwidget_info);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        Toast.makeText(context, "Totally clicking the " + mAppWidgetId + " widget", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setResultDataToWidget(int result) {
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, thisWidgetId);
        setResult(result, resultValue);
        finish();
    }

    public void saveToPreferences(String file_name, String data) {
        SharedPreferences myPrefs = getSharedPreferences("Data",MODE_PRIVATE );
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(file_name, data);
        prefsEditor.commit();
    }

    public String getWidgetData(String file_name) {
        SharedPreferences myPrefs = getSharedPreferences("Data",MODE_PRIVATE);
        return (myPrefs.getString(file_name, null));
    }
}
