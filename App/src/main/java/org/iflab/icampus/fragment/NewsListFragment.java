package org.iflab.icampus.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.R;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;

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
//    private List<String> urlList;
//    private List<NewsItem> newsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_news_list, container, false);
        initNewsList();
        return rootView;
    }

    /**
     * 初始化新闻列表
     */
    private void initNewsList() {
        newsListView=(ListView)rootView.findViewById(R.id.newsListView);
        Bundle bundle = getArguments();
        fragmentName=bundle.getString("fragmentName");
        newsPath =bundle.getString("newsPath");
        currentPage=1;
        String newsURL =UrlStatic.NEWSAPI+"/api.php?table=newslist&url="+newsPath+"&index="+currentPage;// TODO:currentPage未定义，未实现一次加载三个路径 2015/9/29
        AsyncHttpIc.get(newsURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("。。。。"+new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

//        urlList=new ArrayList<>();
//        newsList = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }


}
