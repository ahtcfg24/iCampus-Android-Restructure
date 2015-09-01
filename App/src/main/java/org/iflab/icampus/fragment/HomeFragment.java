package org.iflab.icampus.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.iflab.icampus.NewsActivity;
import org.iflab.icampus.R;
import org.iflab.icampus.YellowPageActivity;
import org.iflab.icampus.model.HomeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 绘制GridView
 */
public class HomeFragment extends Fragment {
    private GridView gridView;//主界面宫格
    private List<HomeItem> modules;//存放所有模块的列表
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initGridView();
        return rootView;

    }

    public void initGridView() {
        gridView = (GridView) rootView.findViewById(R.id.gridView_home);
        modules = new ArrayList<>();
        modules.add(new HomeItem(R.drawable.news, "新闻", NewsActivity.class));
        modules.add(new HomeItem(R.drawable.yellowpage, "黄页", YellowPageActivity.class));
        //        modules.add(new HomeItem(R.drawable.map, "地图", MapActivity.class));
        //        modules.add(new HomeItem(R.drawable.schoolbus, "校车", SchoolBusActivity.class));
        //        modules.add(new HomeItem(R.drawable.wifi, "WiFi", WiFiActivity.class));
        //        modules.add(new HomeItem(R.drawable.job, "工作", JobActivity.class));
        //        modules.add(new HomeItem(R.drawable.group, "群组", GroupActivity.class));
        //        modules.add(new HomeItem(R.drawable.secondhand, "二手", SecondHandActivity.class));
        //        modules.add(new HomeItem(R.drawable.classroom, "教室", ClassRoomActivity.class));
        //        modules.add(new HomeItem(R.drawable.grade, "成绩", GradeActivity.class));

        gridView.setAdapter(new MyGridAdapter(modules, HomeFragment.this.getActivity()));

        gridView.setOnItemClickListener(new MyItemListener());


    }


    private class MyItemListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(new Intent(HomeFragment.this.getActivity(), modules.get(position).getItemModule()));
        }
    }

    /**
     * 自定义的用于绑定模块数据和GridView的Adapter
     */
    private class MyGridAdapter extends BaseAdapter {
        private List<HomeItem> items;//存放从GridView传过来的items
        private Context context;//代表GridView所在的Activity
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
         * 覆盖自AdapteView，此处不会自动调用
         *
         * @param position 正在点击的item在数据集合中的的位置
         * @return 该位置处的item对象
         */
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        /**
         * 点击时调用
         *
         * @param position 正在点击的item在数据集合中的的位置
         * @return 当前选项所在的位置的id
         */
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
            /**
             * 如果Recycle缓冲区里没有可用的View，那么就从资源加载
             */
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.home_item, null);
                convertView.setTag("使convertView不为空");
                //findViewById是用来在父控件中找子控件的
                imageView = (ImageView) convertView.findViewById(R.id.home_icon);
                textView = (TextView) convertView.findViewById(R.id.home_name);
            }
            imageView.setImageResource(items.get(position).getIconId());
            textView.setText(items.get(position).getItemName());
            return convertView;
        }
    }

}
