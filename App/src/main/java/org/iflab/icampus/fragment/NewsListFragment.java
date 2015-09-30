package org.iflab.icampus.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.R;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.NewsItem;
import org.iflab.icampus.ui.MyToast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import medusa.theone.waterdroplistview.view.WaterDropListView;

//import com.yalantis.phoenix.PullToRefreshView;


/**
 * 新闻列表
 */
public class NewsListFragment extends Fragment implements WaterDropListView.IWaterDropListViewListener{
    private static final String KEY_CONTENT = "TestFragment:Content";
    private String mContent;
    private ListView newsListView;
//    private PullToRefreshView mPullToRefreshView;//下拉刷新控件
    private View rootView;//Fragment的界面
    private String fragmentName;
    private String newsPath;//对应Fragment的相对路径
    private int currentPage;//分页加载的当前页编号
    private String newsListData;//新闻列表数据
    private String newsListURL;
    private List<NewsItem> newsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        init();
        getNewsListDataByURL(newsListURL);
        return rootView;
    }

    /**
     * 初始化新闻列表
     */
    private void init() {
        newsList = new ArrayList<>();
        newsListView = (ListView) rootView.findViewById(R.id.newsListView);
        Bundle bundle = getArguments();
        fragmentName = bundle.getString("fragmentName");
        newsPath = bundle.getString("newsPath");
        currentPage = 1;
        newsListURL = UrlStatic.NEWSAPI + "/api.php?table=newslist&url=" + newsPath + "&index=" + currentPage;// TODO:currentPage未定义，未实现一次加载三个路径 2015/9/29
        //下拉刷新
//        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
//        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPullToRefreshView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPullToRefreshView.setRefreshing(false);
//                        getNewsListDataByURL(newsListURL);
//                    }
//                }, 1000);
//            }
//        });


    }

    private void getNewsListDataByURL(String newsURL) {
        AsyncHttpIc.get(newsURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                newsListData = new String(responseBody);
                jsonNewsListData(newsListData);
                newsListView.setAdapter(new NewsListAdapter(newsList, NewsListFragment.this.getActivity()));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
// TODO: 2015/9/29
            }
        });
    }

    private void jsonNewsListData(String newsListData) {
        String newsListData1 = null;
        try {
            JSONObject jsonObject1 = new JSONObject(newsListData);
            newsListData1 = jsonObject1.getString("d");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        try {
            JSONArray jsonArray = new JSONArray(newsListData1);
            for (int i = 0; i < jsonArray.length(); i++) {
                NewsItem newsItem = new NewsItem();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String attributes = jsonObject.getString("attributes");
                JSONObject jsonObject2 = new JSONObject(attributes);
                newsItem.setPassageId(jsonObject2.getString("id"));
                newsItem.setTitle(jsonObject2.getString("n"));
                newsItem.setPreview(jsonObject2.getString("ab"));
                newsItem.setAuthor(jsonObject2.getString("au"));
                newsItem.setUpdateTime(jsonObject2.getString("rt"));
                newsItem.setIcon(jsonObject2.getString("ic"));
                newsItem.setDetailUrl(filterUrl(jsonObject2.getString("url")));
                newsList.add(newsItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String filterUrl(String url) {
        int a = url.indexOf(".cn/");
        int b = url.indexOf(".xml");
        return url.substring(a + 4, b);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    /**
     * 上拉刷新
     */
    @Override
    public void onRefresh() {
        getNewsListDataByURL(newsListURL);
    }

    /**
     * 下拉加载
     */
    @Override
    public void onLoadMore() {
        MyToast.makeText(getActivity(), "hah", Toast.LENGTH_SHORT);
    }

    /**
     * 新闻listView的适配器
     */
    private class NewsListAdapter extends BaseAdapter {
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
                viewHolder.newsListIcon = (ImageView) convertView.findViewById(R.id.newsList_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.newsListTitle.setText(newsList.get(position).getTitle());
            viewHolder.newsListPreview.setText(newsList.get(position).getPreview());
            viewHolder.newsListTime.setText(newsList.get(position).getUpdateTime());
//            viewHolder.newsListIcon.setImageBitmap(new Bitmap(newsList.get(position).getIcon()));
            return convertView;
        }
    }

    /**
     * 起优化作用ListView的ViewHolder类，避免多次加载TextView
     */
    private class ViewHolder {
        private ImageView newsListIcon;
        private TextView newsListTitle, newsListPreview, newsListTime;
    }


}
