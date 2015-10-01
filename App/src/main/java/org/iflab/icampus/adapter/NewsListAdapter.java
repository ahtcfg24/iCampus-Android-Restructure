package org.iflab.icampus.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.iflab.icampus.R;
import org.iflab.icampus.model.NewsItem;

import java.util.List;


/**
 * 新闻listView的适配器
 */
public class NewsListAdapter extends BaseAdapter {
    private List<NewsItem> newsList;
    private Context context;
    private ViewHolder viewHolder;

    public NewsListAdapter(List<NewsItem> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, null);
            viewHolder = new ViewHolder();
            viewHolder.newsListTitle = (TextView) convertView.findViewById(R.id.newsList_title);
            viewHolder.newsListPreview = (TextView) convertView.findViewById(R.id.newsList_preview);
            viewHolder.newsListTime = (TextView) convertView.findViewById(R.id.newsList_time);
            viewHolder.newsListIcon = (SimpleDraweeView) convertView.findViewById(R.id.newsList_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.newsListTitle.setText(newsList.get(position).getTitle());
        viewHolder.newsListPreview.setText(newsList.get(position).getPreview());
        viewHolder.newsListTime.setText(newsList.get(position).getUpdateTime());
        viewHolder.newsListIcon.setImageURI(Uri.parse(newsList.get(position).getIcon()));
        System.out.println(newsList.get(position).getIcon() + "==-=");
        return convertView;
    }

    /**
     * 添加新加载的item到原新闻列表的尾部
     *
     * @param list 新加载出来的item的列表
     */
    public void addItem(List<NewsItem> list) {
        for (NewsItem newsItem : list) {
            newsList.add(newsItem);
        }
    }

    /**
     * 起优化作用ListView的ViewHolder类，避免多次加载TextView
     */
    private class ViewHolder {
        private SimpleDraweeView newsListIcon;
        private TextView newsListTitle, newsListPreview, newsListTime;
    }
}



