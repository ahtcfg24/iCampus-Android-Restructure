package org.iflab.icampus.oauth;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by hcjcch on 2015/2/14.
 */
public class TokenHandle {
    /**
     * 保存access_Token到SharedPreferences
     *
     * @param context     上下文
     * @param accessToken 要保存的accessToken
     */
    public static void saveAccessToken(Context context, String accessToken) {
        SharedPreferences preferences = context.getSharedPreferences("accessToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("accessToken", accessToken);
        editor.apply();
    }

    /**
     * 从SharedPreferences获取access_Token
     *
     * @param context 上下文
     * @return accessToken
     */
    public static String getAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("accessToken", Context.MODE_PRIVATE);
        return preferences.getString("accessToken", null);
    }

    /**
     * 保存refresh_token到SharedPreferences
     *
     * @param context     上下文
     * @param refreshToken 要保存的refreshToken
     */
    public static void saveRefreshToken(Context context, String refreshToken) {
        SharedPreferences preferences = context.getSharedPreferences("accessToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("refreshToken", refreshToken);
        editor.apply();
    }

    /**
     * 从SharedPreferences获取refreshToken
     *
     * @param context 上下文
     * @return refreshToken
     */
    public static String getRefreshToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("accessToken", Context.MODE_PRIVATE);
        return preferences.getString("refreshToken", null);
    }


}