package com.devaddins.slouch;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.cordova.CallbackContext;
import org.json.JSONObject;
import org.json.JSONException;

public class Request {

    public Request(String path, String data, CallbackContext context) throws JSONException {
        this.context = context;
        this.path = path;
        this.data = new JSONObject(data);
        this.setDatabase(path);
        this.setTable(path);
        this.setId(path);
    }
    
    public Request(String path, CallbackContext context) {
        this.context = context;
        this.path = path;
        this.setDatabase(path);
        this.setTable(path);
        this.setId(path);
    }

    private String path;
    private JSONObject data;
    private CallbackContext context;
    private String database;
    private String table;
    private String id;

    public String getPath() {
        return path;
    }

    
    private void setDatabase(String path) {
        String db = null;
        Pattern p = Pattern.compile("^slouch://(\\w+)/");
        Matcher m = p.matcher(path);
        if (m.find() && m.groupCount() >= 1) {
            db = m.group(1);
        }
        this.database = db;
    }

    public String getDatabase() {
        return this.database;
    }

    private void setTable(String path) {
        String tb = null;
        Pattern p = Pattern.compile("^slouch://(\\w+)/(\\w+)");
        Matcher m = p.matcher(path);
        if (m.find() && m.groupCount() >= 2) {
            tb = m.group(2);
        }
        this.table = tb;
    }

    public String getTable() {
        return this.table;
    }

    private void setId(String path) {
        String id = null;
        Pattern p = Pattern.compile("^slouch://(\\w+)/(\\w+)/(\\d+)");
        Matcher m = p.matcher(path);
        if (m.find() && m.groupCount() >= 3) {
            id = m.group(3);
        }
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public CallbackContext getContext() {
        return context;
    }

    public JSONObject getData() {
        return data;
    }
}