package org.iflab.icampus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.News;
import org.iflab.icampus.model.NewsRes;
import org.iflab.icampus.ui.MyToast;
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
    private TextView newsTitleTextView, newsTimeTextView, newsContentTextView;
    private ConvenientBanner<String> newsBannerView;//新闻图片Banner
    private LinearLayout progressLayout;//加载进度控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        init();
        initView();
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
     * 初始化控件
     */
    private void initView() {
        newsTitleTextView = (TextView) findViewById(R.id.newsDetailTitle_textView);
        newsTimeTextView = (TextView) findViewById(R.id.newsDetailTime_textView);
        newsContentTextView = (TextView) findViewById(R.id.newsContent_textView);
        progressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        newsBannerView = (ConvenientBanner<String>) findViewById(R.id.newsDetail_bannerView);

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

                    if (!newsResData.contains("a")) {
                        newsBannerView.setVisibility(View.GONE);//如果新闻没有资源数据，就隐藏图片区域
                    } else {
                        jsonNewsResData(newsResData, newsResList);//解析新闻资源
                    }
                    news.setNewsResList(newsResList);//把新闻资源添加到新闻对象里
                    loadData(news);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                new MyToast("网络连接失败，请重试吧！");
            }
        });
    }

    /**
     * 填充新闻数据到控件里
     *
     * @param news 新闻对象
     */
    private void loadData(News news) {
        progressLayout.setVisibility(View.INVISIBLE);
        newsTitleTextView.setText(news.getDoctitle());
        newsTimeTextView.setText(news.getDocreltime());
        newsContentTextView.setText(news.getDochtmlcon());
        if (news.getNewsResList().size() > 0) {//如果资源线性表不为空就执行
            List<String> newsImageURLList = new ArrayList<>();
            for (NewsRes res : news.getNewsResList()) {
                newsImageURLList.add(res.getResLink());
            }
            //使用Android-ConvenientBanner库设置banner
            newsBannerView.setPages(new CBViewHolderCreator<ImageHolderView>() {
                @Override
                public ImageHolderView createHolder() {
                    return new ImageHolderView();
                }
            }, newsImageURLList)
                    //设置翻页的效果，不需要翻页效果可用不设
                    .setPageTransformer(ConvenientBanner.Transformer.DefaultTransformer);
        }
    }

    /**
     * 设置图片自动滚动
     */
    @Override
    protected void onResume() {
        super.onResume();
        newsBannerView.startTurning(2000);
    }

    /**
     * 停止滚动
     */
    @Override
    protected void onPause() {
        super.onPause();
        newsBannerView.stopTurning();
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
            if (!newsResArray.contains("[")) {
                JSONObject jsonObject0 = new JSONObject(newsResArray);
                String newsSubResData = jsonObject0.getString("attributes");
                jsonNewsResArray(newsSubResData, newsResList);
            } else {
                JSONArray jsonArray = new JSONArray(newsResArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                    String newsSubResData = jsonObject3.getString("attributes");
                    jsonNewsResArray(newsSubResData, newsResList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析新闻资源数组
     *
     * @param newsSubResData
     * @param newsResList
     */
    private void jsonNewsResArray(String newsSubResData, List<NewsRes> newsResList) {
        try {
            JSONObject jsonObject4 = new JSONObject(newsSubResData);
            NewsRes newsRes = new NewsRes();
            newsRes.setResName(jsonObject4.getString("n"));
            newsRes.setResType(jsonObject4.getString("t"));
            newsRes.setResLink(jsonObject4.getString("url"));
            newsResList.add(newsRes);
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

    /**
     * 通过URL加载网络上的新闻图片
     * 使用Android-ConvenientBanner库所需的类
     */
    private class ImageHolderView implements CBPageAdapter.Holder<String> {
        private SimpleDraweeView imageView;

        @Override
        public View createView(Context context) {
            //imageView可以通过layout文件来创建，也可以用代码创建，不一定是Image，任何控件都可以进行翻页
            imageView = new SimpleDraweeView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        /**
         * 结合Fresco加载图片
         *
         * @param context
         * @param position
         * @param data     图片从图片链接线性表中遍历出的一条图片地址
         */
        @Override
        public void UpdateUI(Context context, final int position, String data) {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(data))
                    .build();
            imageView.setController(controller);

        }
    }
}