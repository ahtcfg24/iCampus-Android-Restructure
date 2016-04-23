package org.iflab.icampus.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HYL on 2015/10/12.
 */

public class ShuttleBus {
    private String catBusId;
    private String busName;
    private String departTime;
    private String returnTime;
    private String busIntro;
    private SchoolBusLine busLine;

    public ShuttleBus() {
        super();
    }

    public ShuttleBus(String catBusId, String busName, String departTime,
                      String returnTime, String busIntro, SchoolBusLine busLine) {
        this();
        this.catBusId = catBusId;
        this.busName = busName;
        this.departTime = departTime;
        this.returnTime = returnTime;
        this.busIntro = busIntro;
        this.busLine = busLine;
    }

    public ShuttleBus(JSONObject shuttleBusJsonObject) {
        this();
        //json解析
        try {
            this.catBusId = shuttleBusJsonObject.getString("id");
            this.busName = shuttleBusJsonObject.getString("busName");
            this.departTime = shuttleBusJsonObject.getString("departTime");
            this.returnTime = shuttleBusJsonObject.getString("returnTime");
            this.busIntro = shuttleBusJsonObject.getString("busIntro");
            //this.busIntro = busName;
            String busLineJsonString = shuttleBusJsonObject.getString("busLine");
            JSONArray busLinesJsonArray = new JSONArray(busLineJsonString);
            busLine = new SchoolBusLine(busLinesJsonArray);
            Log.d("testBus4", busLine + "\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCatBusId() {
        return catBusId;
    }

    public void setCatBusId(String catBusId) {
        this.catBusId = catBusId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getBusIntro() {
        return busIntro;
    }

    public void setBusIntro(String busIntro) {
        this.busIntro = busIntro;
    }

    public SchoolBusLine getBusLine() {
        return busLine;
    }

    public void setBusLine(SchoolBusLine busLine) {
        this.busLine = busLine;
    }

    @Override
    public String toString() {
        return "ShuttleBus [catBusId=" + catBusId + ", busName=" + busName
                + ", departTime=" + departTime + ", returnTime=" + returnTime
                + ", busIntro=" + busIntro + ", busLine=" + busLine + "]";
    }

}

