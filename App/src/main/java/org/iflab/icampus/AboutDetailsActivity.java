package org.iflab.icampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class AboutDetailsActivity extends ActionBarActivity {
    private WebView webView;
    private Intent intent;
    private String aboutDetailsData;//每个mod的详情

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_details);
        webView = (WebView) findViewById(R.id.webView);
        intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("title"));//获取传过来的标题并设为activity的标题
        aboutDetailsData = intent.getStringExtra("aboutDetailsData");//获取传过来的模块数据
        /*使用loadData会乱码，原因未知*/
        webView.loadDataWithBaseURL(null, aboutDetailsData, "text/html", "utf-8", null);

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
