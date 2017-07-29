package com.example.marcus.newsapp.utilities;
import com.example.marcus.newsapp.data.DBHelper;
import com.example.marcus.newsapp.data.Item;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.io.IOException;
import org.json.JSONException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Marcus on 7/28/2017.
 */

public class RefreshUtils {
    // updateItems deletes are items within the database and attempts to grab
    // new items to recreate a new database and display the results
    public static void updateItems(Context c){
        ArrayList<Item> itemList = null;
        URL jsonURL = NetworkUtil.buildURL();
        SQLiteDatabase database = new DBHelper(c).getWritableDatabase();

        try {
            DatabaseUtils.removeItems(database);

            String jsonItem = NetworkUtil.getResponseFromHttpUrl(jsonURL);
            itemList = NetworkUtil.parseJSONFile(jsonItem);

            DatabaseUtils.embedItem(database, itemList);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        database.close();
    }
}
