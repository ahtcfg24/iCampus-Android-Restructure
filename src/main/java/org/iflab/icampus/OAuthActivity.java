package org.iflab.icampus;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.iflab.icampus.http.UrlStatic;
import org.iflab.icampus.oauth.OAuthURLUtil;
import org.iflab.icampus.utils.StaticVariable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hcjcch on 2015/2/12.
 */
public class OAuthActivity extends Activity {
    private WebView webViewOauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        initView();
        setWebView();
        oauth();
    }

    private void initView() {
        webViewOauth = (WebView) findViewById(R.id.webview_oauth);
    }

    /**
     * webView设置
     */
    private void setWebView() {
        webViewOauth.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println(url);
                if (url.contains("code")) {
                    String authorizationCode = url.substring(url.indexOf("=") + 1, url.length());
                    Intent intent = getIntent();
                    intent.putExtra("result", authorizationCode);
                    OAuthActivity.this.setResult(RESULT_OK, intent);
                    OAuthActivity.this.finish();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        webViewOauth.setWebChromeClient(new WebChromeClient() {
        });
    }

    /**
     * 加载授权网页
     */
    private void oauth() {
        webViewOauth.loadUrl(authorizeURL());
    }

    /**
     * 构造获取code的url
     * @return 返回获取code的url
     */
    private String authorizeURL() {
        Map<String, String> params = new HashMap<>();
        params.put(StaticVariable.OauthStaticVariable.KEY_CLIENT_ID, StaticVariable.OauthStaticVariable.CLIENT_ID);
        params.put(StaticVariable.OauthStaticVariable.KEY_REDIRECT_URI, StaticVariable.OauthStaticVariable.REDIRECT_URI);
        params.put(StaticVariable.OauthStaticVariable.KEY_RESPONSE_TYPE,StaticVariable.OauthStaticVariable.RESPONSE_TYPE);
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