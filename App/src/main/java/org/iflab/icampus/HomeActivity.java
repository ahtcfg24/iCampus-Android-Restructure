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
import android.widget.Toast;

import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import org.iflab.icampus.fragment.HomeFragment;
import org.iflab.icampus.oauth.AuthorizationCodeHandle;
import org.iflab.icampus.oauth.GetAccessToken;
import org.iflab.icampus.oauth.RefreshToken;
import org.iflab.icampus.oauth.TokenHandle;
import org.iflab.icampus.utils.StaticVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 */
public class HomeActivity extends ActionBarActivity implements OnMenuItemClickListener {


    private FragmentManager fragmentManager;
    private DialogFragment menuDialogFragment;
    private long exitTime = 0;//记录按返回键的时间点

    private Intent intent;


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case StaticVariable.GET_AUTHORIZATION_CODE:
                if (resultCode == RESULT_OK) {
                    String authorizationCode = data.getStringExtra("result");
                    AuthorizationCodeHandle.saveAuthorizationCode(HomeActivity.this, authorizationCode);//保存authorizationCode到本地
                    GetAccessToken.getAccessToken(HomeActivity.this, authorizationCode);//根据authorizationCode获得AccessToken并保存到本地
                    Toast.makeText(HomeActivity.this, "登录成功，尽情体验吧0.0", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "登录失败囧", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    /**
     * 添加菜单选项对象
     *
     * @return
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
     * @param fragment
     * @param addToBackStack
     * @param containerId
     */
    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);
        if (! fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(containerId, fragment, backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    /**
     * 监听按键
     * @param keyCode 正在点击的按键代码
     * @param event 触发的事件
     * @return
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
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
                if (TokenHandle.getAccessToken(HomeActivity.this) == null) {
                    Toast.makeText(HomeActivity.this, "亲，你还木有登录哟0.0", Toast.LENGTH_LONG).show();
                    intent.setClass(HomeActivity.this, OAuthActivity.class);
                    startActivityForResult(intent, StaticVariable.GET_AUTHORIZATION_CODE);
                } else {
//                    RefreshToken.refreshToken(HomeActivity.this);//登录后每次进入都刷新Token，防止Token过期
                    intent.setClass(HomeActivity.this, UserCenterActivity.class);
                    startActivity(intent);
                }
                break;
            case 2:// TODO: 2015/8/25

                break;
            case 3:// TODO: 2015/8/25

                break;
        }

    }

}