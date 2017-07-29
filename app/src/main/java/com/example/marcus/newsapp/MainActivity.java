package com.example.marcus.newsapp;
import com.example.marcus.newsapp.utilities.ScheduleUtils;

import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.content.Loader;
import android.widget.EditText;
import java.net.URL;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.AsyncTask;
import android.content.SharedPreferences;

import com.example.marcus.newsapp.data.Contract;
import com.example.marcus.newsapp.data.DBHelper;
import com.example.marcus.newsapp.utilities.DatabaseUtils;
import com.example.marcus.newsapp.utilities.NetworkUtil;
import com.example.marcus.newsapp.utilities.RefreshUtils;

import android.view.MenuItem;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

import android.net.Uri;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, NewsAdapter.ItemClickListener {

    private TextView mNewsTextView;
    private ProgressBar progress;
    private RecyclerView rv;
    private EditText search;
    private SQLiteDatabase database;
    private Cursor cursor;

    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mNewsTextView = (TextView) findViewById(R.id.news_data);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        //search = (EditText) findViewById(R.id.searchQuery);

        rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setLayoutManager( new LinearLayoutManager(this));

        //loadNewsData();

        // Check the app if it has already been installed.
        // If not load the database using the network methods

        // Check to see if the app has been launched before
        SharedPreferences launched = PreferenceManager.getDefaultSharedPreferences(this);
        boolean previouslyLaunched = launched.getBoolean("firstLaunch", false);

        if (!previouslyLaunched){
            load();

            SharedPreferences.Editor editor = launched.edit();
            editor.putBoolean("isFirst", false);
            editor.commit();
        }
        ScheduleUtils.scheduleRefresh(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        database = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.fetchNews(database);
        adapter = new NewsAdapter(cursor, this);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemNumber = item.getItemId();
        if (itemNumber == R.id.search){
            load();
        }
        return true;
    }

//    private void loadNewsData(){ new FetchNewsTask().execute();}
//

    // AsyncTaskLoader to replace AsyncTask.
    // Database is refreshed whenever the search button is used
    @Override
    public Loader<Void> onCreateLoader(int id, final Bundle args){
        return new AsyncTaskLoader<Void>(this) {
            @Override
            public Void loadInBackground() {
                RefreshUtils.updateItems(MainActivity.this);
                return null;
            }
            @Override
            protected void onStartLoading(){
                super.onStartLoading();
                progress.setVisibility(View.VISIBLE);
            }
        };
    }

    //
    @Override
    public void onLoadFinished(Loader<Void> loader, Void data){
        progress.setVisibility(View.GONE);
        database = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.fetchNews(database);

        adapter = new NewsAdapter(cursor, this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader){

    }

    @Override
    public void onItemClick(Cursor cursor, int clickedItemIndex){
        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(Contract.OBJECT_TABLES.COLUMN_NAME_URL));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void load(){
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(1, null, this).forceLoad();
    }

    public class FetchNewsTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            URL newsRequestURL = NetworkUtil.buildURL();
            try {
                String jsonNewsResponse = NetworkUtil.getResponseFromHttpUrl(newsRequestURL);
                return jsonNewsResponse;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String newsData){
            if(newsData != null){
                mNewsTextView.append((newsData) + "/n/n/n");
            }
            progress.setVisibility(View.INVISIBLE);
        }
    }
}
