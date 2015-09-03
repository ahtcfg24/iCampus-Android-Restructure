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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.utils.ACache;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AboutActivity extends ActionBarActivity {
    private ArrayList<String> aboutItemList;//存储关于列表各选项的名字
    private HashMap<String, String> aboutModMap;//存储每个mod的名字
    private ListView aboutListView;
    private Intent intent;
    private String modName;//mod的名字
    private String url;//mod对应的网络URL
    private String aboutDetailsData;//每个mod的详细内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = new Intent();
        initAboutItemList();
        aboutListView = (ListView) findViewById(R.id.about_listView);
        aboutListView.setAdapter(new AboutListViewAdapter(aboutItemList, this));
        aboutListView.setOnItemClickListener(new ItemListener());
    }

    /**
     * 初始化List
     */
    private void initAboutItemList() {
        aboutItemList = new ArrayList<>();
        aboutItemList.add("学校简介");
        aboutItemList.add("历史沿革");
        aboutItemList.add("院系专业");
        aboutItemList.add("Credits");
        aboutItemList.add("ifLab");
        aboutModMap = new HashMap<>();
        aboutModMap.put(aboutItemList.get(0), "intro");
        aboutModMap.put(aboutItemList.get(1), "history");
        aboutModMap.put(aboutItemList.get(2), "colleges");
        aboutModMap.put(aboutItemList.get(3), "credits");
        aboutModMap.put(aboutItemList.get(4), "iflab");

    }

    /**
     * 通过传入的URL获取网络上的每个mod的详情
     *
     * @param url 对应模块的URL
     */
    private void getAboutDetailsByUrl(String url) {
        AsyncHttpIc.get(url, null,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String introData = new String(responseBody);
                                try {
                                    /*因为传入的introData并不符合json格式，因此要先做处理*/
                                    JSONObject jsonObject = new JSONObject(introData.substring(1, introData.length() - 1));
                                    aboutDetailsData = jsonObject.getString("introCont");
                                            /*获取到之后存入缓存里*/
                                    ACache aCache = ACache.get(getApplicationContext());
                                    aCache.put(modName, aboutDetailsData);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(getApplicationContext(), "获取数据异常，请重试0.0", Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
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
    private class ItemListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            modName = aboutModMap.get(aboutItemList.get(position));
            ACache aCache = ACache.get(getApplicationContext());
            aboutDetailsData = aCache.getAsString(modName);
            /*如果缓存里没有这个mod的内容，就从网络获取*/
            if (aboutDetailsData == null) {
                url = UrlStatic.ICAMPUSAPI + "/intro.php?mod=" + modName;//构造获取mod详细内容的url
                getAboutDetailsByUrl(url);
            }
            intent.putExtra("aboutDetailsData", aboutDetailsData);//把所点击的模块的数据传入
            intent.putExtra("title", aboutItemList.get(position));//把所点击的模块的标题传入
            intent.setClass(AboutActivity.this, AboutDetailsActivity.class);
            startActivity(intent);
        }
    }

    private class AboutListViewAdapter extends BaseAdapter {
        private ArrayList<String> aboutItemList;
        private Context context;
        private TextView aboutItemTextView;


        public AboutListViewAdapter(ArrayList<String> aboutItemList, Context context) {
            this.aboutItemList = aboutItemList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return aboutItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 绘制每个item
         *
         * @param position    item的位置
         * @param convertView 即将出缓冲区再度进入内存被重新利用的View,这里屏幕未占满，始终为空
         * @param parent      item的父级容器
         * @return 要显示的每个Item的View
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                /*绑定控件资源*/
                convertView = LayoutInflater.from(context).inflate(R.layout.about_item, null);
                /*因为是要在convertView中找到TextView，因此不能省略前面的convertView，否则就是在Layout中找*/
                aboutItemTextView = (TextView) convertView.findViewById(R.id.about_item_textView);
            }
            /*填充列表文字*/
            aboutItemTextView.setText(aboutItemList.get(position));
            return convertView;
        }
    }
}
