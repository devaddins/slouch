package com.devaddins.slouch;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Type;

import org.apache.cordova.CallbackContext;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class Request {

    private Gson gson = new Gson();

    public Request(String path, String data, CallbackContext context) throws JsonSyntaxException {
        this.context = context;
        this.path = path;
        this.setDatabase(path);
        this.setType(path);
        this.setId(path);
        this.setDataMap(data);
    }
    
    public Request(String path, CallbackContext context) throws JsonSyntaxException {
        this(path, "{}", context);
    }

    private String path;
    private CallbackContext context;
    private String database;
    private String type;
    private String id;
    private Map<String, Object> dataMap;

    public String getPath() {
        return this.path;
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
        return this.context;
    }

    public Map<String, Object> getDataMap() {
        return this.dataMap;
    }

    private void setDataMap(String data) throws JsonSyntaxException {
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        this.dataMap = gson.fromJson(data, mapType);
    }

    private void setDatabase(String path) {
        String db = null;
        Pattern p = Pattern.compile("^slouch://([\\w-]+)/");
        Matcher m = p.matcher(path);
        if (m.find() && m.groupCount() >= 1) {
            db = m.group(1);
        }
        this.database = db;
    }

    private void setType(String path) {
        String ty = null;
        Pattern p = Pattern.compile("^slouch://([\\w-]+)/([\\w-]+)");
        Matcher m = p.matcher(path);
        if (m.find() && m.groupCount() >= 2) {
            ty = m.group(2);
        }
        this.type = ty;
    }

    private void setId(String path) {
        String id = null;
        Pattern p = Pattern.compile("^slouch://([\\w-]+)/([\\w-]+)/([\\w-]+)");
        Matcher m = p.matcher(path);
        if (m.find() && m.groupCount() >= 3) {
            id = m.group(3);
        }
        this.id = id;
    }
}
