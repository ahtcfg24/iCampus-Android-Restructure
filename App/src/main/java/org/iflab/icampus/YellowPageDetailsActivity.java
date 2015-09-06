package org.iflab.icampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.iflab.icampus.http.UrlStatic;

public class YellowPageDetailsActivity extends ActionBarActivity {
    private ListView yellowPageDetailsListView;
    private String yellowPageDetailsUrl;
    private String depart;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow_page_details);
        intent = getIntent();
        depart = intent.getStringExtra("depart");
        yellowPageDetailsListView = (ListView) findViewById(R.id.yellowPageDetails_ListView);
        yellowPageDetailsUrl = UrlStatic.ICAMPUSAPI + "/yellowpage.php?action=tel&catid=" + depart;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yellow_page_details, menu);
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
