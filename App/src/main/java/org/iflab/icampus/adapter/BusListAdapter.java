package org.iflab.icampus.adapter;

/**
 * Created by HYL on 2015/10/12.
 */

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.iflab.icampus.R;
import org.iflab.icampus.model.ShuttleBus;

public class BusListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Object> bus;
    private static final int TITLE = 0;//定义条目类型
    private static final int BUS_LINE = 1;

    public BusListAdapter() {
        super();
    }

    public BusListAdapter(Context context, ArrayList<Object> bus) {
        this();
        this.context = context;
        this.bus = bus;
    }

    @Override
    public int getCount() {
        return bus.size();
    }

    @Override
    public Object getItem(int position) {
        return bus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (bus.get(position) instanceof ShuttleBus) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return bus.get(position) instanceof ShuttleBus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == TITLE) {
            TitleItem titleItem = new TitleItem();
            convertView = LayoutInflater.from(context).inflate(R.layout.school_bus_title_item, null);
            titleItem.titleTextView = (TextView) convertView.findViewById(R.id.titleTextViewHead);
            titleItem.titleTextView.setText((String) bus.get(position));
        } else {
            BusItem busItem;
            if (convertView == null) {
                busItem = new BusItem();
                convertView = LayoutInflater.from(context).inflate(R.layout.school_bus_item, null);
                busItem.busNameTextView = (TextView) convertView.findViewById(R.id.busNameTextView);
                busItem.busIntroTextView = (TextView) convertView.findViewById(R.id.busIntroTextView);
                busItem.goTimeTextView = (TextView) convertView.findViewById(R.id.goTimeTextView);
                busItem.backTimeTextView = (TextView) convertView.findViewById(R.id.backTimeTextView);
                convertView.setTag(busItem);
            } else {
                busItem = (BusItem) convertView.getTag();
            }
            ShuttleBus shuttleBus = (ShuttleBus) bus.get(position);
            busItem.busNameTextView.setText(shuttleBus.getBusName());
            busItem.busIntroTextView.setText(shuttleBus.getBusName());
            String departTime = shuttleBus.getDepartTime();
            String returnTime = shuttleBus.getReturnTime();
            busItem.goTimeTextView.setText(departTime.substring(0, departTime.lastIndexOf(":")));
            if (!returnTime.equals("null")) {
                busItem.backTimeTextView.setText(returnTime.substring(0, returnTime.lastIndexOf(":")));
            } else {
                busItem.backTimeTextView.setText("  -");
            }
        }
        return convertView;
    }

    class TitleItem {
        TextView titleTextView;
    }

    class BusItem {
        TextView busNameTextView;
        TextView busIntroTextView;
        TextView goTimeTextView;
        TextView backTimeTextView;
    }
}
