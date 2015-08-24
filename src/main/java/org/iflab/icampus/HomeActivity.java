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
        modules.add(new HomeItem(R.drawable.news, "新闻", NewsActivity.class));
        modules.add(new HomeItem(R.drawable.yellowpage, "黄页", YellowPageActivity.class));
        modules.add(new HomeItem(R.drawable.map, "地图", MapActivity.class));
        modules.add(new HomeItem(R.drawable.schoolbus, "校车", SchoolBusActivity.class));
        modules.add(new HomeItem(R.drawable.wifi, "WiFi", WiFiActivity.class));
        modules.add(new HomeItem(R.drawable.job, "工作", JobActivity.class));
        modules.add(new HomeItem(R.drawable.group, "群组", GroupActivity.class));
        modules.add(new HomeItem(R.drawable.secondhand, "二手", SecondActivity.class));
        modules.add(new HomeItem(R.drawable.classroom, "教室", ClassRoomActivity.class));
        modules.add(new HomeItem(R.drawable.grade, "成绩", GradeActivity.class));

        gridView.setAdapter(new MyGridAdapter(modules, HomeActivity.this));
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
        private ViewHolder viewHolder;
        private TextView textView;
        private ImageView imageView;

        public MyGridAdapter(List<HomeItem> items, Context context) {
            this.items = items;
            this.context = context;
        }

        /**
         * @return 要绘制的View数目，不能大于实际存在资源总数
         */
        @Override
        public int getCount() {
            return items.size();
        }

        /**
         * @param position 正在点击的item在数据集合中的的位置
         * @return 该位置处的item对象
         */
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        /**
         * @param position 正在点击的item在数据集合中的的位置
         * @return position
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 通过传入的position，把需要绘制的View数据加工成我们想要的View，最后返回给父级容器使用
         * 把数据和控件联系起来的关键方法，形成一个缓冲模型，屏幕上能显示的View都在内存里，
         * 而把不能显示的放进缓冲区队列Recycle里，缓冲区占用总大小是一个屏幕能显示的所有View数量+2再整体
         * 乘单个View所占的内存空间
         *
         * @param position    即将脱离屏幕边缘的位置，如果屏幕向上滑动就是最上端的View所在位置
         * @param convertView 即将出缓冲区再度进入内存被重新利用的View，在屏幕未被占满时，该参数为空,
         *                    此时可以为其新建一个View对象，也可以从XML布局文件中inflate出来一个View对象，
         *                    当屏幕被占满，有View进入缓冲区时，这时该参数就是一个已经存在过但已经脱离屏幕范围
         *                    时的View对象
         * @param parent      item的父级容器，在这里就是指GridView
         * @return 绑定了home_item控件之后的自定义的View
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**
             * 如果Recycle缓冲区里没有可用的View，那么就从资源加载
             */
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.home_item, null);
                imageView = (ImageView) convertView.findViewById(R.id.home_icon);//findViewById是用来找子控件的
                textView = (TextView) convertView.findViewById(R.id.home_name);
            }
            Log.i("哈哈",""+ position);
            imageView.setImageResource(items.get(position).getIconId());
            textView.setText(items.get(position).getItemName());
            return convertView;
        }
    }

    class ViewHolder extends View {
        private TextView myTextView;
        private ImageView myImageView;

        public ViewHolder(TextView myTextView, ImageView myImageView, Context context) {
            super(context);
            this.myTextView = myTextView;
            this.myImageView = myImageView;
        }

        public TextView getMyTextView() {
            return myTextView;
        }

        public void setMyTextView(TextView myTextView) {
            this.myTextView = myTextView;
        }

        public ImageView getMyImageView() {
            return myImageView;
        }

        public void setMyImageView(ImageView myImageView) {
            this.myImageView = myImageView;
        }
    }
}
