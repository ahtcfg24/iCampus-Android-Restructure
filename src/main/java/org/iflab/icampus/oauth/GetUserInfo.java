package org.iflab.icampus.oauth;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.User;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hcjcch on 2015/3/7.
 */
public class GetUserInfo {
    public static void getUser(Context context, final HandleUser handleUser) {
        final User user = new User();
        AsyncHttpIc.get(UrlStatic.GET_PERSONAL_INFORMATION, createAccessTokenParams(TokenHandle.getAccessToken(context)), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println(new String(responseBody));
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    String userName = jsonObject.getString("userName");
                    String realName = jsonObject.getString("realName");
                    String userType = jsonObject.getString("userType");
                    String email = jsonObject.getString("email");
                    String avatar = jsonObject.getString("avatar");
                    String idCard = jsonObject.getString("idCard");
                    user.setUserName(userName);
                    user.setRealName(realName);
                    user.setUserType(userType);
                    user.setEmail(email);
                    user.setAvatar(avatar);
                    user.setIdCard(idCard);
                    handleUser.handleUser(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
            }
        });
    }

    private static RequestParams createAccessTokenParams(String accessToken) {
        RequestParams params = new RequestParams();
        params.put("access_token", accessToken);
        return params;
    }

    public interface HandleUser {
        public void handleUser(User user);
    }
}