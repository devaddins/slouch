package com.devaddins.slouch;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * slouch://database_name/table_name/{id} 
 */
public class slouch extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext context) throws JSONException {
        if (action.equals("get")) {
            String path = args.getString(0);
            this.get(path, context);
            return true;
        } else if (action.equals("put")) {
            String path = args.getString(0);
            JSONObject obj = new JSONObject(args.getString(1));
            this.put(path, obj, context);
            return true;
        } else if (action.equals("post")) {
            String path = args.getString(0);
            JSONObject obj = new JSONObject(args.getString(1));
            this.post(path, obj, context);
            return true;
        } else if (action.equals("delete")) {
            String path = args.getString(0);
            this.delete(path, context);
            return true;
        } else if (action.equals("table")) {
            String path = args.getString(0);
            JSONObject obj = new JSONObject(args.getString(1));
            this.table(path, obj, context);
            return true;
        }
        return false;
    }

    private void get(String path, CallbackContext context) {
        context.success(path);
    }

    private void put (String path, JSONObject data, CallbackContext context) {
        context.success(data);
    }

    private void post (String path, JSONObject data, CallbackContext context) {
        context.success(data);
    }

    private void delete (String path, CallbackContext context) {
        context.success(path);
    }
    
    private void table (String path, JSONObject data, CallbackContext context) {
        context.success(data);
    }
}
