package org.iflab.icampus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.adapter.BusListAdapter;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.SchoolBus;
import org.iflab.icampus.model.ShuttleBus;
import org.iflab.icampus.ui.MyToast;
import org.iflab.icampus.utils.ACache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SchoolBusActivity extends ActionBarActivity {
    private ListView busListView;
    private ACache aCache;
    private LinearLayout progressLayout;
    private String busListURL;
    private String busListJSON;
    private List<ShuttleBus> commuterBuses;//通勤车
    private List<ShuttleBus> teachBuses;//教学车
    private ArrayList<Object> busListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_bus);
        //new MyToast("正在加载,请稍后~");
        init();
        if (busListJSON == null) {
            /*如果缓存没有就从网络获取*/
            getBusListDataByUrl(busListURL);
        } else {
            jsonBusListData(busListJSON);
        }
        busListView.setOnItemClickListener(new ItemClick());
    }

    /**
     * 初始化
     */
    private void init() {
        progressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        busListView = (ListView) findViewById(R.id.busList);
        aCache = ACache.get(getApplicationContext());
        busListURL = UrlStatic.ICAMPUSAPI + "/bus.php";
        busListJSON = aCache.getAsString("busListJSON");
    }

    /**
     * 从网络获取校车数据
     */
    public void getBusListDataByUrl(String busListURL) {

        AsyncHttpIc.get(busListURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                busListJSON = new String(responseBody);
                if (busListJSON.contains("<HTML>")) {
                    new MyToast("你的WiFI还没有登录哦~");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://1.1.1.1/login.html")));
                } else {
                    aCache.put("busListJSON", busListJSON);
                    jsonBusListData(busListJSON);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                new MyToast("获取校车数据失败啦，请重试吧= =");
            }
        });
    }


    /**
     * 解析校车列表数据
     *
     * @param busListJson 转换成字符串后的json
     */
    private void jsonBusListData(String busListJson) {
        List<SchoolBus> schoolBusList = new ArrayList<>();
        try {
            JSONArray busListJsonArray = new JSONArray(busListJson);
            for (int i = 0; i < busListJsonArray.length(); i++) {
                JSONObject schoolBusJsonObject = busListJsonArray.getJSONObject(i);
                SchoolBus schoolBus = new SchoolBus(schoolBusJsonObject);
                schoolBusList.add(schoolBus);
                Log.d("testBus1", schoolBus + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        commuterBuses = schoolBusList.get(0).getCatbus();
        teachBuses = schoolBusList.get(1).getCatbus();
        busListData = new ArrayList<>();
        busListData.add("通勤班车");
        for (ShuttleBus everyBus : commuterBuses) {
            busListData.add(everyBus);
            Log.d("testBus5", everyBus + "\n");
        }

        busListData.add("教学班车");
        for (ShuttleBus everyBus : teachBuses) {
            busListData.add(everyBus);
            Log.d("testBus6", everyBus + "\n");
        }

        BusListAdapter adapter = new BusListAdapter(SchoolBusActivity.this, busListData);
        busListView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 监听校车列表
     */
    class ItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            ShuttleBus everyBus = (ShuttleBus) busListData.get(position);
            int busDivide = busListData.indexOf("教学班车");
            if (position < busDivide) {
                bundle.putString("busType", "通勤班车");
            } else {
                bundle.putString("busType", "教学班车");
            }
            bundle.putString("busName", everyBus.getBusName());
            bundle.putString("departTime", everyBus.getDepartTime());
            bundle.putString("returnime", everyBus.getReturnTime());
            bundle.putSerializable("busLine", everyBus.getBusLine());
            intent.putExtras(bundle);
            //跳转
            intent.setClass(SchoolBusActivity.this, SchoolBusDetailActivity.class);
            startActivity(intent);
        }
    }

}
