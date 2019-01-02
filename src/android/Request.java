package com.devaddins.slouch;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.cordova.CallbackContext;
import org.json.JSONObject;
import org.json.JSONException;

public class Request {

    public Request(String path, String data, CallbackContext context) {
        this.context = context;
        this.path = path;
        this.data = data;
    }
    
    public Request(String path, CallbackContext context) {
        this.context = context;
        this.path = path;
        this.data = "{}";
    }

    private String path;

    private String data;

    private CallbackContext context;

    public String getPath() {
        return path;
    }
    
    public String getDatabase() {
        String db = null;
        Pattern p = Pattern.compile("^slouch://(\\w+)/");
        Matcher m = p.matcher(this.path);
        if (m.find() && m.groupCount() >= 1) {
            db = m.group(1);
        }
        return db;
    }

    public String getTable() {
        String tb = null;
        Pattern p = Pattern.compile("^slouch://(\\w+)/(\\w+)");
        Matcher m = p.matcher(this.path);
        if (m.find() && m.groupCount() >= 2) {
            tb = m.group(2);
        }
        return tb;
    }

    public String getId() {
        String id = null;
        Pattern p = Pattern.compile("^slouch://(\\w+)/(\\w+)/(\\d+)");
        Matcher m = p.matcher(this.path);
        if (m.find() && m.groupCount() >= 3) {
            id = m.group(3);
        }
        return id;
    }

    public CallbackContext getContext() {
        return context;
    }

    public JSONObject getData() throws JSONException {
        return new JSONObject(data);
    }
}