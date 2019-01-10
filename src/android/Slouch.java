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
 * simple lazy couchbase
 * slouch://database_name/table_name/{id} 
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
        } else if (action.equals("put")) {
            Request req = new Request(args.getString(0), args.getString(1), context);
            this.put(req);
            return true;
        } else if (action.equals("post")) {
            Request req = new Request(args.getString(0), args.getString(1), context);
            this.post(req);
            return true;
        } else if (action.equals("delete")) {
            Request req = new Request(args.getString(0), context);
            this.delete(req);
            return true;
        } else if (action.equals("table")) {
            Request req = new Request(args.getString(0), args.getString(1), context);
            this.table(req);
            return true;
        } else if (action.equals("database")) {
            Request req = new Request(args.getString(0), context);
            this.database(req);
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

    private void get(Request request) {
        try {
            Database db =  DatabaseManager.getDatabase(this.getAppContext(), request.getDatabase());
            String id = request.getId();
            String type = request.getType();
            Query query;
            if (id != null && type != null) {
                query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(db))
                .where(Expression.property(TYPE_NAME).equalTo(Expression.string(type))
                    .and(Expression.property(ID_NAME).equalTo(Expression.string(id))));
            }
            else if (id != null) { 
                query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(db))
                .where(Expression.property(TYPE_NAME).equalTo(Expression.string(type)));
            }  
            else {
                query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(db));
            }
            request.getContext().success(toJson(query.execute()));
        } 
        catch (Exception x) {
            request.getContext().error(x.getMessage());
        }
    }

    private void put(Request request) {
        request.getContext().success(request.getType());
    }

    private void post(Request request) {
        try {
            String type = request.getType();
            if (type == null) {
                request.getContext().error("post requires a type");
            } 
            else {
                Database db = DatabaseManager.getDatabase(this.getAppContext(), request.getDatabase());
                Map<String, Object> dat = request.getDataMap();
                MutableDocument doc = new MutableDocument(dat);
                doc.setString(TYPE_NAME, request.getType());
                doc.setString(ID_NAME, doc.getId());
                db.save(doc);
                request.getContext().success("true");
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
    
    private void delete(ResultSet resultSet, Database database) throws CouchbaseLiteException {
        for (Result r: resultSet.allResults()) {
            String s = gson.toJson(r.toMap().get(database.getName()));
            Map<String, Object> m = getDataMap(s);
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
    
    private void table(Request request) {
        request.getContext().success(request.getType());
    }
    
    private void database(Request request) {
        request.getContext().success(request.getType());
    }
}
