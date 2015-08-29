package org.iflab.icampus.oauth;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.utils.StaticVariable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获得AccessToken并根据该根据AccessToken获取用户信息
 * Created by hcjcch on 2015/2/14.
 */
public class GetAccessToken {
    public static String getAccessToken(final Context context, String code) {
        AsyncHttpIc.post(UrlStatic.GET_ACCESS_TOKEN, createParams(code), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(responseBody));
                        String access_token = jsonObject.getString("access_token");
                        String refresh_token = jsonObject.getString("refresh_token");
                        TokenHandle.saveAccessToken(context, access_token);//保存AccessToken到本地
                        TokenHandle.saveRefreshToken(context, refresh_token);
                        GetUserInfo.getUser(context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "获取AccessToken失败", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    /**
     * 创建获得AccessToken所需要的参数
     *
     * @param context 读取本地AccessToken所需的参数
     * @return 参数
     */
    public static RequestParams createAccessTokenParams(Context context) {
        RequestParams params = new RequestParams();
        params.put("access_token", TokenHandle.getAccessToken(context));
        return params;
    }

    /**
     * 创建从服务器获取首次获取AccessToken和RefreshToken的参数
     *
     * @param code 服务器返回的OAuth认证码
     * @return 参数
     */
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