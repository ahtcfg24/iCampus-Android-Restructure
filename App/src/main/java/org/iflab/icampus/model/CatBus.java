package org.iflab.icampus.model;

/**
 * Created by HYL on 2015/10/12.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CatBus extends ArrayList<ShuttleBus> {

    public CatBus() {
        super();
    }

    public CatBus(JSONArray catBusJsonArray) {
        this();
        //json解析
        try {
            for (int j = 0; j < catBusJsonArray.length(); j++) {
                JSONObject shuttleBusJsonObject = catBusJsonArray.getJSONObject(j);
                ShuttleBus shuttleBus = new ShuttleBus(shuttleBusJsonObject);
                Log.d("testBus3", shuttleBus + "\n");
                this.add(shuttleBus);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
