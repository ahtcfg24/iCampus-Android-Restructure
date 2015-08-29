package org.iflab.icampus;


import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.iflab.icampus.model.User;
import org.iflab.icampus.oauth.GetUserInfo;
import org.iflab.icampus.oauth.RefreshToken;
import org.iflab.icampus.oauth.TokenHandle;
import org.iflab.icampus.ui.ACache;

public class UserCenterActivity extends ActionBarActivity {
    private SimpleDraweeView avatarImageView;


    private Button button;
    private User user;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fresco.initialize(this);//初始化Fresco
        setContentView(R.layout.activity_user_center);
        setTitle("用户中心");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        user = (User) getIntent().getSerializableExtra("user");
        System.out.println(user);
        avatarImageView = (SimpleDraweeView) findViewById(R.id.avatar_image_view);
        avatarImageView.setImageURI(Uri.parse(user.getAvatar()));

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textview);
        textView.setText(user.toString() + TokenHandle.getAccessToken(this));

    }


    /**
     * 绘制菜单按钮，添加部件按钮到ActionBar上
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_center, menu);
        return true;
    }

    /**
     * 处理ActionBar上按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void re(View view) {
        RefreshToken.refreshToken(this);
        GetUserInfo.getUser(this);
        ACache aCache = ACache.get(this);
        user = (User) aCache.getAsObject("user");
        textView.setText(user.toString() + TokenHandle.getAccessToken(this));
    }
}
