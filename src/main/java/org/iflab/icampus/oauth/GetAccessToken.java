package org.iflab.icampus.oauth;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.User;
import org.iflab.icampus.utils.StaticVariable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hcjcch on 2015/2/14.
 */
public class GetAccessToken {
    public static String getAccessToken(final Context context, String code) {
        AsyncHttpIc.post(UrlStatic.GET_ACCESS_TOKEN, createParams(code), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    System.out.println(new String(responseBody));
                    try {
                        JSONObject jsonObject = new JSONObject(new String(responseBody));
                        String access_token = jsonObject.getString("access_token");
                        String refresh_token = jsonObject.getString("refresh_token");
                        TokenHandle.saveAccessToken(context, access_token);
                        TokenHandle.saveRefreshToken(context, refresh_token);
                        GetUserInfo.getUser(context,new GetUserInfo.HandleUser() {
                            @Override
                            public void handleUser(User user) {
                                //do nothing
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //TODO 待处理
            }
        });
        return null;
    }

    private static RequestParams createParams(String code) {
        RequestParams params = new RequestParams();
        params.put(StaticVariable.OauthStaticVariable.KEY_CLIENT_ID, StaticVariable.OauthStaticVariable.CLIENT_ID);
        params.put(StaticVariable.OauthStaticVariable.KEY_CLIENT_SECRET, StaticVariable.OauthStaticVariable.CLIENT_SECRET);
        params.put(StaticVariable.OauthStaticVariable.KEY_REDIRECT_URI, StaticVariable.OauthStaticVariable.REDIRECT_URI);
        params.put(StaticVariable.OauthStaticVariable.KEY_GRANT_TYPE, StaticVariable.OauthStaticVariable.GRANT_TYPE);
        params.put("code", code);
        return params;
    }

}