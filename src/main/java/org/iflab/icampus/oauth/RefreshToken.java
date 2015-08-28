package org.iflab.icampus.oauth;


import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.utils.StaticVariable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @date 2015/8/27
 * @time 14:55
 */
public  class RefreshToken {
    /**
     * 创建刷新Token所需要的参数
     * @param context 读取本地Token所需要的参数
     * @return 参数
     */
    public static RequestParams createRefreshTokenParams(Context context) {
        RequestParams params = new RequestParams();
        params.put(StaticVariable.OauthStaticVariable.KEY_CLIENT_ID, StaticVariable.OauthStaticVariable.CLIENT_ID);
        params.put(StaticVariable.OauthStaticVariable.KEY_CLIENT_SECRET, StaticVariable.OauthStaticVariable.CLIENT_SECRET);
        params.put(StaticVariable.OauthStaticVariable.KEY_REDIRECT_URI, StaticVariable.OauthStaticVariable.REDIRECT_URI);
        params.put(StaticVariable.OauthStaticVariable.KEY_GRANT_TYPE, "refresh_token");
        params.put("refresh_token", TokenHandle.getRefreshToken(context));
        return params;
    }

    /**
     * 刷新AccessToken
     * @param context 保存到本地SharePreference需要的上下文环境
     */
    public static void refreshToken(final Context context) {
        AsyncHttpIc.post("https://222.249.250.89:8443/oauth/token", createRefreshTokenParams(context), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    String access_token = jsonObject.getString("access_token");
                    String refresh_token = jsonObject.getString("refresh_token");
                    TokenHandle.saveAccessToken(context, access_token);
                    TokenHandle.saveRefreshToken(context, refresh_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }
}
