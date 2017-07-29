package com.example.marcus.newsapp.utilities;
import com.example.marcus.newsapp.data.Item;

/**
 * Created by Marcus on 6/25/2017.
 */

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import android.util.Log;

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

public class NetworkUtil {
    public static final String TAG = "NetworkUtils";

    private static final String NEWS_BASE_URL = "https://newsapi.org/v1/articles?";
    final static String SOURCE_PARAM = "source";
    final static String SORTBY_PARAM = "sortBy";
    final static String APIKEY_PARAM = "apiKey";

    private static final String source = "the-next-web";
    private static final String sortBy = "latest";
    private static final String apiKey = "8ec7ea91ac5544748c71b271cb8b19e3";

    public static URL buildURL(){
        Uri builtUri = Uri.parse(NEWS_BASE_URL).buildUpon()
                .appendQueryParameter(SOURCE_PARAM, source)
                .appendQueryParameter(SORTBY_PARAM, sortBy)
                .appendQueryParameter(APIKEY_PARAM, apiKey)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.v(TAG, "Build URL" + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<Item> parseJSONFile(String json) throws Exception {
        JSONObject jsonArticle = new JSONObject(json);
        JSONArray articles = jsonArticle.getJSONArray("articles");

        ArrayList<Item> articleArray = new ArrayList<>();
        try {
            for (int i = 0; i < articles.length(); i++) {
                JSONObject unparsedArticle = articles.getJSONObject(i);

                String author = unparsedArticle.getString("author");
                String title = unparsedArticle.getString("title");
                String description = unparsedArticle.getString("description");
                String url = unparsedArticle.getString("url");
                String urlToImage = unparsedArticle.getString("urlToImage");
                String publishedAt = unparsedArticle.getString("publishedAt");

                Item parsedArticle = new Item(author, title, description, url, urlToImage, publishedAt);

                articleArray.add(parsedArticle);
            }
            return articleArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
