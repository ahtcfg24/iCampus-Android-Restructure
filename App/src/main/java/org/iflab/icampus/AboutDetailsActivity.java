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
import org.json.JSONException;
import org.json.JSONObject;

public class AboutDetailsActivity extends ActionBarActivity {
    private WebView webView;
    private String modName;
    private Intent intent;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_details);
        webView = (WebView) findViewById(R.id.webView);
        intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("title"));//获取传过来的标题并设为activity的标题
        modName = intent.getStringExtra("mod_name");//获取传过来的模块名称
        url = UrlStatic.ICAMPUSAPI + "/intro.php?mod=" + modName;//构造url
        AsyncHttpIc.get(url, null,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                String introData = new String(responseBody);
                                try {
                                    /*因为传入的introData并不符合json格式，因此要先做处理*/
                                    JSONObject jsonObject = new JSONObject(introData.substring(1, introData.length() - 1));
                                    String htmlData = jsonObject.getString("introCont");
                                    webView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                Log.i("--", "----->失败");

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
