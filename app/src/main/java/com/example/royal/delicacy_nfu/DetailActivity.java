package com.example.royal.delicacy_nfu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by user on 2017/11/5.
 */

public class DetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTextView_ShopName;
    private ImageView mImageView_Shop;
    private ViewPager mViewPager_Slideshow;
    private ShopDataUtil mShopDataUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initialize();

        Intent intent = getIntent();
        int shopNum = intent.getIntExtra("ShopNumber", 0);
        int imageID = intent.getIntExtra("ShopImage", R.drawable.cat);
        String shopName = intent.getStringExtra("ShopName");
        String mealName = intent.getStringExtra("MealName");

        //Toolbar
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setTitle(shopName);

        //ImageView
        GlideApp.with(this)
                .load(imageID)
                .thumbnail(0.2f)
                .centerCrop()
                .into(mImageView_Shop);

        //TextView
        mTextView_ShopName.setText(getDetailContent(mealName, shopNum));

        //ViewPage
        ArrayList<Integer> imgList = getImageList(mealName, shopNum);
        ImageSlideAdapter adapter = new ImageSlideAdapter(getSupportFragmentManager(), imgList);
        mViewPager_Slideshow.setAdapter(adapter);

        mImageView_Shop.setImageAlpha(50);

    }

    private void initialize() {
        mShopDataUtil = new ShopDataUtil(getApplicationContext());
        mToolbar = findViewById(R.id.detail_Toolbar);
        mImageView_Shop = findViewById(R.id.shop_thumbnail);
        mTextView_ShopName = findViewById(R.id.detail_shopName);
        mViewPager_Slideshow = findViewById(R.id.shop_image_slider);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    private String getDetailContent(String mealName, int shopNum) {
        if (mealName.equals("cat")) return "cat";

        ShopDataAdapter mDbHelper = new ShopDataAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        StringBuilder contentBuilder = new StringBuilder();
        Cursor shopData;
        String sqlCmd;
        String colTemp;
        int colIndex = 0;
        try {
            sqlCmd = "SELECT * FROM " + mealName + " WHERE rowId = " + shopNum;
            Log.d("DetailActivity",sqlCmd);
            shopData = mDbHelper.getDataBySQL(sqlCmd);
            for (String column : shopData.getColumnNames()) {
                colTemp = shopData.getString(colIndex);
                if (colTemp != null) {
                    contentBuilder.append(column).append("：").append(colTemp).append("\n");
                }
                colIndex++;
            }
        } catch (Exception e) {
            Log.e("getDetailContent", "Exception = " + e);
        }

        mDbHelper.close();
        return contentBuilder.toString();
    }

    private ArrayList<Integer> getImageList(String mealName, int shopNum) {
        ArrayList<Integer> imgList = new ArrayList<>();

        int shopImgID;
        int imgIndex = 1;
        int errorCount = 0;
        do {
            shopImgID = mShopDataUtil.getShopImgID(mealName,shopNum,imgIndex);
            Log.d("slideGetImageList", shopImgID + "");
            if (shopImgID != R.drawable.cat) {
                imgList.add(shopImgID);
            } else {
                break;
            }
            imgIndex++;
            errorCount++;
        } while (errorCount < 100);
        Log.d("slideGetImageList", imgList.size() + "");
        return imgList;
    }
}
