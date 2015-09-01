package org.iflab.icampus;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;

public class AboutActivity extends ActionBarActivity {
    private ArrayList<String> aboutItemList;
    private HashMap<String, String> aboutModMap;
    private ListView aboutListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initAboutListView();
        setItemListener();
    }

    private void setItemListener() {
        aboutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "点击了" + aboutModMap.get(aboutItemList.get(position)), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化ListView
     */
    public void initAboutListView() {
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
        aboutListView = (ListView) findViewById(R.id.about_listView);
        aboutListView.setAdapter(new AboutListViewAdapter(aboutItemList, this));
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
