package org.iflab.icampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.iflab.icampus.adapter.SchoolBusDetailAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SchoolBusDetailActivity extends ActionBarActivity {
    private Bundle bundle;
    private View header;
    private View footer;
    private TextView headerTime;
    private TextView footerTime;
    private TextView headerStation;
    private TextView footerStation;
    private List<Map<String, String>> busLine;
    private ArrayList<String> stations;
    private ArrayList<String> times;
    private ListView busDetailListView;
    private String busName;
    private TextView busName1;
    private TextView busName2;
    private TextView busName3;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();


        //将传过来的路线数据处理成两个数组
        stations = new ArrayList<>();
        times = new ArrayList<>();
        for (Map<String, String> stationMap : busLine) {
            Set<Map.Entry<String, String>> stationEntrySet;
            stationEntrySet = stationMap.entrySet();
            Iterator<Map.Entry<String, String>> stationIterator = stationEntrySet.iterator();
            Map.Entry<String, String> station = stationIterator.next();
            this.stations.add(station.getKey());
            this.times.add(station.getValue());
        }

        //设置listview头和尾
        String timeString = times.get(0);
        if (timeString.equals("")) {
            headerTime.setText("     ");
        } else {
            headerTime.setText(timeString);
        }
        headerStation.setText(stations.get(0));
        footerTime.setText(times.get(times.size() - 1));
        footerStation.setText(stations.get(stations.size() - 1));

        //将已设置的头和尾的数据从元数据中心删除
        times.remove(times.size() - 1);
        times.remove(0);
        stations.remove(stations.size() - 1);
        stations.remove(0);

        //设置标题
        getSupportActionBar().setTitle(title);

        //设置listview适配
        show();
    }

    private void init() {
                /*
        *
            bundle.putString("departTime", everyBus.getDepartTime());
            bundle.putString("returnime", everyBus.getReturnTime());
           */
        Intent intent = getIntent();
        bundle = intent.getExtras();
        title = bundle.getString("busType");
        busName = bundle.getString("busName");
        busLine = (ArrayList<Map<String, String>>) bundle.getSerializable("busLine");

        LayoutInflater inflater = LayoutInflater.from(this);
        header = inflater.inflate(R.layout.activity_school_bus_detail_header, null);
        footer = inflater.inflate(R.layout.activity_school_bus_detail_footer, null);
        headerTime = (TextView) header.findViewById(R.id.headerTimeTextView);
        headerStation = (TextView) header.findViewById(R.id.headerStationTextView);
        footerTime = (TextView) footer.findViewById(R.id.footerTimeTextView);
        footerStation = (TextView) footer.findViewById(R.id.footerStationTextView);
    }

    //显示界面
    private void show() {
        String[] stationNames = busName.split("-");
        TextView[] busNameTextViews = {busName1, busName2, busName3,};
        int[] busNameTextViewIds = {R.id.titleTextViewHead,
                R.id.titleTextViewBody, R.id.titleTextViewFoot,};
        int[] layouts = {R.layout.school_bus_detail_layout_one,
                R.layout.school_bus_detail_layout_two,
                R.layout.school_bus_detail_layout_three,};
        setContentView(layouts[stationNames.length - 1]);
        busDetailListView = (ListView) findViewById(R.id.busDetailListView);
        for (int i = 0; i < stationNames.length; i++) {
            busNameTextViews[i] = (TextView) findViewById(busNameTextViewIds[i]);
            busNameTextViews[i].setText(stationNames[i]);
        }

        SchoolBusDetailAdapter adapter = new SchoolBusDetailAdapter(stations, times, this);
        busDetailListView.addHeaderView(header, null, false);
        busDetailListView.addFooterView(footer, null, false);
        busDetailListView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_school_bus_detail, menu);
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
}

