package org.iflab.icampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.News;
import org.iflab.icampus.model.NewsRes;
import org.iflab.icampus.utils.MyFilter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailActivity extends ActionBarActivity {
    private Intent intent;
    private String detailURL;//新闻详情的相对路径
    private String newsURL;//新闻详情的绝对路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        init();
        getNewsDataByURL();
    }

    /**
     * 初始化
     */
    private void init() {
        intent = getIntent();
        setTitle(intent.getStringExtra("fragmentName"));
        detailURL = intent.getStringExtra("detailURL");
        newsURL = UrlStatic.NEWSAPI + "/api.php?table=news&url=/" + detailURL;
    }

    /**
     * 从网络获取新网数据
     */
    private void getNewsDataByURL() {
        AsyncHttpIc.get(newsURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String newsData = new String(responseBody);
                News news = new News();
                try {
                    JSONObject jsonObject = new JSONObject(newsData);
                    String newsDetailData = jsonObject.getString("property");
                    jsonNewsDetailData(newsDetailData, news);//解析新闻详情
                    String newsResData = jsonObject.getString("as");
                    List<NewsRes> newsResList = new ArrayList<>();
                    jsonNewsResData(newsResData, newsResList);//解析新闻资源
                    news.setNewsResList(newsResList);//把新闻资源添加到新闻对象里
                    System.out.println("news=" + news);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 解析新闻详情
     *
     * @param newsDetailData 新闻详情
     * @param news           新闻对象
     */
    private void jsonNewsDetailData(String newsDetailData, News news) {
        try {
            JSONObject jsonObject1 = new JSONObject(newsDetailData);

            news.setDocid(jsonObject1.getString("docid"));
            news.setDocchannel(jsonObject1.getString("docchannel"));
            news.setDoctype(jsonObject1.getString("doctype"));
            news.setDoctitle(MyFilter.handleNewsTitle(jsonObject1.getString("doctitle")));
            news.setSubdoctitle(jsonObject1.getString("subdoctitle"));
            news.setDocauthor(jsonObject1.getString("docauthor"));
            news.setDocsourcename(jsonObject1.getString("docsourcename"));
            news.setDocreltime(jsonObject1.getString("docreltime"));
            news.setOpertime(jsonObject1.getString("opertime"));
            news.setCruser(jsonObject1.getString("cruser"));
            news.setCrtime(jsonObject1.getString("crtime"));
            news.setDocabstract(jsonObject1.getString("docabstract"));
            news.setDochtmlcon(MyFilter.replaceBr(jsonObject1.getString("dochtmlcon")));
            news.setPcurl(jsonObject1.getString("pcurl"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析新闻详情的资源
     *
     * @param newsResData 资源数据
     * @param newsResList 存放资源的线性表
     */
    private void jsonNewsResData(String newsResData, List<NewsRes> newsResList) {

        try {
            JSONObject jsonObject2 = new JSONObject(newsResData);

            String newsResArray = jsonObject2.getString("a");
            JSONArray jsonArray = new JSONArray(newsResArray);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                System.out.println("jsonArray[" + i + "]=" + jsonObject3);
                String newsSubResData = jsonObject3.getString("attributes");
                System.out.println("newsSubResData=" + newsSubResData);
                JSONObject jsonObject4 = new JSONObject(newsSubResData);
                NewsRes newsRes = new NewsRes();
                newsRes.setResName(jsonObject4.getString("n"));
                newsRes.setResType(jsonObject4.getString("t"));
                newsRes.setResLink(jsonObject4.getString("url"));
                newsResList.add(newsRes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
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
