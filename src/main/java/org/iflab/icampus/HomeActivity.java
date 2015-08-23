package org.iflab.icampus;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.iflab.icampus.model.HomeItem;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends ActionBarActivity {
    private GridView gridView;//主界面宫格
    private List<HomeItem> modules;//存放所有模块的列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initGridView();
    }

    public void initGridView() {
        gridView = (GridView) findViewById(R.id.gridView_home);
        modules = new ArrayList<>();
        modules.add(new HomeItem(R.drawable.news,"新闻",NewsActivity.class));
        modules.add(new HomeItem(R.drawable.yellowpage, "黄页", YellowPageActivity.class));
        modules.add(new HomeItem(R.drawable.map, "地图", MapActivity.class));
        modules.add(new HomeItem(R.drawable.schoolbus, "校车", SchoolBusActivity.class));
        modules.add(new HomeItem(R.drawable.wifi, "WiFi", WiFiActivity.class));
        modules.add(new HomeItem(R.drawable.job, "工作", JobActivity.class));
        modules.add(new HomeItem(R.drawable.group, "群组", GroupActivity.class));
        modules.add(new HomeItem(R.drawable.secondhand, "二手", SecondActivity.class));
        modules.add(new HomeItem(R.drawable.classroom, "教室", ClassRoomActivity.class));

        gridView.setAdapter(new MyGridAdapter(modules,HomeActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
     * 自定义的用于绑定模块数据和GridView的Adapter
     */
    private class MyGridAdapter extends BaseAdapter {
        private List<HomeItem> items;//存放从GridView传过来的items
        private Context context;//代表GridView所在的Activity
        private ImageView imageView;//一个item的图标
        private TextView textView;//一个item的文字

        public MyGridAdapter(List<HomeItem> items, Context context) {
            this.items = items;
            this.context = context;
        }

        /**
         * @return item的总数目
         */
        @Override
        public int getCount() {
            return items.size();
        }

        /**
         * @param position item在ArrayList中所处的位置
         * @return 该位置处的item对象
         */
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        /**
         * @param position item在ArrayList中所处的位置
         * @return position
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 把数据和控件联系起来的关键方法，传入的参数是数据中的，方法内容给这些参数赋予真正的控件
         *
         * @param position    item在ArrayList中所处的位置
         * @param convertView 要在每个item中显示出来的View，这里就是要把自定义的home_item当成一个convertView
         * @param parent      item的父级容器，在这里就是指GridView
         * @return 绑定了home_item控件之后的自定义的View
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.home_item, null);
            imageView=(ImageView)findViewById(R.id.home_icon);
            textView=(TextView)findViewById(R.id.home_name);
            imageView.setImageResource(items.get(position).getIconId());
            textView.setText(items.get(position).getItemName());
            return convertView;
        }
    }
}
