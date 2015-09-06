package org.iflab.icampus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.YellowPageDepartDetails;
import org.iflab.icampus.ui.MyProgressDialog;
import org.iflab.icampus.ui.MyToast;
import org.iflab.icampus.utils.ACache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YellowPageDetailsActivity extends ActionBarActivity {
    private ListView yellowPageDetailsListView;
    private String yellowPageDetailsUrl;
    private String depart;
    private String name;
    private Intent intent;
    private ACache aCache;
    private String yellowPageDetailsData;
    private List<YellowPageDepartDetails> yellowPageDepartDetailsList;
    private MyProgressDialog myProgressDialog;
    private YellowPageDepartDetails yellowPageDepartDetails;
    private String branchName;
    private String telephoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow_page_details);
        init();//初始化
        if (yellowPageDetailsData == null) {
            getYellowPageDetailsDataByUrl(yellowPageDetailsUrl);
        } else {
            jsonYellowPageDetailsData(yellowPageDetailsData);
            yellowPageDetailsListView.setAdapter(new yellowPageDetailsAdapter(YellowPageDetailsActivity.this, yellowPageDepartDetailsList));
        }

    }

    private void init() {
        yellowPageDepartDetailsList = new ArrayList<>();
        intent = getIntent();
        depart = intent.getStringExtra("depart");
        name = intent.getStringExtra("name");
        getSupportActionBar().setTitle(name);
        yellowPageDetailsListView = (ListView) findViewById(R.id.yellowPageDetails_ListView);
        yellowPageDetailsUrl = UrlStatic.ICAMPUSAPI + "/yellowpage.php?action=tel&catid=" + depart;
        aCache = ACache.get(getApplicationContext());
        yellowPageDetailsData = aCache.getAsString(depart);
    }


    private void getYellowPageDetailsDataByUrl(String yellowPageDetailsUrl) {
        myProgressDialog = new MyProgressDialog(YellowPageDetailsActivity.this);
        AsyncHttpIc.get(yellowPageDetailsUrl, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                myProgressDialog.dismiss();
                yellowPageDetailsData = new String(responseBody);
                jsonYellowPageDetailsData(yellowPageDetailsData);
                yellowPageDetailsListView.setAdapter(new yellowPageDetailsAdapter(YellowPageDetailsActivity.this, yellowPageDepartDetailsList));
                aCache.put(depart, yellowPageDetailsData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                myProgressDialog.dismiss();
                new MyToast(getApplicationContext(), "获取部门号码失败，请重试");
            }
        });
    }

    private void jsonYellowPageDetailsData(String yellowPageDetailsData) {
        try {
            JSONArray jsonArray = new JSONArray(yellowPageDetailsData);
            for (int i = 0; i < jsonArray.length(); i++) {
                yellowPageDepartDetails = new YellowPageDepartDetails();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                branchName = jsonObject.getString("name");
                telephoneNumber = jsonObject.getString("telnum");
                yellowPageDepartDetails.setBranchName(branchName);
                yellowPageDepartDetails.setTelephoneNumber(telephoneNumber);
                yellowPageDepartDetailsList.add(yellowPageDepartDetails);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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


    private class yellowPageDetailsAdapter extends BaseAdapter {
        private Context context;
        private List<YellowPageDepartDetails> yellowPageDepartDetailsList;
        private ViewHolder viewHolder;

        public yellowPageDetailsAdapter(Context context, List<YellowPageDepartDetails> yellowPageDepartDetailsList) {
            this.context = context;
            this.yellowPageDepartDetailsList = yellowPageDepartDetailsList;
        }

        @Override
        public int getCount() {
            return yellowPageDepartDetailsList.size();
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
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.yellow_page_details_item, null);
                viewHolder = new ViewHolder();
                viewHolder.branchNameTextView = (TextView) convertView.findViewById(R.id.yellowPageDetails_item_textView1);
                viewHolder.telephoneNumberTextView = (TextView) convertView.findViewById(R.id.yellowPageDetails_item_textView2);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.telephoneNumberTextView.setText(yellowPageDepartDetailsList.get(position).getTelephoneNumber());
            viewHolder.branchNameTextView.setText(yellowPageDepartDetailsList.get(position).getBranchName());
            return convertView;
        }
    }

    private class ViewHolder {
        private TextView branchNameTextView;
        private TextView telephoneNumberTextView;
    }
}
