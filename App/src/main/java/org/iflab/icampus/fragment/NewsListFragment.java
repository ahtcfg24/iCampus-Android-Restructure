package org.iflab.icampus.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.R;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.NewsItem;
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
    private static final String KEY_CONTENT = "TestFragment:Content";
    private String mContent;
    private ListView newsListView;
    private View rootView;//Fragment的界面
    private String fragmentName;
    private String newsPath;//对应Fragment的相对路径
    private int currentPage;//（滑动scroll时）当前所在的屏幕范围编号
    private String newsListData;//新闻列表数据
    private String newsListURL;
    private ACache aCache;
    //    private List<String> urlList;
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
        if (newsListData == null) {
            /*如果缓存没有就从网络获取*/
            getNewsListDataByURL(newsListURL);
        } else {
            jsonNewsListData(newsListData);
            newsListView.setAdapter(new NewsListAdapter(newsList, NewsListFragment.this));
        }
        for (int i = 0; i < newsList.size(); i++) {
            System.out.println("-!!!!" + newsList.get(i));
        }
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
        aCache = ACache.get(NewsListFragment.this.getActivity());
        newsListData = aCache.getAsString("newsListData");


//        urlList=new ArrayList<>();

    }

    private void getNewsListDataByURL(String newsURL) {
        AsyncHttpIc.get(newsURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                newsListData = new String(responseBody);
                jsonNewsListData(newsListData);
                newsListView.setAdapter(new NewsListAdapter(newsList, NewsListFragment.this));
                aCache.put("newsListData", newsListData, ACache.TIME_HOUR);
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
            // TODO Auto-generated catch block
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
                newsItem.setTitle(jsonObject2.getString("n").replaceAll("\\|br\\|", ""));
                newsItem.setPreview(jsonObject2.getString("ab").replaceAll("\\|br\\|", ""));
                newsItem.setAuthor(jsonObject2.getString("au"));
                newsItem.setUpdateTime(jsonObject2.getString("rt"));
                //	if (jsonObject2.getString("ic").indexOf(".") != -1) {
                //newsListType.setIcon(add_120(jsonObject2.getString("ic")));
                newsItem.setIcon(jsonObject2.getString("ic"));
                //	}else {
                //		newsListType.setIcon(jsonObject2.getString("ic"));
                //}
                newsItem.setDetailUrl(filterUrl(jsonObject2.getString("url")));
                newsList.add(newsItem);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
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


    private class NewsListAdapter extends BaseAdapter {
        public NewsListAdapter(List<NewsItem> newsList, NewsListFragment newsListFragment) {
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
