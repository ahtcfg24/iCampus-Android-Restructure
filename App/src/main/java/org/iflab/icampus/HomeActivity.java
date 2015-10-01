package org.iflab.icampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import org.iflab.icampus.fragment.HomeFragment;
import org.iflab.icampus.model.User;
import org.iflab.icampus.oauth.AuthorizationCodeHandle;
import org.iflab.icampus.oauth.GetAccessToken;
import org.iflab.icampus.oauth.TokenHandle;
import org.iflab.icampus.ui.MyToast;
import org.iflab.icampus.utils.ACache;
import org.iflab.icampus.utils.StaticVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 */
public class HomeActivity extends ActionBarActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener {

    private FragmentManager fragmentManager;
    private DialogFragment menuDialogFragment;
    private long exitTime = 0;//记录按返回键的时间点

    private Intent intent;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager = getSupportFragmentManager();
        initToolbar();
        initMenuFragment();
        addFragment(new HomeFragment(), true, R.id.container);

        intent = new Intent();
    }

    /**
     * 只有在OAuthActivity结束之后才会执行该方法
     * 获得从OAuthActivity传来的授权码，并根据授权进一步获取数据
     *
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data        从OauthActivity传来的授权码
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case StaticVariable.GET_AUTHORIZATION_CODE:
                if (resultCode == RESULT_OK) {
                    String authorizationCode = data.getStringExtra("result");
                    AuthorizationCodeHandle.saveAuthorizationCode(HomeActivity.this, authorizationCode);//保存authorizationCode到本地
                    GetAccessToken.getAccessToken(HomeActivity.this, authorizationCode);//根据authorizationCode获得AccessToken及用户信息并保存到本地
                    new MyToast(getBaseContext(), "登录成功啦^_^");
                } else {
                    new MyToast(getBaseContext(), "登录失败囧");
                }
                break;
        }
    }

    /**
     * 初始化菜单Fragment
     */
    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    /**
     * 添加菜单选项对象
     * @return 包含选项的集合
     */
    private List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject cancelOption = new MenuObject();
        cancelOption.setResource(R.layout.selector_option_fold);

        MenuObject userOption = new MenuObject("用户中心");
        userOption.setResource(R.drawable.option_user);
        userOption.setMenuTextAppearanceStyle(R.style.MenuTextStyle);//设置选项样式

        MenuObject aboutOption = new MenuObject("关于我们");
        aboutOption.setResource(R.drawable.option_about);
        aboutOption.setMenuTextAppearanceStyle(R.style.MenuTextStyle);

        MenuObject updateOption = new MenuObject("检查更新");
        updateOption.setResource(R.drawable.option_update);
        updateOption.setMenuTextAppearanceStyle(R.style.MenuTextStyle);

        menuObjects.add(cancelOption);
        menuObjects.add(userOption);
        menuObjects.add(aboutOption);
        menuObjects.add(updateOption);
        return menuObjects;
    }

    /**
     * 初始化ToolBar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("iBistu");
        toolbar.setLogo(R.drawable.logo_bistu);//设置静态logo
        setSupportActionBar(toolbar);//把ToolBar设置为ActionBar
    }

    /**
     * 添加Fragment
     *
     * @param fragment fragment
     * @param addToBackStack addToBackStack
     * @param containerId containerId
     */
    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(containerId, fragment, backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    /**
     * 监听按键
     *
     * @param keyCode 正在点击的按键代码
     * @param event   触发的事件
     * @return onKeyDown
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*按菜单键时也能弹出菜单*/
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                menuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 加载菜单
     *
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    /**
     * 监听点击Toolbar上的按钮
     *
     * @param item 点击Toolbar上的按钮时传入
     * @return super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    menuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 当在主页按返回键时，双击退出，并确保fragment被dismiss
     */
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            new MyToast(getApplicationContext(), "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            if (menuDialogFragment != null && menuDialogFragment.isAdded()) {
                menuDialogFragment.dismiss();
            }
            finish();
        }
    }


    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {
            case 0://返回主界面
                break;
            case 1:
                ACache aCache = ACache.get(this);
                user = (User) aCache.getAsObject("user");
                if (TokenHandle.getAccessToken(HomeActivity.this) == null) {
                    new MyToast(getApplicationContext(), "亲，你还木有登录哟0.0");
                    intent.setClass(this, OAuthActivity.class);
                    startActivityForResult(intent, StaticVariable.GET_AUTHORIZATION_CODE);
                } else if (user == null) {
                    new MyToast(getApplicationContext(), "缓存失效了，请重新登录吧");
                    intent.setClass(this, OAuthActivity.class);
                    startActivityForResult(intent, StaticVariable.GET_AUTHORIZATION_CODE);
                } else {
                    intent.putExtra("user", user);
                    intent.setClass(this, UserCenterActivity.class);
                    startActivity(intent);
                }
                break;
            case 2:
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                break;
            case 3:
                new MyToast(getApplicationContext(), "正在检查更新。。。");
                break;
        }

    }

    @Override
    public void onMenuItemLongClick(View view, int i) {

    }
}
