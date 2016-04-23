package org.iflab.icampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.iflab.icampus.adapter.AboutListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class AboutActivity extends ActionBarActivity {
    private ArrayList<String> aboutItemList;//存储关于列表各选项的名字
    private HashMap<String, String> aboutModMap;//存储每个mod的名字
    private ListView aboutListView;
    private Intent intent;
    private String modName;//mod的名字

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
            intent.putExtra("modName", modName);//把模块的名字传入
            intent.putExtra("title", aboutItemList.get(position));//把所点击的模块的标题传入
            intent.setClass(AboutActivity.this, AboutDetailsActivity.class);
            startActivity(intent);
        }
    }


}
