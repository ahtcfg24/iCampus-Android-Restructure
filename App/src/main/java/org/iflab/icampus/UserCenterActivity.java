package org.iflab.icampus;


import android.content.SharedPreferences;
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
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.iflab.icampus.model.User;
import org.iflab.icampus.ui.MyToast;

public class UserCenterActivity extends ActionBarActivity {
    private SimpleDraweeView avatarImageView;
    private User user;
    private TextView realNameTextView, userNameTextView, emailTextView, typeTextView, departmentTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        user = (User) getIntent().getSerializableExtra("user");//获得user对象

        initView();
        setContent();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("accessToken", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();//清除token
                finish();
                new MyToast("退出成功");
            }
        });
    }

    /**
     * 初始化控件
     */
    public void initView() {
        logoutButton = (Button) findViewById(R.id.logout_button);
        avatarImageView = (SimpleDraweeView) findViewById(R.id.avatar_image_view);
        realNameTextView = (TextView) findViewById(R.id.realName_textView);
        userNameTextView = (TextView) findViewById(R.id.userName_textView);
        emailTextView = (TextView) findViewById(R.id.email_textView);
        typeTextView = (TextView) findViewById(R.id.type_textView);
        departmentTextView = (TextView) findViewById(R.id.department_textView);

    }

    /**
     * 填充数据
     */
    public void setContent() {
        Uri uri = Uri.parse(user.getAvatar());
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();

        avatarImageView.setController(controller);
        realNameTextView.setText(user.getRealName());
        userNameTextView.setText(user.getUserName());
        emailTextView.setText(user.getEmail());
        typeTextView.setText(user.getUserType());
        departmentTextView.setText(user.getDepartment());
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
