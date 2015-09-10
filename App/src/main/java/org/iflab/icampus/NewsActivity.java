package org.iflab.icampus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.viewpagerindicator.TabPageIndicator;

import java.util.HashMap;
import java.util.Map;

/**
 * 新闻模块
 */
public class NewsActivity extends ActionBarActivity {

    final static String[] newsTabs = {"学校要闻", "人才培养", "教学科研", "文化活动",
            "媒体关注", "校园人物"};//新闻类别
    private ViewPager newsPager;//每个类的新闻页
    private FragmentPagerAdapter adapter;//适配器
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
        pathMap = new HashMap<String, String>();
        pathMap.put(newsTabs[0], "/xw/zhxw");
        pathMap.put(newsTabs[1], "/xw/rcpy");
        pathMap.put(newsTabs[2], "/xw/jxky");
        pathMap.put(newsTabs[3], "/xw/whhd");
        pathMap.put(newsTabs[4], "/xw/mtgz");
        pathMap.put(newsTabs[5], "/xw/xyrw");
    }

    /**
     * 初始化Tabs和页面
     */
    private void initViewPager() {
        indicator = (TabPageIndicator) findViewById(R.id.indicator);//初始化pagerIndicator
        indicator.setViewPager(newsPager);
        newsPager = (ViewPager) findViewById(R.id.news_pager);//初始化pager
        adapter = new TabPageIndicatorAdapter(getSupportFragmentManager(), pathMap);//适配器
        newsPager.setAdapter(adapter);
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

    private class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        private Map<String, String> pathMap;

        public TabPageIndicatorAdapter(FragmentManager supportFragmentManager, Map<String, String> pathMap) {
            super(supportFragmentManager);
            this.pathMap=pathMap;
        }

        /**
         * @param position 指定的位置
         * @return 指定位置的fragment
         */
        @Override
        public Fragment getItem(int position) {

            return null;
        }

        /**
         * @return 要显示的View的数目
         */
        @Override
        public int getCount() {
            return pathMap.size();
        }
    }

    /**
     * ViewPagerIndicator的适配器
     */

//    private class TabPageIndicatorAdapter extends FragmentPagerAdapter {
//        private Map<String, String> pathMap;
//
//        public TabPageIndicatorAdapter(FragmentManager fragmentManager, Map<String, String> pathMap) {
//            super(fragmentManager);
//            this.pathMap = pathMap;
//        }
//
//        /**
//         *
//         * @param arg0
//         * @return
//         */
//        @Override
//        public Fragment getItem(int arg0) {
//            Fragment fragment = new NewsItem();
//            Bundle bundle = new Bundle();
//            bundle.putString("bundle", NewsActivity.newsTabs[arg0]);
//            bundle.putString("url", pathMap.get(NewsActivity.newsTabs[arg0]));
//            fragment.setArguments(bundle);
//            return fragment;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return NewsActivity.newsTabs[position];
//        }
//
//        @Override
//        public int getCount() {
//            return NewsActivity.newsTabs.length;
//        }
//    }
}
