package org.iflab.icampus;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.oauth.TokenHandle;
import org.iflab.icampus.utils.StaticVariable;
import org.json.JSONException;
import org.json.JSONObject;


public class TestActivity extends ActionBarActivity {

    private Button refreshToken;

    public static RequestParams createRefreshTokenParams(Context context) {
        RequestParams params = new RequestParams();
        params.put(StaticVariable.OauthStaticVariable.KEY_CLIENT_ID, StaticVariable.OauthStaticVariable.CLIENT_ID);
        params.put(StaticVariable.OauthStaticVariable.KEY_CLIENT_SECRET, StaticVariable.OauthStaticVariable.CLIENT_SECRET);
        params.put(StaticVariable.OauthStaticVariable.KEY_REDIRECT_URI, StaticVariable.OauthStaticVariable.REDIRECT_URI);
        params.put(StaticVariable.OauthStaticVariable.KEY_GRANT_TYPE, "refresh_token");
        params.put("refresh_token", TokenHandle.getRefreshToken(context));
        return params;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        refreshToken = (Button) findViewById(R.id.button3);

       /*刷新token*/
        refreshToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpIc.post("https://222.249.250.89:8443/oauth/token", createRefreshTokenParams(TestActivity.this), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(responseBody));
                            String access_token = jsonObject.getString("access_token");
                            String refresh_token = jsonObject.getString("refresh_token");
                            TokenHandle.saveAccessToken(TestActivity.this, access_token);
                            TokenHandle.saveRefreshToken(TestActivity.this, refresh_token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                });
            }
        });
    }
}