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
        this.setType(path);
        this.setId(path);
    }
    
    public Request(String path, CallbackContext context) throws JSONException {
        this(path, "{}", context);
    }

    private String path;
    private JSONObject data;
    private CallbackContext context;
    private String database;
    private String type;
    private String id;

    public String getPath() {
        return path;
    }
    
    public String getDatabase() {
        return this.database;
    }
    
    public String getType() {
        return this.type;
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

    private void setDatabase(String path) {
        String db = null;
        Pattern p = Pattern.compile("^slouch://(\\w+)/");
        Matcher m = p.matcher(path);
        if (m.find() && m.groupCount() >= 1) {
            db = m.group(1);
        }
        this.database = db;
    }

    private void setType(String path) {
        String ty = null;
        Pattern p = Pattern.compile("^slouch://(\\w+)/(\\w+)");
        Matcher m = p.matcher(path);
        if (m.find() && m.groupCount() >= 2) {
            ty = m.group(2);
        }
        this.type = ty;
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
}
