package org.iflab.icampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.adapter.yellowPageDetailsAdapter;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.YellowPageDepartBranch;
import org.iflab.icampus.ui.MyProgressDialog;
import org.iflab.icampus.ui.MyToast;
import org.iflab.icampus.ui.YellowPageDialog;
import org.iflab.icampus.utils.ACache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YellowPageDetailsActivity extends ActionBarActivity {
    private ListView yellowPageDetailsListView;
    private String yellowPageDetailsUrl;
    private String depart;//部门网址的代号，用于构造URL
    private String name;//部门的名称，用于设置ActionBar的标题
    private Intent intent;
    private ACache aCache;
    private String yellowPageDepartDetailsData;//每个部门下面的所有的分支的json数组数据
    private List<YellowPageDepartBranch> yellowPageDepartBranchList;
    private MyProgressDialog myProgressDialog;
    private YellowPageDepartBranch yellowPageDepartBranch;
    private String branchName;//部门下分支的名字
    private String telephoneNumber;//部门下分支的号码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow_page_details);
        init();//初始化
        if (yellowPageDepartDetailsData == null) {
            /*如果本地缓存没有该部门下的数据，就从网络获取*/
            getYellowPageDetailsDataByUrl(yellowPageDetailsUrl);
        } else {
            jsonYellowPageDetailsData(yellowPageDepartDetailsData);
            yellowPageDetailsListView.setAdapter(new yellowPageDetailsAdapter(YellowPageDetailsActivity.this, yellowPageDepartBranchList));
        }
        yellowPageDetailsListView.setOnItemClickListener(new BranchListListener());

    }

    private void init() {
        yellowPageDepartBranchList = new ArrayList<>();
        intent = getIntent();
        depart = intent.getStringExtra("depart");
        name = intent.getStringExtra("name");
        getSupportActionBar().setTitle(name);
        yellowPageDetailsListView = (ListView) findViewById(R.id.yellowPageDetails_ListView);
        yellowPageDetailsUrl = UrlStatic.ICAMPUSAPI + "/yellowpage.php?action=tel&catid=" + depart;
        aCache = ACache.get(getApplicationContext());
        yellowPageDepartDetailsData = aCache.getAsString(depart);//从缓存获取该分支的数据
    }

    private void getYellowPageDetailsDataByUrl(String yellowPageDetailsUrl) {
        myProgressDialog = new MyProgressDialog(YellowPageDetailsActivity.this);
        AsyncHttpIc.get(yellowPageDetailsUrl, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                myProgressDialog.dismiss();
                yellowPageDepartDetailsData = new String(responseBody);
                jsonYellowPageDetailsData(yellowPageDepartDetailsData);
                /*由于从网络获取是异步处理，所以需要在这里直接设置Adapter*/
                yellowPageDetailsListView.setAdapter(new yellowPageDetailsAdapter(YellowPageDetailsActivity.this, yellowPageDepartBranchList));
                aCache.put(depart, yellowPageDepartDetailsData);//存放到缓存
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                myProgressDialog.dismiss();
                new MyToast(getApplicationContext(), "获取部门号码失败，请重试");
            }
        });
    }

    /**
     * 解析部门下的详情数据
     *
     * @param yellowPageDetailsData 包含该部门的所有分支的json数据
     */
    private void jsonYellowPageDetailsData(String yellowPageDetailsData) {
        try {
            JSONArray jsonArray = new JSONArray(yellowPageDetailsData);
            for (int i = 0; i < jsonArray.length(); i++) {
                yellowPageDepartBranch = new YellowPageDepartBranch();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                branchName = jsonObject.getString("name");
                telephoneNumber = jsonObject.getString("telnum");
                yellowPageDepartBranch.setBranchName(branchName);
                yellowPageDepartBranch.setTelephoneNumber(telephoneNumber);
                yellowPageDepartBranchList.add(yellowPageDepartBranch);
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

    /**
     * 监听listView号码
     */
    private class BranchListListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            branchName = yellowPageDepartBranchList.get(position).getBranchName();
            new YellowPageDialog(YellowPageDetailsActivity.this
                    , branchName
                    , telephoneNumber
                    , yellowPageDepartBranchList
                    , position);
        }
    }


}
