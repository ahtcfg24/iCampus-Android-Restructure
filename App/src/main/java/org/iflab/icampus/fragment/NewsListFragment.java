package org.iflab.icampus.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yalantis.phoenix.PullToRefreshView;

import org.apache.http.Header;
import org.iflab.icampus.R;
import org.iflab.icampus.adapter.NewsListAdapter;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.NewsItem;
import org.iflab.icampus.ui.MyToast;
import org.iflab.icampus.utils.ACache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 新闻列表
 */
public class NewsListFragment extends Fragment {
    private ACache aCache;
    private ListView newsListView;
    private PullToRefreshView pullToRefreshView;//下拉刷新控件
    private View loadMoreView;//上拉加载更多控件
    private View rootView;//Fragment的界面
    private String newsPath;//对应Fragment的相对路径
    private int currentPage;//分页加载的当前页编号
    private String newsListData;//新闻列表数据
    private String newsListData1;//不含返回码的新闻列表数据
    private String newsListURL;
    private NewsListAdapter newsListAdapter;
    private List<NewsItem> newsList;
    private TextView loadMoreTextView, loadToLastTextView;
    private LinearLayout footerProgressLayout, progressLayout;//两个progressBar


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        init();
        initView();
        loadData();
        return rootView;
    }

    /**
     * 初始化新闻列表
     */
    private void init() {
        Bundle bundle = getArguments();
        newsPath = bundle.getString("newsPath");
        currentPage = 1;
        newsListURL = UrlStatic.NEWSAPI + "/api.php?table=newslist&url=" + newsPath + "&index=" + currentPage;
        aCache = ACache.get(getActivity());
    }

    /**
     * 初始化布局控件
     */
    private void initView() {
        newsListView = (ListView) rootView.findViewById(R.id.newsListView);
        loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.load_more_item, null);
        loadMoreTextView = (TextView) loadMoreView.findViewById(R.id.load_more_textView);
        loadToLastTextView = (TextView) loadMoreView.findViewById(R.id.load_to_last_textView);
        footerProgressLayout = (LinearLayout) loadMoreView.findViewById(R.id.footer_progress_layout);
        progressLayout = (LinearLayout) rootView.findViewById(R.id.progress_layout);
        newsListView.addFooterView(loadMoreView);
        pullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
        pullToRefreshView.setOnRefreshListener(new RefreshListener());
        newsListView.setOnScrollListener(new ScrollListener());//下拉刷新
    }

    /**
     * 通过URL从网络获取数据
     *
     * @param URL 按页分的数据URL
     */
    private void getNewsListDataByURL(String URL) {
        AsyncHttpIc.get(URL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                newsListData = new String(responseBody);
                if (newsListData.contains("<HTML>")) {
                    new MyToast("你的WiFI还没有登录哦~");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://1.1.1.1/login.html")));
                } else {
                    switch (newsListData) {
                        case "-1":
                            footerProgressLayout.setVisibility(View.INVISIBLE);
                            loadToLastTextView.setVisibility(View.VISIBLE);
                            break;
                        case "0":
                            new MyToast("服务器出问题了，请稍后再试吧~");
                            break;
                        case "1":
                            new MyToast("解析不出数据啦！请重试！");
                            break;
                        default:
                            String newsListData1 = null;
                            try {
                                JSONObject jsonObject1 = new JSONObject(newsListData);
                                newsListData1 = jsonObject1.getString("d");
                                aCache.put(newsListURL, newsListData1, 1000);//存入缓存,过期时间1000秒
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            handleNewsListData(newsListData1);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                new MyToast("网络连接失败，请检查网络吧！");
            }

        });
    }

    /**
     * 处理分页后的新闻列表数据
     *
     * @param newsListData1 分页存放的数据
     */
    private void handleNewsListData(String newsListData1) {
        //不让新刷新的数据存到newsList的尾部，而是整个替代原来的newsList，然后在适配器里添加到原来newsList的尾部
        newsList = new ArrayList<>();
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
        } catch (NullPointerException e) {
            Log.i("handleNewsListData", "空指针异常");
        }

        progressLayout.setVisibility(View.INVISIBLE);

        if (currentPage == 1) {
            newsListAdapter = new NewsListAdapter(newsList, NewsListFragment.this.getActivity());
            newsListView.setAdapter(newsListAdapter);
            currentPage++;
        } else {
            newsListAdapter.addItem(newsList);
            newsListAdapter.notifyDataSetChanged();//更新列表视图
            currentPage++;
        }
        newsListURL = UrlStatic.NEWSAPI + "/api.php?table=newslist&url=" + newsPath + "&index=" + currentPage;
        footerProgressLayout.setVisibility(View.INVISIBLE);
        loadMoreTextView.setVisibility(View.VISIBLE);
    }


    /**
     * @param url
     * @return
     */
    private String filterUrl(String url) {
        int a = url.indexOf(".cn/");
        int b = url.indexOf(".xml");
        return url.substring(a + 4, b);
    }

    /**
     * 从网络或者缓存载入数据
     */
    private void loadData() {
        newsListData1 = aCache.getAsString(newsListURL);
        if (newsListData1 == null) {
            getNewsListDataByURL(newsListURL);
        } else {
            handleNewsListData(newsListData1);
        }
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
                loadToLastTextView.setVisibility(View.INVISIBLE);
                footerProgressLayout.setVisibility(View.VISIBLE);
                loadData();
                isLastRow = false;
            }
        }
    }

    /**
     * 下拉刷新监听器
     */
    private class RefreshListener implements PullToRefreshView.OnRefreshListener {
        @Override
        public void onRefresh() {
            pullToRefreshView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullToRefreshView.setRefreshing(false);
                    currentPage = 1;
                    newsListURL = UrlStatic.NEWSAPI + "/api.php?table=newslist&url=" + newsPath + "&index=" + currentPage;
                    getNewsListDataByURL(newsListURL);
                    new MyToast("刷新完成");
                }
            }, 1000);

        }
    }
}
