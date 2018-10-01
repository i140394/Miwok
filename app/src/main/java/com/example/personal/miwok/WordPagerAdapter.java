package com.example.personal.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WordPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    WordPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new NumbersFragment();
            case 1: return new FamilyFragment();
            case 2: return new ColorsFragment();
            case 3: return new PhrasesFragment();
            default: return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return mContext.getString(R.string.number);
            case 1: return mContext.getString(R.string.family_members);
            case 2: return mContext.getString(R.string.colors);
            case 3: return mContext.getString(R.string.phrases);
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
