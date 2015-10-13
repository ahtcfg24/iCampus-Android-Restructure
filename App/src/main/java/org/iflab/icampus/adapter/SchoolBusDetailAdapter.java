package org.iflab.icampus.adapter;

import android.widget.BaseAdapter;

/**
 * Created by HYL on 2015/10/13.
 */

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.iflab.icampus.R;

public class SchoolBusDetailAdapter extends BaseAdapter {
    private ArrayList<String> stations;
    private ArrayList<String> times;
    private Context context;

    public SchoolBusDetailAdapter() {
        super();
    }

    public SchoolBusDetailAdapter(ArrayList<String> stations, ArrayList<String> times,
                                  Context context) {
        this();
        this.stations = stations;
        this.times = times;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public Object getItem(int position) {
        return stations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item;
        if (convertView == null) {
            item = new Item();
            convertView = LayoutInflater.from(context).inflate(R.layout.school_bus_detail_item, null);
            item.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            item.stationTextView = (TextView) convertView.findViewById(R.id.stationTextView);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }

        String timeString = times.get(position);
        if (timeString != null) {
            item.timeTextView.setText(times.get(position));
        } else {
            item.timeTextView.setText("     ");
        }
        item.stationTextView.setText(stations.get(position));
        return convertView;
    }

    class Item {
        TextView timeTextView;
        TextView stationTextView;
    }
}
