package com.devaddins.slouch;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.devaddins.slouch.Request;

/**
 * simple lazy couchbase
 * slouch://database_name/table_name/{id} 
 */
public class Slouch extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext context) throws JSONException {
        if (action.equals("get")) {
            // this.get(args.getString(0), context);
            Request req = new Request(args.getString(0), context);
            this.get(req);
            return true;
        } else if (action.equals("put")) {
            this.put(args.getString(0), args.getString(1), context);
            return true;
        } else if (action.equals("post")) {
            this.post(args.getString(0), args.getString(1), context);
            return true;
        } else if (action.equals("delete")) {
            this.delete(args.getString(0), context);
            return true;
        } else if (action.equals("table")) {
            this.table(args.getString(0), args.getString(1), context);
            return true;
        }
        return false;
    }

    private void get(String path, CallbackContext context) {
        try {
            context.success("foo");
        } 
        catch (Exception x) {
            context.error(x.getMessage());
        }
    }

    private void get(Request request) {
        // request.getContext().success(request.getPath());
        request.getContext().success(request.getTable());
        // request.getContext().success(request.getDatabase());
        // request.getContext().success(request.getId());
    }

    private void put(String path, String data, CallbackContext context) {
        try {
            context.success("foo");
        } 
        catch (Exception x) {
            context.error(x.getMessage());
        }
    }

    private void post(String path, String data, CallbackContext context) {
        try {
            context.success("foo");
        } 
        catch (Exception x) {
            context.error(x.getMessage());
        }
    }

    private void delete(String path, CallbackContext context) {
        try {
            context.success("foo");
        } 
        catch (Exception x) {
            context.error(x.getMessage());
        }
    }
    
    private void table(String path, String data, CallbackContext context) {
        try {
            context.success("foo");
        } 
        catch (Exception x) {
            context.error(x.getMessage());
        }
    }
}
