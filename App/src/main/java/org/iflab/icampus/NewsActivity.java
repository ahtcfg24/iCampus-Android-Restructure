package org.iflab.icampus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.viewpagerindicator.TabPageIndicator;

import org.iflab.icampus.fragment.NewsListFragment;
import org.iflab.icampus.utils.StaticVariable;

import java.util.HashMap;
import java.util.Map;

/**
 * 新闻模块
 */
public class NewsActivity extends ActionBarActivity {


    private ViewPager newsPager;//每个类的新闻页
    private TabPageIndicator indicator;//Tabs
    private Map<String, String> pathMap;//存放不同新闻的相对路径
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置ActionBar左上角返回父级Activity，需要在清单中声明父级
        initMap();
        initViewPager();

    }


    /**
     * 初始化map
     */
    private void initMap() {
        pathMap = new HashMap<>();
        pathMap.put(StaticVariable.NEWS_TABS[0], "/xw/zhxw");
        pathMap.put(StaticVariable.NEWS_TABS[1], "/xw/rcpy");
        pathMap.put(StaticVariable.NEWS_TABS[2], "/xw/jxky");
        pathMap.put(StaticVariable.NEWS_TABS[3], "/xw/whhd");
        pathMap.put(StaticVariable.NEWS_TABS[4], "/xw/mtgz");
        pathMap.put(StaticVariable.NEWS_TABS[5], "/xw/xyrw");
    }

    /**
     * 初始化Tabs和页面
     * 必须先添加adapter之后才能添加pager
     */
    private void initViewPager() {
        newsPager = (ViewPager) findViewById(R.id.news_pager);//初始化pager
        newsPager.setAdapter(new TabPageIndicatorAdapter(getSupportFragmentManager(), pathMap));//添加adapter
        indicator = (TabPageIndicator) findViewById(R.id.indicator);//初始化pagerIndicator
        indicator.setViewPager(newsPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

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
    /**
     * ViewPager的适配器
     */
    private class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        private NewsListFragment newsListFragment;
        private Map<String, String> pathMap;

        public TabPageIndicatorAdapter(FragmentManager supportFragmentManager, Map<String, String> pathMap) {
            super(supportFragmentManager);
            this.pathMap=pathMap;
        }

        /**
         * 每次点击tab，会把该tab以及左右边的tab绘制出来
         * @param position 指定的位置
         * @return 指定位置的fragment
         */
        @Override
        public Fragment getItem(int position) {
            Log.i("newsactivity", "----->" + StaticVariable.NEWS_TABS[position % StaticVariable.NEWS_TABS.length]);
            return NewsListFragment.newInstance(StaticVariable.NEWS_TABS[position % StaticVariable.NEWS_TABS.length]);
        }

        /**
         *
         * @param position 指定位置
         * @return 指定页面的标题
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return StaticVariable.NEWS_TABS[position];
        }

        /**
         * @return 要显示的View的数目
         */
        @Override
        public int getCount() {
            return StaticVariable.NEWS_TABS.length;
        }
    }




}
