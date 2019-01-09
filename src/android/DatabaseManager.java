package com.devaddins.slouch;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.FullTextIndex;
import com.couchbase.lite.FullTextIndexItem;
import com.couchbase.lite.IndexBuilder;

import java.util.Map;
import java.util.HashMap;

public class DatabaseManager {

    private static DatabaseManager instance = null;

    private static Map<String, Database> Databases = new HashMap<String, Database>();

    private static DatabaseConfiguration configuration = null;

    private DatabaseManager(Context context) throws CouchbaseLiteException {
        configuration = new DatabaseConfiguration(context);
    }

    private static void createDatabase(String name) throws CouchbaseLiteException {
        Databases.put(name, new Database(name, configuration));
    }

    public static Database getDatabase(Context context, String name) throws CouchbaseLiteException {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        if (!Databases.containsKey(name)) {
            createDatabase(name);
        }
        return Databases.get(name);
    }

    public static void remove(String name) {
        Databases.remove(name);
    }
}
