package com.devaddins.slouch;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import com.couchbase.lite.Database;

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

    Database database = null;

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

    private void get(Request request) {
        request.getContext().success(request.getTable());
    }

    private void put(Request request) {
        request.getContext().success(request.getTable());
    }

    private void post(Request request) {
        request.getContext().success(request.getTable());
    }

    private void delete(Request request) {
        request.getContext().success(request.getTable());
    }
    
    private void table(Request request) {
        request.getContext().success(request.getTable());
    }
    
    private void database(Request request) {
        request.getContext().success(request.getTable());
    }
}
