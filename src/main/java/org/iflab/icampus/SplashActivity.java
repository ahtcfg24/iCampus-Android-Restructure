package org.iflab.icampus;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去ActionBar
        setContentView(R.layout.activity_splash);

    }

}
