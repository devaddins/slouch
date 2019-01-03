package com.devaddins.slouch;

import android.content.Context;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import com.couchbase.lite.Database;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private void get(Request request) {
        try {
            Database db =  DatabaseManager.getDatabase(this.getAppContext(), request.getDatabase());
            String id = request.getId();
                Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(db))
                .where(Expression.property("type").equalTo(Expression.string(request.getType())));
                ResultSet rs = query.execute();
                request.getContext().success(gson.toJson(rs.allResults()));
        } 
        catch (Exception x) {
            request.getContext().error(x.getMessage());
        }
    }

    private void put(Request request) {
        request.getContext().success(request.getType());
    }

    private void post(Request request) {
        request.getContext().success(request.getType());
    }

    private void delete(Request request) {
        request.getContext().success(request.getType());
    }
    
    private void table(Request request) {
        request.getContext().success(request.getType());
    }
    
    private void database(Request request) {
        request.getContext().success(request.getType());
    }
}
