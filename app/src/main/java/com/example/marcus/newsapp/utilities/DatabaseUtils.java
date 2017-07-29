package com.example.marcus.newsapp.utilities;
import com.example.marcus.newsapp.data.Contract;
import com.example.marcus.newsapp.data.Item;

import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import org.json.JSONException;

/**
 * Created by Marcus on 7/28/2017.
 */

public class DatabaseUtils {
    // Method to delete all items within the database
    public static void removeItems(SQLiteDatabase database){
        database.delete(Contract.OBJECT_TABLES.TABLE_NAME, null, null);
    }

    // Method to embed all items within the database by using an ArrayList
    public static void embedItem (SQLiteDatabase database, ArrayList<Item> newsItem){
        database.beginTransaction();

        try {
            for(Item i : newsItem){
                ContentValues contentvalues = new ContentValues();

                // Getters from Contract use contentvalues
                contentvalues.put(Contract.OBJECT_TABLES.COLUMN_NAME_TITLE, i.getTitle());
                contentvalues.put(Contract.OBJECT_TABLES.COLUMN_NAME_DESC ,i.getDescription());
                contentvalues.put(Contract.OBJECT_TABLES.COLUMN_NAME_URL ,i.getUrlToImage());
                contentvalues.put(Contract.OBJECT_TABLES.COLUMN_NAME_DATE ,i.getPublishedAt());

                database.insert(Contract.OBJECT_TABLES.TABLE_NAME, null, contentvalues);
            }

            database.setTransactionSuccessful();
        }
//        catch (IOException e){
//            e.printStackTrace();
//        } catch (JSONException e){
//            e.printStackTrace();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
        finally {
            database.endTransaction();
            database.close();
        }
    }

    public static Cursor fetchNews (SQLiteDatabase database){
        Cursor c = database.query(
                Contract.OBJECT_TABLES.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Contract.OBJECT_TABLES.COLUMN_NAME_DATE + " DESC"
        );

        return c;
    }
}
