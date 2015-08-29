package org.iflab.icampus;


import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.iflab.icampus.model.User;

public class UserCenterActivity extends ActionBarActivity {
    private SimpleDraweeView avatarImageView;
    private User user;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fresco.initialize(this);//初始化Fresco
        setContentView(R.layout.activity_user_center);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        user = (User) getIntent().getSerializableExtra("user");//获得user对象

        initView();
        setContent();

    }

    /**
     * 初始化控件
     */
    public void initView() {
        avatarImageView = (SimpleDraweeView) findViewById(R.id.avatar_image_view);
        textView = (TextView) findViewById(R.id.real_name_text_view);
    }

    /**
     * 填充数据
     */
    public void setContent() {
        avatarImageView.setImageURI(Uri.parse(user.getAvatar()));
        textView.setText(user.getRealName());
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
