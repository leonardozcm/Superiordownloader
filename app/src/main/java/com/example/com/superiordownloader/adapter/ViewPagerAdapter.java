
package com.example.com.superiordownloader.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by 59771 on 2017/10/1.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private String[] titleList;
    private List<Fragment>fragmentList;

    public ViewPagerAdapter(FragmentManager fm, String[] titleList, List<Fragment> fragmentList) {
        super(fm);
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }

    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
