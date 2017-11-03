package com.example.royal.delicacy_nfu;

import android.app.Fragment;

/**
 * Created by user on 2017/11/2.
 */

public class MainListFragment extends Fragment {
    static MainListFragment instance;

    public MainListFragment() {
    }

    public static MainListFragment getInstance() {
        if (instance == null) {
            instance = new MainListFragment();
        }
        return instance;
    }

    public static String search(String queue) {
        return queue;
    }
}
