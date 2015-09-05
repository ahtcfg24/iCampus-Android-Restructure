package org.iflab.icampus;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.ui.MyProgressDialog;
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
    private String departName;
    private List<String> departNameList;
    private String yellowPageData;
    private ACache aCache;

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
            myProgressDialog = new MyProgressDialog(YellowPageActivity.this);
            getYellowPageDataByUrl(YellowPageURl);
        } else {
            jsonYellowPageData(yellowPageData);
            yellowPageListView.setAdapter(new YellowPageAdapter(departNameList, YellowPageActivity.this));
        }
    }


    public void getYellowPageDataByUrl(String yellowPageURl) {
        AsyncHttpIc.get(YellowPageURl, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                myProgressDialog.dismiss();
                yellowPageData = new String(responseBody);
                Log.i("YellowPageActivity", "----->" + yellowPageData);
                jsonYellowPageData(yellowPageData);
                yellowPageListView.setAdapter(new YellowPageAdapter(departNameList, YellowPageActivity.this));
                aCache.put("yellowPageData", yellowPageData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void jsonYellowPageData(String yellowPageData) {
        try {
            JSONArray jsonArray = new JSONArray(yellowPageData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                departName = jsonObject.getString("name");
                Log.i("YellowPageActivity", "----->" + departName + "\n");
                departNameList.add(departName);
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

    private class YellowPageAdapter extends BaseAdapter {


        private List<String> departNameList;
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

            return null;
        }
    }

    private class ViewHolder {
        private TextView yellowPageItemTextView;
    }
}
