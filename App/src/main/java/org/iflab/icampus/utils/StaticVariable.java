package org.iflab.icampus.utils;

/**
 * Created at 2015/2/12 13:00.
 */
public final class StaticVariable {
    public static final int GET_AUTHORIZATION_CODE = 1;//OAuth认证的requestCode
    public final static String[] NEWS_TABS = {"学校要闻", "人才培养", "教学科研", "文化活动",
            "媒体关注", "校园人物"};//新闻类别

    public static final class OauthStaticVariable {
        public static final String CLIENT_ID = "0b97ad49e755de0d210a8bab3d310046";
        public static final String CLIENT_SECRET = "6c4347eb6d23825f1586d8a67784d29b";
        public static final String REDIRECT_URI = "http://www.baidu.com/";
        public static final String RESPONSE_TYPE = "code";
        public static final String GRANT_TYPE = "authorization_code";
        public static final String KEY_CLIENT_ID = "client_id";
        public static final String KEY_CLIENT_SECRET = "client_secret";
        public static final String KEY_REDIRECT_URI = "redirect_uri";
        public static final String AUTHORIZATIONCODE = "authorizationCode";
        public static final String KEY_RESPONSE_TYPE = "response_type";
        public static final String KEY_GRANT_TYPE = "grant_type";


    }
}