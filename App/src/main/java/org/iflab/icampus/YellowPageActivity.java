package org.iflab.icampus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.adapter.YellowPageAdapter;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.YellowPageDepart;
import org.iflab.icampus.ui.MyToast;
import org.iflab.icampus.utils.ACache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YellowPageActivity extends ActionBarActivity {
    private ListView yellowPageListView;
    private LinearLayout progressLayout;
    private String YellowPageURl;
    private YellowPageDepart yellowPageDepart;
    private String depart;
    private String name;
    private List<YellowPageDepart> yellowPageDepartList;
    private String yellowPageData;
    private ACache aCache;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow_page);
        init();//初始化
        if (yellowPageData == null) {
            /*如果缓存没有就从网络获取*/
            getYellowPageDataByUrl(YellowPageURl);
        } else {
            jsonYellowPageData(yellowPageData);

        }
        yellowPageListView.setOnItemClickListener(new yellowPageListListener());
    }

    private void init() {
        progressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        yellowPageListView = (ListView) findViewById(R.id.yellowPage_listView);
        yellowPageDepartList = new ArrayList<>();
        aCache = ACache.get(getApplicationContext());
        YellowPageURl = UrlStatic.ICAMPUSAPI + "/yellowpage.php?action=cat";
        yellowPageData = aCache.getAsString("yellowPageData");

    }

    /**
     * 从网络获取黄页数据
     */
    public void getYellowPageDataByUrl(String yellowPageURl) {
        AsyncHttpIc.get(yellowPageURl, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                yellowPageData = new String(responseBody);
                if (yellowPageData.contains("<HTML>")) {
                    new MyToast("你的WiFI还没有登录哦~");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://1.1.1.1/login.html")));
                } else {
                    aCache.put("yellowPageData", yellowPageData);
                    jsonYellowPageData(yellowPageData);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                new MyToast("获取黄页数据失败啦，请重试吧= =");
            }
        });
    }

    /**
     * 解析部门列表数据
     *
     * @param yellowPageData 转换成字符串后的json
     */
    private void jsonYellowPageData(String yellowPageData) {
        try {
            JSONArray jsonArray = new JSONArray(yellowPageData);
            for (int i = 0; i < jsonArray.length(); i++) {
                yellowPageDepart = new YellowPageDepart();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                depart = jsonObject.getString("depart");
                name = jsonObject.getString("name");
                yellowPageDepart.setName(name);
                yellowPageDepart.setDepart(depart);
                yellowPageDepartList.add(yellowPageDepart);
            }
            progressLayout.setVisibility(View.INVISIBLE);//解除progressLayout
            yellowPageListView.setAdapter(new YellowPageAdapter(yellowPageDepartList, YellowPageActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yellow_page, menu);
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
     * 监听listview
     */
    private class yellowPageListListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            intent = new Intent();
            intent.putExtra("depart", yellowPageDepartList.get(position).getDepart());
            intent.putExtra("name", yellowPageDepartList.get(position).getName());
            intent.setClass(YellowPageActivity.this, YellowPageDetailsActivity.class);
            startActivity(intent);
        }
    }
}
