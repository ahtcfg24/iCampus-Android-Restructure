package org.iflab.icampus.oauth;

import android.content.Context;
import android.content.SharedPreferences;

import org.iflab.icampus.utils.StaticVariable;

/**
 * Created by hcjcch on 2015/2/13.
 */
public class AuthorizationCodeHandle {
    /**
     * 保存authorizationCode到SharedPreferences
     *
     * @param context           上下文
     * @param authorizationCode 要保存的authorizationCode
     */
    public static void saveAuthorizationCode(Context context, String authorizationCode) {
        SharedPreferences authorizationCodesharedPreferences = context.getSharedPreferences(StaticVariable.OauthStaticVariable.AUTHORIZATIONCODE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = authorizationCodesharedPreferences.edit();
        editor.putString(StaticVariable.OauthStaticVariable.AUTHORIZATIONCODE, authorizationCode);
        editor.apply();
    }

    /**
     * 从SharedPreferences获取authorizationCode
     *
     * @param context 上下文
     * @return authorizationCode
     */
    public static String getAuthorizationCode(Context context) {
        SharedPreferences authorizationCodesharedPreferences = context.getSharedPreferences(StaticVariable.OauthStaticVariable.AUTHORIZATIONCODE, Context.MODE_PRIVATE);
        return authorizationCodesharedPreferences.getString(StaticVariable.OauthStaticVariable.AUTHORIZATIONCODE, null);
    }

}