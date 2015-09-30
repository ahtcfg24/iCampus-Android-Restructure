package org.iflab.icampus.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.iflab.icampus.fragment.NewsListFragment;
import org.iflab.icampus.utils.StaticVariable;

import java.util.Map;

/**
 * ViewPager的适配器
 */
public class TabPageIndicatorAdapter extends FragmentPagerAdapter {
    private NewsListFragment newsListFragment;
    private String fragmentName;//每个Fragment对应的Tab名称
    private Map<String, String> pathMap;

    public TabPageIndicatorAdapter(FragmentManager supportFragmentManager, Map<String, String> pathMap) {
        super(supportFragmentManager);
        this.pathMap=pathMap;
    }

    /**
     * 每次点击tab，会把该tab以及左右边的tab绘制出来,同时向fragment传递数据
     * @param position 指定的位置
     * @return 指定位置的fragment
     */
    @Override
    public Fragment getItem(int position) {
        fragmentName= StaticVariable.NEWS_TABS[position % StaticVariable.NEWS_TABS.length];
        newsListFragment = new NewsListFragment();
        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putString("fragmentName", fragmentName);
        fragmentBundle.putString("newsPath", pathMap.get(fragmentName));
        newsListFragment.setArguments(fragmentBundle);
        return newsListFragment;
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