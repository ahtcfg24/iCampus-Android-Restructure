package org.iflab.icampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.iflab.icampus.R;
import org.iflab.icampus.model.YellowPageDepart;

import java.util.List;

/**
 * 绑定列表与黄页部门列表数据的适配器
 */
public class YellowPageAdapter extends BaseAdapter {

    private List<YellowPageDepart> yellowPageDepartList;
    private Context context;
    private ViewHolder viewHolder;

    public YellowPageAdapter(List<YellowPageDepart> yellowPageDepartList, Context context) {
        this.yellowPageDepartList = yellowPageDepartList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return yellowPageDepartList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 绘制每个item
     *
     * @param position    点击的位置
     * @param convertView item对应的View
     * @param parent      可选的父控件
     * @return 要显示的单个item的View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.yellow_page_item, null);
            viewHolder = new ViewHolder();
            viewHolder.yellowPageItemTextView = (TextView) convertView.findViewById(R.id.yellowPage_item_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.yellowPageItemTextView.setText(yellowPageDepartList.get(position).getName());
        return convertView;
    }

    /**
     * 起优化作用ListView的ViewHolder类，避免多次加载TextView
     */
    private class ViewHolder {
        private TextView yellowPageItemTextView;
    }
}


