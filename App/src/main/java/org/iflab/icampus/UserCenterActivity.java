package org.iflab.icampus;


import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.iflab.icampus.model.User;
import org.iflab.icampus.oauth.GetUserInfo;

public class UserCenterActivity extends ActionBarActivity {
    private SimpleDraweeView avatarImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(UserCenterActivity.this);//初始化Fresco
        setContentView(R.layout.activity_user_center);
        setTitle("用户中心");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        avatarImageView = (SimpleDraweeView) findViewById(R.id.avatar_image_view);

        GetUserInfo.getUser(UserCenterActivity.this, new GetUserInfo.HandleUser() {
            @Override
            public void handleUser(User user) {
                Log.i("User","----->"+user);

                avatarImageView.setImageURI(Uri.parse(user.getAvatar()));
            }
        });
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
}