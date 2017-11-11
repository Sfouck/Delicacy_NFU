package com.example.royal.delicacy_nfu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;
import java.util.Locale;

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
    private ShopDataAdapter mDbHelper;

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
        setMainView("早餐");

        //Drawer
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //Navigation
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initialize() {
        mToolbar = findViewById(R.id.app_bar_mainToolbar);
        mRecyclerList = findViewById(R.id.main_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        ArrayList<Pair<String, Integer>> DataSet = new ArrayList<>();
        DataSet.add(new Pair<>("flower", R.drawable.flower));
        mMainViewAdapter = new MainViewAdapter(DataSet);
        mRecyclerList.setAdapter(mMainViewAdapter);
        this.deleteDatabase("ShopDataBase.sqlite");
        mDbHelper = new ShopDataAdapter(this);
        mDbHelper.createDatabase();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_toolbar_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

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
                Snackbar.make(findViewById(R.id.drawer_layout), "gatchatest", Snackbar.LENGTH_SHORT).show();
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
                showData("早餐");
                setMainView("早餐");
                break;
            case R.id.nav_lunch:
                showData("正餐");
                break;
            case R.id.nav_dessert:
                ArrayList<Pair<String, Integer>> DataSet = new ArrayList<>();
                DataSet.add(new Pair<>("flower", R.drawable.flower));
                DataSet.add(new Pair<>("cat", R.drawable.cat));
                mMainViewAdapter.update(DataSet);
                break;
            case R.id.nav_night:
                showData("宵夜");
                break;
            case R.id.nav_drink:
                showData("飲料");
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showData(String meal) {
        mDbHelper.open();
        Cursor testdata = mDbHelper.getTableData(meal);
        Log.d("testdata", testdata.getCount() + "");

        if (testdata.getCount() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("總共有 ").append((testdata.getCount())).append("筆資料\n");
            int index = 0;
            testdata.moveToFirst();
            stringBuilder.append(testdata.getColumnName(index)).append("\n");
            do {
                stringBuilder.append(testdata.getString(index)).append("\n");
            } while (testdata.moveToNext());
            Log.d("testdata", stringBuilder.toString());
        }
        mDbHelper.close();
    }

    private ArrayList getShopData(String meal) {
        ArrayList<Pair<String, Integer>> DataSet = new ArrayList<>();

        mDbHelper.open();
        Cursor shopdata = mDbHelper.getTableData(meal);

        if (shopdata.getCount() > 0) {
            String shopName;
            int shopImgID;
            int rowIndex = 1;
            shopdata.moveToFirst();
            do {
                shopName = shopdata.getString(0);
                shopImgID = getShopImgID(
                        "breakfast_" + String.format(Locale.CHINESE, "%03d", rowIndex) + "_01"
                );
                DataSet.add(Pair.create(shopName, shopImgID));
                rowIndex++;
            } while (shopdata.moveToNext());
        }
        mDbHelper.close();
        return DataSet;
    }

    private int getShopImgID(String name) {
        int resID = 0;
        try {
            resID = getResources().getIdentifier(name, "drawable", getApplicationInfo().packageName);
        } catch (Exception e) {
            Log.d("test.getShopImgID", "e=" + e);
        }
        return (resID > 0 ? resID : R.drawable.cat);
    }

    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {

        mToolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        mDrawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.quantum_grey_600));

        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int width = mToolbar.getWidth() -
                        (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                        ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                        isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, 0.0f, (float) width);
                createCircularReveal.setDuration(250);
                createCircularReveal.start();
            } else {
                TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-mToolbar.getHeight()), 0.0f);
                translateAnimation.setDuration(220);
                mToolbar.clearAnimation();
                mToolbar.startAnimation(translateAnimation);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
            } else {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-mToolbar.getHeight()));
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(220);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mToolbar.setBackgroundColor(getThemeColor(MainActivity.this, R.attr.colorPrimary));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mToolbar.startAnimation(animationSet);
            }

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

    private void setMainView(String meal) {
        ArrayList<Pair<String, Integer>> ShopDataList;
        ShopDataList = getShopData(meal);
        mMainViewAdapter.update(ShopDataList);
    }
}
