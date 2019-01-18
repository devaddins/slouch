package com.devaddins.slouch;

import android.content.Context;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import com.couchbase.lite.Database;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Document;
import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.Result;
import com.couchbase.lite.SelectResult;
import com.couchbase.lite.CouchbaseLiteException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.devaddins.slouch.Request;
import com.devaddins.slouch.DatabaseManager;


/**
 * simple lazy couchbase lite
 * slouch://database_name/type_name/{id} 
 */
public class Slouch extends CordovaPlugin {

    private Gson gson = new Gson();
    private static String TYPE_NAME = "_type";
    private static String ID_NAME = "_id";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext context) throws JSONException {
        if (action.equals("get")) {
            Request req = new Request(args.getString(0), context);
            this.get(req);
            return true;
        } else if (action.equals("post")) {
            Request req = new Request(args.getString(0), args.getString(1), context);
            this.post(req);
            return true;
        } else if (action.equals("delete")) {
            Request req = new Request(args.getString(0), context);
            this.delete(req);
            return true;
        }
        return false;
    }

    private Context getAppContext() {
        return this.cordova.getActivity().getApplicationContext(); 
    }

    private String toJson(ResultSet resultSet) {
        ArrayList<Map<String, Object>> rslt = new ArrayList<Map<String, Object>>();
        for (Result r: resultSet.allResults()) {
            rslt.add(r.toMap());
        }
        return gson.toJson(rslt);
    }

    private void fetch(Request request, String type, String id)
    throws CouchbaseLiteException {
        Database db =  DatabaseManager.getDatabase(this.getAppContext(), request.getDatabase());
        Query query = QueryBuilder.select(SelectResult.all())
            .from(DataSource.database(db))
            .where(Expression.property(TYPE_NAME).equalTo(Expression.string(type))
            .and(Expression.property(ID_NAME).equalTo(Expression.string(id))));
        request.getContext().success(toJson(query.execute()));
    }

    private void fetch(Request request, String type) 
    throws CouchbaseLiteException {
        Database db =  DatabaseManager.getDatabase(this.getAppContext(), request.getDatabase());
        Query query = QueryBuilder.select(SelectResult.all())
            .from(DataSource.database(db))
            .where(Expression.property(TYPE_NAME).equalTo(Expression.string(type)));
        request.getContext().success(toJson(query.execute()));
    }

    private void fetch(Request request) 
    throws CouchbaseLiteException {
        Database db =  DatabaseManager.getDatabase(this.getAppContext(), request.getDatabase());
        Query query = QueryBuilder.select(SelectResult.all())
            .from(DataSource.database(db));
        request.getContext().success(toJson(query.execute()));
    }

    private void get(Request request) {
        try {
            String id = request.getId();
            String type = request.getType();
            if (id != null && type != null) {
                fetch(request, type, id);
            }
            else if (type != null) { 
                fetch(request, type);
            }  
            else {
                fetch(request);
            }
        } 
        catch (Exception x) {
            request.getContext().error(x.getMessage());
        }
    }

    private void post(Request request) {
        try {
            String type = request.getType();
            String id = request.getId();
            Database db = DatabaseManager.getDatabase(this.getAppContext(), request.getDatabase());
            if (type == null) {
                request.getContext().error("post requires a type");
            } 
            else {
                if (id == null ) {
                    Map<String, Object> dat = request.getDataMap();
                    MutableDocument doc = new MutableDocument(dat);
                    String i = doc.getId();
                    doc.setString(TYPE_NAME, type);
                    doc.setString(ID_NAME, i);
                    db.save(doc);
                    fetch(request, type, i);
                } else {
                    Map<String, Object> dat = request.getDataMap();
                    Document doc = db.getDocument(id);
                    if (doc != null) {
                        MutableDocument mdoc = new MutableDocument(id, dat);
                        mdoc.setString(TYPE_NAME, type);
                        mdoc.setString(ID_NAME, id);
                        db.save(mdoc);
                        fetch(request, type, id);
                    } else {
                        request.getContext().error("could not get document with id " + id);
                    }
                }
            }
        }
        catch (Exception x) {
            request.getContext().error(x.getMessage());
        }
    }
    
    private Map<String, Object> getDataMap(String data) throws JsonSyntaxException {
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        return gson.fromJson(data, mapType);
    }

    private Map<String, Object> getUnwrapped(Result result, String name) {
        Map<String, Object> map = null;
        Map<String, Object> resMap = result.toMap();
        if (resMap != null) {
            Object obj = resMap.get(name);
            if (obj != null) {
                String data = gson.toJson(obj);
                map = getDataMap(data);
            }
        }
        return map;
    }
    
    private void delete(ResultSet resultSet, Database database) throws CouchbaseLiteException {
        for (Result r: resultSet.allResults()) {
            Map<String, Object> m = getUnwrapped(r, database.getName());
            String id = m.get(ID_NAME).toString();
            if (id == null) {
                throw new CouchbaseLiteException("no id field on result");
            } else  {
                Document doc = database.getDocument(id);
                if (doc == null) {
                    throw new CouchbaseLiteException("document not found for id " + id);
                } else {
                    database.delete(doc);
                }
            }
        }
    }

    private void delete(Request request) {
        try {
            String dbName = request.getDatabase();
            Database db =  DatabaseManager.getDatabase(this.getAppContext(), dbName);
            String id = request.getId();
            String type = request.getType();
            if (id != null && type != null) {
                if (id.equals("DELETE_TYPE_" + type)){ 
                    Query query = QueryBuilder.select(SelectResult.all())
                    .from(DataSource.database(db))
                    .where(Expression.property(TYPE_NAME).equalTo(Expression.string(type)));
                    delete(query.execute(), db);
                } 
                else if (type.equals("DELETE_DATABASE") && id.equals("DELETE_DATABASE_" + dbName)) {
                    db.delete();
                    DatabaseManager.remove(dbName);
                }
                else {
                    Query query = QueryBuilder.select(SelectResult.all())
                    .from(DataSource.database(db))
                    .where(Expression.property(TYPE_NAME).equalTo(Expression.string(type))
                        .and(Expression.property(ID_NAME).equalTo(Expression.string(id))));
                    delete(query.execute(), db);
                }
                request.getContext().success("true");
            } else {
                request.getContext().error("delete expects an id");
            }
        } 
        catch (Exception x) {
            request.getContext().error(x.getMessage());
        }
    }
}
