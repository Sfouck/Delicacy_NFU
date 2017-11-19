package com.example.royal.delicacy_nfu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MenuItem mSearchItem;
    private MenuItem mGatchaItem;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private RecyclerView mRecyclerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private MainViewAdapter mMainViewAdapter;
    private SearchView mSearchView;
    private HashMap<String, String> mealList;
    private ShopDataAdapter mDbHelper;
    private ShopDataUtil mShopDataUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //init
        initialize();

        //Toolbar
        setSupportActionBar(mToolbar);

        //Main View
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerList.setLayoutManager(layoutManager);
        mRecyclerList.setItemAnimator(new DefaultItemAnimator());

        //Drawer
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //Navigation
        mNavigationView.setNavigationItemSelectedListener(this);

        //Intent
        handleIntent(getIntent());
    }

    private void initialize() {
        mShopDataUtil = new ShopDataUtil(getApplicationContext());
        mealList = mShopDataUtil.getMealList();
        mToolbar = findViewById(R.id.app_bar_mainToolbar);
        mRecyclerList = findViewById(R.id.main_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);


        this.deleteDatabase(mShopDataUtil.ShopDataBaseName);
        mDbHelper = new ShopDataAdapter(this);
        mDbHelper.createDatabase();
        mMainViewAdapter = new MainViewAdapter(getShopData("breakfast"));
        mRecyclerList.setAdapter(mMainViewAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mSearchItem.isActionViewExpanded()) {
            animateSearchToolbar(3, false, false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String TAG = "handleIntent";
        Log.d(TAG, "handleIntent: ");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.d(TAG, "handleIntent: ACTION_SEARCH");
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getApplicationContext(),
                    ShopSearchSuggestions.AUTHORITY, ShopSearchSuggestions.MODE);
            suggestions.saveRecentQuery(searchQuery, null);
            hideSoftKeyboard();
            mMainViewAdapter.update(searchShopByName(searchQuery));
//        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
        } else {
            Log.d(TAG, "ACTION is " + intent.getAction());
        }
    }


    @Override
    public boolean onSearchRequested() {
        Log.d("onSearchRequested", "true");
        return super.onSearchRequested();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_toolbar_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.m_search).getActionView();
        if (mSearchView != null) {
            try {
                mSearchView.setSearchableInfo(
                        searchManager.getSearchableInfo(getComponentName()));
            } catch (Exception e) {
                Log.d("SearchableInfo", "e=" + e);
            }

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Snackbar.make(findViewById(R.id.main_view), query, Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra(SearchManager.QUERY, query);
                    intent.setAction(Intent.ACTION_SEARCH);
                    onNewIntent(intent);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            };
            mSearchView.setOnQueryTextListener(queryTextListener);
        }


        mSearchItem = menu.findItem(R.id.m_search);

        mSearchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Called when SearchView is collapsing
                if (mSearchItem.isActionViewExpanded()) {
                    animateSearchToolbar(3, false, false);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Called when SearchView is expanding
                animateSearchToolbar(2, true, true);
                return true;
            }
        });

        mGatchaItem = menu.findItem(R.id.m_gatcha);

        mGatchaItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GatchaActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_breakfast:
                setMainView("breakfast");
                break;
            case R.id.nav_lunch:
                setMainView("lunch");
                break;
            case R.id.nav_snack:
                setMainView("snack");
                break;
            case R.id.nav_supper:
                setMainView("supper");
                break;
            case R.id.nav_drink:
                setMainView("drink");
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<ArrayList<String>> searchShopByName(String query) {
        List<ArrayList<String>> searchResult = new ArrayList<>();
        Cursor shopData;
        String sqlCmd;
        ArrayList<String> rowValue;
        String[] queryList = query.trim().split(" ");
        int querySize = queryList.length;

        mDbHelper.open();

        for (String mealName : mealList.keySet()) {
            try {
                if (querySize > 1) {
                    sqlCmd = "SELECT _ID,店名 FROM " + mealName
                            + " WHERE 店名 LIKE '%" + queryList[0].trim() + "%'";
                    for (int i = 1; i < querySize; i++) {
                        sqlCmd += ("OR 店名 LIKE '%" + queryList[i].trim() + "%'");
                    }
                } else if (querySize == 1) {
                    sqlCmd = "SELECT _ID,店名 FROM " + mealName
                            + " WHERE 店名 LIKE '%" + query.trim() + "%'";
                } else {
                    sqlCmd = "SELECT _ID,店名 FROM breakfast";
                }

                Log.d("searchShopByName", sqlCmd);
                shopData = mDbHelper.getDataBySQL(sqlCmd);
                shopData.moveToFirst();
                String shopName;
                int shopImgID;
                int rowIndex;
                do {
                    rowValue = new ArrayList<>();
                    rowIndex = shopData.getInt(0);
                    shopName = shopData.getString(1);
                    shopImgID = mShopDataUtil.getShopImgID(mealName, rowIndex, 1);

                    rowValue.add(String.valueOf(rowIndex));
                    rowValue.add(shopName);
                    rowValue.add(String.valueOf(shopImgID));
                    rowValue.add(mealName);
                    searchResult.add(rowValue);
                } while (shopData.moveToNext());
            } catch (Exception e) {
                Log.e("searchShopByName", "Exception = " + e);
            }
        }

        mDbHelper.close();
        return searchResult;
    }

    private List<ArrayList<String>> getShopData(String mealName) {
        List<ArrayList<String>> DataSet = new ArrayList<>();

        mDbHelper.open();
        Cursor shopdata = mDbHelper.getTableData(mealName);

        if (shopdata.getCount() > 0) {
            String shopName;
            int shopImgID;
            int rowIndex;
            ArrayList<String> rowValue;
            shopdata.moveToFirst();
            do {
                rowValue = new ArrayList<>();
                rowIndex = shopdata.getInt(0);
                shopName = shopdata.getString(1);
                shopImgID = mShopDataUtil.getShopImgID(mealName, rowIndex, 1);

                rowValue.add(String.valueOf(rowIndex));
                rowValue.add(shopName);
                rowValue.add(String.valueOf(shopImgID));
                rowValue.add(mealName);
                DataSet.add(rowValue);
            } while (shopdata.moveToNext());
        }

        mDbHelper.close();
        return DataSet;
    }

    private void setMainView(String meal) {
        List<ArrayList<String>> shopData = getShopData(meal);
        mMainViewAdapter.update(shopData);
    }

    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {

        mToolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        mDrawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.quantum_grey_600));

        if (show) {
            int width = mToolbar.getWidth() -
                    (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                    ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                    isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, 0.0f, (float) width);
            createCircularReveal.setDuration(250);
            createCircularReveal.start();
        } else {
            int width = mToolbar.getWidth() -
                    (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                    ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                    isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, (float) width, 0.0f);
            createCircularReveal.setDuration(250);
            createCircularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mToolbar.setBackgroundColor(getThemeColor(MainActivity.this, R.attr.colorPrimary));
                    mDrawerLayout.setStatusBarBackgroundColor(getThemeColor(MainActivity.this, R.attr.colorPrimaryDark));
                }
            });
            createCircularReveal.start();
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        }
    }

    private boolean isRtl(Resources resources) {
        return resources.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    private static int getThemeColor(Context context, int id) {
        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(new int[]{id});
        int result = a.getColor(0, 0);
        a.recycle();
        return result;
    }


    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
                Log.d("hideSoftKeyboard", "e" + e);
            }
        }
    }
}
