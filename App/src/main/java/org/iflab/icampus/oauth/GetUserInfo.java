package org.iflab.icampus.oauth;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.iflab.icampus.http.AsyncHttpIc;
import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.model.User;
import org.iflab.icampus.ui.MyToast;
import org.iflab.icampus.utils.ACache;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取用户信息，并缓存
 * Created by hcjcch on 2015/3/7.
 */
public class GetUserInfo {
    public static void getUser(final Context context) {
        final User user = new User();
        //先从文件中读取出刷新后的AccessToken,然后根据AccessToken来获取数据
        AsyncHttpIc.get(UrlStatic.GET_PERSONAL_INFORMATION,
                createAccessTokenParams(TokenHandle.getAccessToken(context)),
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        String userJsonData = new String(responseBody);
                        handleUserJson(user, userJsonData);
                        ACache aCache = ACache.get(context);
                        aCache.put("user", user, 31 * ACache.TIME_DAY);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        new MyToast(context, "获取用户信息失败，请重试");

                    }
                });
    }


    /**
     * 解析用户json信息
     *
     * @param user         用户对象
     * @param userJsonData json数据
     */
    public static void handleUserJson(User user, String userJsonData) {
        try {
            JSONObject jsonObject = new JSONObject(userJsonData);
            Log.i("----", "----》" + jsonObject);
            String department = jsonObject.getString("department");
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
            user.setDepartment(department);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static RequestParams createAccessTokenParams(String accessToken) {
        RequestParams params = new RequestParams();
        params.put("access_token", accessToken);
        return params;
    }
}