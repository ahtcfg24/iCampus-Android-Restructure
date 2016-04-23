package org.iflab.icampus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.iflab.icampus.AMapActivity;
import org.iflab.icampus.NewsActivity;
import org.iflab.icampus.R;
import org.iflab.icampus.SchoolBusActivity;
import org.iflab.icampus.YellowPageActivity;
import org.iflab.icampus.adapter.MyGridAdapter;
import org.iflab.icampus.model.HomeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 绘制GridView
 */
public class HomeFragment extends Fragment {
    private GridView gridView;//主界面宫格
    private List<HomeItem> modules;//存放所有模块的列表
    private View rootView;//fragment的主界面

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
        modules.add(new HomeItem(R.drawable.map, "地图", AMapActivity.class));
        modules.add(new HomeItem(R.drawable.schoolbus, "校车", SchoolBusActivity.class));
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


}
