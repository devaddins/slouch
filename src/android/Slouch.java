package com.devaddins.slouch;

import android.content.Context;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import com.couchbase.lite.Database;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.Result;
import com.couchbase.lite.SelectResult;
import com.couchbase.lite.CouchbaseLiteException;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    Gson gson = new Gson();

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

    private void delete(ResultSet resultSet, Database database) throws CouchbaseLiteException {
        ArrayList<Map<String, Object>> rslt = new ArrayList<Map<String, Object>>();
        for (Result r: resultSet.allResults()) {
            // database.delete(r); // TODO! does not work
        }
    }

    private void get(Request request) {
        try {
            Database db =  DatabaseManager.getDatabase(this.getAppContext(), request.getDatabase());
            String id = request.getId();
            String type = request.getType();
            Query query;
            if (id == null || id.equals("")){ 
                query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(db))
                .where(Expression.property("type").equalTo(Expression.string(type)));
            } 
            else {
                query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(db))
                .where(Expression.property("type").equalTo(Expression.string(type))
                    .and(Expression.property("id").equalTo(Expression.string(id))));
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
            Database db = DatabaseManager.getDatabase(this.getAppContext(), request.getDatabase());

            MutableDocument doc = new MutableDocument(request.getDataMap());
            doc.setString("type", request.getType());
            doc.setString("id", doc.getId());
            db.save(doc);
            request.getContext().success("true");
        }
        catch (Exception x) {
            request.getContext().error(x.getMessage());
        }
    }

    private void delete(Request request) {
        try {
            Database db =  DatabaseManager.getDatabase(this.getAppContext(), request.getDatabase());
            String id = request.getId();
            String type = request.getType();
            Query query;
            if (id == null || id.equals("")){ 
                query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(db))
                .where(Expression.property("type").equalTo(Expression.string(type)));
            } 
            else {
                query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(db))
                .where(Expression.property("type").equalTo(Expression.string(type))
                    .and(Expression.property("id").equalTo(Expression.string(id))));
            }
            delete(query.execute(), db);
            request.getContext().success("true");
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
