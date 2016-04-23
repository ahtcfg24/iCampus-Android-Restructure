package org.iflab.icampus.model;

/**
 * Created by HYL on 2015/10/12.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SchoolBusLine extends ArrayList<Map<String, String>>{

    public SchoolBusLine() {
        super();
    }

    public SchoolBusLine(String station, String time) {
        this();
        addStation(station, time);
    }

    public SchoolBusLine(JSONArray busLinesJsonArray) {
        //json解析
        try {
            for (int i = 0; i < busLinesJsonArray.length(); i++) {
                JSONObject temp = busLinesJsonArray.getJSONObject(i);
                Iterator<String> iterator = temp.keys();
                String key = iterator.next();
                Map<String, String> map = new HashMap<>();
                map.put(key, temp.getString(key));
                this.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addStation(String station, String time) {
        Map<String, String> map = new HashMap<>();
        map.put(station, time);
        this.add(map);
    }

}
