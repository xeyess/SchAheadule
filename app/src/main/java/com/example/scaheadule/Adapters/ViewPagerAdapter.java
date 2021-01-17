package com.example.scaheadule.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentName = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return fragmentName.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return fragmentName.get(position);
    }

    public void AddFragment(Fragment fragment, String title)
    {
        fragmentList.add((fragment));
        fragmentName.add(title);
    }
}