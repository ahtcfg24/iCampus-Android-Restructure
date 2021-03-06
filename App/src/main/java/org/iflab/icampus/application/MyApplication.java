package org.iflab.icampus.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.iflab.icampus.http.UrlStatic.ANDROIDUPGRADE;
import static org.iflab.icampus.http.UrlStatic.CAS;
import static org.iflab.icampus.http.UrlStatic.ICAMPUSAPI;
import static org.iflab.icampus.http.UrlStatic.JSONSOURCE;
import static org.iflab.icampus.http.UrlStatic.JWAPI;
import static org.iflab.icampus.http.UrlStatic.NEWSAPI;
import static org.iflab.icampus.http.UrlStatic.OAUTH2;


/**
 * Application类，程序一打开就会最先执行，程序结束才消亡的类
 *
 * @date 2015/8/30
 * @time 8:16
 */
public class MyApplication extends Application {
    private static Context context;
    private SharedPreferences sharedPreferences;

    /**
     * 获得最高生命周期的Context，方便别的类中使用
     *
     * @return Context
     */
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fresco.initialize(getApplicationContext());//初始化Fresco库，程序中会多处用到
       /*请求源数据不需要接参数，故设为null*/
        AsyncHttpIc.get(JSONSOURCE, null, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                /*先把sharedPreferences对象给实例化，防止空指针异常*/
                sharedPreferences = getApplicationContext()
                        .getSharedPreferences("JSONURL", MODE_PRIVATE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String SourceJsonData = new String(responseBody);
                saveURL(SourceJsonData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(getClass().getName(), "获取源数据失败");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                /* 如果本地URL没有存储下来，就使用默认的URL*/
                if (CAS == null) {
                    CAS = sharedPreferences.getString("CAS", "https://auth.bistu.edu.cn");
                    OAUTH2 = sharedPreferences.getString("OAUTH2", "https://222.249.250.89:8443");
                    ANDROIDUPGRADE = sharedPreferences.getString("ANDROIDUPGRADE", "http://m.bistu.edu.cn/upgrade/Android.php");
                    JWAPI = sharedPreferences.getString("JWAPI", "http://m.bistu.edu.cn/jiaowu");
                    ICAMPUSAPI = sharedPreferences.getString("ICAMPUSAPI", "http://m.bistu.edu.cn/api");
                    NEWSAPI = sharedPreferences.getString("NEWSAPI", "http://m.bistu.edu.cn/newsapi");
                }
            }
        });


    }

    /**
     * 把JsonURl存储到本地SharedPreferences
     *
     * @param SourceJsonData json
     */
    public void saveURL(String SourceJsonData) {
        try {
            JSONArray jsonArray = new JSONArray(SourceJsonData);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            CAS = jsonObject.getString("CAS");
            OAUTH2 = jsonObject.getString("oAuth2");
            ANDROIDUPGRADE = jsonObject.getString("AndroidUpgrade");
            JWAPI = jsonObject.getString("jwApi");
            ICAMPUSAPI = jsonObject.getString("icampusApi");
            NEWSAPI = jsonObject.getString("newsApi");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CAS", CAS);
            editor.putString("OAUTH2", OAUTH2);
            editor.putString("ANDROIDUPGRADE", ANDROIDUPGRADE);
            editor.putString("JWAPI", JWAPI);
            editor.putString("ICAMPUSAPI", ICAMPUSAPI);
            editor.putString("NEWSAPI", NEWSAPI);
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
