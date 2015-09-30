package org.iflab.icampus.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yalantis.phoenix.PullToRefreshView;

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


/**
 * 新闻列表
 */
public class NewsListFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    private String mContent;
    private ListView newsListView;
    private PullToRefreshView mPullToRefreshView;//下拉刷新控件
    private View loadMoreView;//上拉加载更多控件
    private View rootView;//Fragment的界面
    private String fragmentName;
    private String newsPath;//对应Fragment的相对路径
    private int currentPage;//分页加载的当前页编号
    private String newsListData;//新闻列表数据
    private String newsListURL;
    private NewsListAdapter newsListAdapter;
    private List<NewsItem> newsList;
    private TextView loadMoreTextView;
    private LinearLayout progressLayout;//footer布局

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
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
        loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.load_more_item, null);
        loadMoreTextView = (TextView) loadMoreView.findViewById(R.id.load_more_textView);
        progressLayout = (LinearLayout) loadMoreView.findViewById(R.id.progress_layout);
        newsListView.addFooterView(loadMoreView);

        Bundle bundle = getArguments();
        fragmentName = bundle.getString("fragmentName");
        newsPath = bundle.getString("newsPath");


        currentPage = 1;
        newsListURL = UrlStatic.NEWSAPI + "/api.php?table=newslist&url=" + newsPath + "&index=" + currentPage;
        //下拉刷新
        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        getNewsListDataByURL(newsListURL);
                    }
                }, 1000);
            }
        });

        newsListView.setOnScrollListener(new ScrollListener());

    }

    private void getNewsListDataByURL(String URL) {
        AsyncHttpIc.get(URL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                newsListData = new String(responseBody);
                if (newsListData.equals("-1")) {
                    new MyToast(getActivity(), "后面已经没有内容啦！");
                } else if (newsListData.equals("0")) {
                    new MyToast(getActivity(), "网络访问失败啦，请检查网络再试吧~");
                } else if(newsListData.equals("1")){
                    new MyToast(getActivity(),"服务器出问题了，解析不出数据啦！请重试！");
                }else {
                    jsonNewsListData(newsListData);
                    if (currentPage == 1) {
                        newsListAdapter = new NewsListAdapter(newsList,NewsListFragment.this.getActivity());
                        newsListView.setAdapter(newsListAdapter);
                        currentPage++;
                    } else {
                        newsListAdapter.addItem(newsList);
                        newsListAdapter.notifyDataSetChanged();
                        currentPage++;
                    }
                    newsListURL = UrlStatic.NEWSAPI + "/api.php?table=newslist&url=" + newsPath + "&index=" + currentPage;
                }
                progressLayout.setVisibility(View.INVISIBLE);
                loadMoreTextView.setVisibility(View.VISIBLE);
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
                viewHolder.newsListIcon = (SimpleDraweeView) convertView.findViewById(R.id.newsList_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.newsListTitle.setText(newsList.get(position).getTitle());
            viewHolder.newsListPreview.setText(newsList.get(position).getPreview());
            viewHolder.newsListTime.setText(newsList.get(position).getUpdateTime());
            viewHolder.newsListIcon.setImageURI(Uri.parse(newsList.get(position).getIcon()));
            return convertView;
        }

        /**
         * 添加新加载的item
         *
         * @param list 新加载出来的item的列表
         */
        public void addItem(List<NewsItem> list) {
            //临时存放传进来的新数据
            List<NewsItem> list1 =new ArrayList<>();
            for (NewsItem newsItem : list) {
                list1.add(newsItem);
            }
            newsList=list1;
        }
    }

    /**
     * 起优化作用ListView的ViewHolder类，避免多次加载TextView
     */
    private class ViewHolder {
        private SimpleDraweeView newsListIcon;
        private TextView newsListTitle, newsListPreview, newsListTime;
    }

    /**
     * listView的滑动监听器
     */
    private class ScrollListener implements AbsListView.OnScrollListener {
        boolean isLastRow = false;//是否滚动到最后一行

        /**
         * 滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
         *
         * @param view
         * @param firstVisibleItem 当前能看见的第一个列表项ID（从0开始）
         * @param visibleItemCount 当前能看见的列表项个数（小半个也算）
         * @param totalItemCount   列表项共数
         */
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //判断是否滚到最后一行
            if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                isLastRow = true;
            }
        }

        /**
         * 正在滚动时回调，回调2-3次，手指没抛则回调2次。scrollState = 2的这次不回调
         * 回调顺序如下
         * 第1次：scrollState = SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
         * 第2次：scrollState = SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
         * 第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动
         * 当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
         * 由于用户的操作，屏幕产生惯性滑动时为2
         *
         * @param view
         * @param scrollState 滚动状态
         */
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //当滚到最后一行且停止滚动时，执行加载
            if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                loadMoreTextView.setVisibility(View.INVISIBLE);
                progressLayout.setVisibility(View.VISIBLE);
                getNewsListDataByURL(newsListURL);
                isLastRow = false;
            }
        }
    }
}
