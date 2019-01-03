package com.devaddins.slouch;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.FullTextIndex;
import com.couchbase.lite.FullTextIndexItem;
import com.couchbase.lite.IndexBuilder;

public class DatabaseManager {

    private static Database database;
    private static DatabaseManager instance = null;

    private DatabaseManager(Context context, String name) throws CouchbaseLiteException {
        DatabaseConfiguration configuration = new DatabaseConfiguration(context);
        database = new Database(name, configuration);
    }

    public static Database getDatabase(Context context, String name) throws CouchbaseLiteException {
        if (instance == null) {
            instance = new DatabaseManager(context, name);
        }
        return database;
    }
}
