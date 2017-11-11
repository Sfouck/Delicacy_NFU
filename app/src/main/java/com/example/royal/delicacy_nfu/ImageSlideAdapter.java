package com.example.royal.delicacy_nfu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.List;


/**
 * Created by user on 2017/11/7.
 */

public class ImageSlideAdapter extends FragmentStatePagerAdapter {
    private List<Integer> imgList;
    private int NUM_PAGES;

    public ImageSlideAdapter(FragmentManager fm, List<Integer> imgList) {
        super(fm);
        this.imgList = imgList;
        NUM_PAGES = this.imgList.size();
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("slideGetItem",imgList.get(position) +"");
        return ImageSlideFragment.init(imgList.get(position));
    }

}
