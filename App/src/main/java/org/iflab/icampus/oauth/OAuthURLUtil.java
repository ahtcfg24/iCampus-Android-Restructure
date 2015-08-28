package org.iflab.icampus.oauth;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

public class OAuthURLUtil {
    /**
     * 创建URL
     *
     * @param baseUrl
     * @param addParams
     * @return
     * @throws Exception
     * @author hcjcch
     */
    //url生成器
    //例https://auth.bistu.edu.cn/cas/oauth/authorize?redirect_uri=http%3A%2F%2Fwww.baidu.com%2F&client_id=b2ab76c05627d30ef61992dbfc25af38
    public static String createUrl(String baseUrl, Map<String, String> addParams) throws Exception {
        String uri = null;
        if (addParams != null) {
            StringBuilder uriBuffer = new StringBuilder();
            uriBuffer.append(baseUrl);
            uriBuffer.append("?");
            Set<String> keySet = addParams.keySet();
            for (String key : keySet) {
                if (!uriBuffer.toString().equals(baseUrl + "?")) {
                    uriBuffer.append("&");
                }
                if (addParams.get(key) != null) {
                    uriBuffer.append(URLEncoder.encode(key, "utf-8"));
                    uriBuffer.append("=");
                    uriBuffer.append(URLEncoder.encode(addParams.get(key), "utf-8"));
                }
            }
            uri = uriBuffer.toString();
        }
        return uri;
    }

}