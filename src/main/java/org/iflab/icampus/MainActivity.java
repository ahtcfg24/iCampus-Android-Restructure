package org.iflab.icampus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.model.User;
import org.iflab.icampus.oauth.GetUserInfo;
import org.iflab.icampus.oauth.TokenHandle;
import org.iflab.icampus.utils.StaticVariable;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    private static final int GET_AUTHO_RIZATIONCODE = 1;//OAuth认证的requestCode
    private Button login;
    private Button logout;
    private Button refreshToken;
    private Button getUser;
    private TextView textView;
    private String info = "";

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
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.button);
        logout = (Button) findViewById(R.id.button2);
        refreshToken = (Button) findViewById(R.id.button3);
        getUser = (Button) findViewById(R.id.button4);
        textView = (TextView) findViewById(R.id.textView);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OAuthActivity.class);
                startActivityForResult(intent, GET_AUTHO_RIZATIONCODE);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2015/8/23 logout

            }
        });
       /*刷新token*/
        refreshToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpIc.post("https://222.249.250.89:8443/oauth/token", createRefreshTokenParams(MainActivity.this), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(responseBody));
                            String access_token = jsonObject.getString("access_token");
                            String refresh_token = jsonObject.getString("refresh_token");
                            TokenHandle.saveAccessToken(MainActivity.this, access_token);
                            TokenHandle.saveRefreshToken(MainActivity.this, refresh_token);
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
        getUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUserInfo.getUser(MainActivity.this,new GetUserInfo.HandleUser() {
                    @Override
                    public void handleUser(User user) {
                        info = info + TokenHandle.getAccessToken(MainActivity.this) + "\n" + user;
                        textView.setText(info);
                    }
                });
            }
        });
    }

    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //        switch (requestCode) {
    //            case GET_AUTHO_RIZATIONCODE:
    //                if (resultCode == RESULT_OK) {
    //                    String authorizationCode = data.getStringExtra("result");
    //                    System.out.println("authorizationCode:   " + authorizationCode);
    //                    AuthorizationCodeHandle.saveAuthorizationCode(MainActivity.this, authorizationCode);//保存authorizationCode到本地
    //                    GetAccessToken.getAccessToken(MainActivity.this, authorizationCode);//根据authorizationCode获得AccessToken并保存到本地
    //                } else {
    //                    Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
    //                }
    //                break;
    //        }
    //    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


}