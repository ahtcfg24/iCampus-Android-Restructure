package org.iflab.icampus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.YellowPageDepart;
import org.iflab.icampus.ui.MyProgressDialog;
import org.iflab.icampus.ui.MyToast;
import org.iflab.icampus.utils.ACache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YellowPageActivity extends ActionBarActivity {
    private ListView yellowPageListView;
    private MyProgressDialog myProgressDialog;
    private String YellowPageURl;
    private YellowPageDepart yellowPageDepart;
    private List<YellowPageDepart> departNameList;
    private String yellowPageData;
    private ACache aCache;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow_page);
        yellowPageListView = (ListView) findViewById(R.id.yellowPage_listView);
        departNameList = new ArrayList<>();
        YellowPageURl = UrlStatic.ICAMPUSAPI + "/yellowpage.php?action=cat";
        aCache = ACache.get(getApplicationContext());
        yellowPageData = aCache.getAsString("yellowPageData");
        if (yellowPageData == null) {
            /*如果缓存没有就从网络获取*/
            getYellowPageDataByUrl(YellowPageURl);
        } else {
            jsonYellowPageData(yellowPageData);
            yellowPageListView.setAdapter(new YellowPageAdapter(departNameList, YellowPageActivity.this));
        }
        yellowPageListView.setOnItemClickListener(new yellowPageListListener());
    }

    /**
     * 从网络获取黄页数据
     */
    public void getYellowPageDataByUrl(String yellowPageURl) {
        myProgressDialog = new MyProgressDialog(YellowPageActivity.this);
        AsyncHttpIc.get(yellowPageURl, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                myProgressDialog.dismiss();
                yellowPageData = new String(responseBody);
                jsonYellowPageData(yellowPageData);
                /*由于从网络获取是异步处理，所以需要在这里直接设置Adapter*/
                yellowPageListView.setAdapter(new YellowPageAdapter(departNameList, YellowPageActivity.this));
                aCache.put("yellowPageData", yellowPageData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                myProgressDialog.dismiss();
                new MyToast(getApplicationContext(), "获取黄页数据失败啦，请重试吧= =");
            }
        });
    }

    /**
     * 解析部门列表数据
     *
     * @param yellowPageData 转换成字符串后的json
     */
    private void jsonYellowPageData(String yellowPageData) {
        yellowPageDepart = new YellowPageDepart();
        try {
            JSONArray jsonArray = new JSONArray(yellowPageData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                yellowPageDepart.getName() = jsonObject.getString("name");
                yellowPageDepart.getDepart() = jsonObject.getString("depart");
                departNameList.add(yellowPageDepart);
            }
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
     * 绑定列表与黄页部门列表数据的适配器
     */
    private class YellowPageAdapter extends BaseAdapter {

        private List<YellowPageDepart> departNameList;
        private Context context;
        private ViewHolder viewHolder;

        public YellowPageAdapter(List<String> departNameList, Context context) {
            this.departNameList = departNameList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return departNameList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * 绘制每个item
         *
         * @param position    点击的位置
         * @param convertView item对应的View
         * @param parent      可选的父控件
         * @return 要显示的单个item的View
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.yellow_page_item, null);
                viewHolder = new ViewHolder();
                viewHolder.yellowPageItemTextView = (TextView) convertView.findViewById(R.id.yellowPage_item_textView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.yellowPageItemTextView.setText(departNameList.get(position));
            return convertView;
        }
    }

    /**
     * 起优化作用ListView的ViewHolder类
     */
    private class ViewHolder {
        private TextView yellowPageItemTextView;
    }

    private class yellowPageListListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            intent = new Intent();
            intent.putExtra("depart", );
            intent.setClass(YellowPageActivity.this, YellowPageDetailsActivity.class);
            startActivity(intent);
        }
    }
}
