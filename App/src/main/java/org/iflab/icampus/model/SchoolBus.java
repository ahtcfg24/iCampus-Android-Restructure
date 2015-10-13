package org.iflab.icampus.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HYL on 2015/10/12.
 */

public class SchoolBus {
    private String id;
    private String catName;
    private String catIntro;
    private CatBus catbus;

    public SchoolBus(String id, String catName, String catIntro, CatBus catbus) {
        super();
        this.id = id;
        this.catName = catName;
        this.catIntro = catIntro;
        this.catbus = catbus;
    }

    public SchoolBus(JSONObject schoolBusJsonObject) {
        //json解析
        try {
            id = schoolBusJsonObject.getString("id");
            catName = schoolBusJsonObject.getString("catName");
            catIntro = schoolBusJsonObject.getString("catIntro");
            String catBusJsonString = schoolBusJsonObject.getString("catBus");
            JSONArray catBusJsonArray = new JSONArray(catBusJsonString);
            catbus = new CatBus(catBusJsonArray);
            Log.d("testBus2", catbus + "\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public SchoolBus() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatIntro() {
        return catIntro;
    }

    public void setCatIntro(String catIntro) {
        this.catIntro = catIntro;
    }

    public CatBus getCatbus() {
        return catbus;
    }

    public void setCatbus(CatBus catbus) {
        this.catbus = catbus;
    }

    @Override
    public String toString() {
        return "SchoolBus [id=" + id + ", catName=" + catName + ", catIntro="
                + catIntro + ", catbus=" + catbus + "]";
    }

}
