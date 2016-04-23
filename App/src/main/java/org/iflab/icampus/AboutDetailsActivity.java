package org.iflab.icampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.ui.MyProgressDialog;
import org.iflab.icampus.ui.MyToast;
import org.iflab.icampus.utils.ACache;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutDetailsActivity extends ActionBarActivity {
    private WebView webView;
    private Intent intent;
    private String modName;//mod的名字
    private String aboutDetailsUrl;//mod对应的网络URL
    private String aboutDetailsData;//每个mod的详细内容
    private MyProgressDialog myProgressDialog;
    private ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_details);
        webView = (WebView) findViewById(R.id.webView);
        intent = getIntent();
        modName = intent.getStringExtra("modName");//获取传过来的模块的名字
        getSupportActionBar().setTitle(intent.getStringExtra("title"));//获取传过来的标题并设为activity的标题

        aCache = ACache.get(getApplicationContext());
        aboutDetailsData = aCache.getAsString(modName);
            /*如果缓存里没有这个mod的内容，就从网络获取*/
        if (aboutDetailsData == null) {
            aboutDetailsUrl = UrlStatic.ICAMPUSAPI + "/intro.php?mod=" + modName;//构造获取mod详细内容的url
            getAboutDetailsByUrl(aboutDetailsUrl);
        } else {
            /*使用loadData会乱码，原因未知*/
            webView.loadDataWithBaseURL(null, aboutDetailsData, "text/html", "utf-8", null);
        }
    }

    /**
     * 通过传入的URL获取网络上的每个mod的详情
     *
     * @param url 对应模块的URL
     */
    private void getAboutDetailsByUrl(String url) {
        myProgressDialog = new MyProgressDialog(AboutDetailsActivity.this);
        AsyncHttpIc.get(url, null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String introData = new String(responseBody);
                        try {
                            myProgressDialog.dismiss();
                                    /*因为传入的introData是一个jsonArray包含一个jsonObject的格式，因此要先做处理使之成为jsonObject格式*/
                            JSONObject jsonObject = new JSONObject(introData.substring(1, introData.length() - 1));
                            aboutDetailsData = jsonObject.getString("introCont");
                                    /*使用loadData会乱码，原因未知*/
                            webView.loadDataWithBaseURL(null, aboutDetailsData, "text/html", "utf-8", null);
                                    /*获取到之后存入缓存里*/
                            aCache.put(modName, aboutDetailsData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        myProgressDialog.dismiss();
                        new MyToast("获取网络数据失败，请重试");
                        Log.i(modName, "----->" + "获取失败");
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_details, menu);
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
