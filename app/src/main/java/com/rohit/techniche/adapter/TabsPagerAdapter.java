package com.rohit.techniche.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rohit.techniche.bean.CategoryItem;
import com.rohit.techniche.fragment.PageFragment;

import java.util.List;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    private List<CategoryItem> categoryItemList;

    public TabsPagerAdapter(FragmentManager fm, List<CategoryItem> categoryItemList) {
        super(fm);
        this.categoryItemList = categoryItemList;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        return PageFragment.newInstance(categoryItemList.get(position));
    }

    @Override
    public int getCount() {
        return categoryItemList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categoryItemList.get(position).getName();
    }
}