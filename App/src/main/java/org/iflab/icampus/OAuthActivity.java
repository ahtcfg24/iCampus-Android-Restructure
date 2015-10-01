package org.iflab.icampus;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.oauth.OAuthURLUtil;
import org.iflab.icampus.ui.MyToast;
import org.iflab.icampus.utils.StaticVariable;

import java.util.HashMap;
import java.util.Map;

/**
 * 加载登录界面的网页
 * Created by hcjcch on 2015/2/12.
 */
public class OAuthActivity extends Activity {
    private WebView oAuthWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        oAuthWebView = (WebView) findViewById(R.id.oAuth_webView);
        setWebView();
        oAuthWebView.loadUrl(authorizeURL());//加载授权网页

    }

    /**
     * webView设置
     */
    private void setWebView() {
        oAuthWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println(url);
                if (url.contains("code")) {
                    String authorizationCode = url.substring(url.indexOf("=") + 1, url.length());
                    Intent intent = getIntent();
                    /*登录完成之后会获得一个授权码传递到onActivityResult()*/
                    intent.putExtra("result", authorizationCode);
                    OAuthActivity.this.setResult(RESULT_OK, intent);
                    OAuthActivity.this.finish();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                new MyToast("加载登录页面失败，请重试");
            }


            @Override
            public void onReceivedSslError(WebView view, @NonNull SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        oAuthWebView.setWebChromeClient(new WebChromeClient() {
        });
    }


    /**
     * 构造获取code的url
     *
     * @return 返回获取code的url
     */
    private String authorizeURL() {
        Map<String, String> params = new HashMap<>();
        params.put(StaticVariable.OauthStaticVariable.KEY_CLIENT_ID, StaticVariable.OauthStaticVariable.CLIENT_ID);
        params.put(StaticVariable.OauthStaticVariable.KEY_REDIRECT_URI, StaticVariable.OauthStaticVariable.REDIRECT_URI);
        params.put(StaticVariable.OauthStaticVariable.KEY_RESPONSE_TYPE, StaticVariable.OauthStaticVariable.RESPONSE_TYPE);
        String requestUrl = null;
        try {
            requestUrl = OAuthURLUtil.createUrl(UrlStatic.GET_CODE, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(requestUrl);
        return requestUrl;
    }
}