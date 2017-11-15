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
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by user on 2017/11/5.
 */

public class DetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTextView_ShopName;
    private ImageView mImageView_Shop;
    private ViewPager mViewPager_Slideshow;
    private HashMap<String, String> mealList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initialize();

        Intent intent = getIntent();
        int shopNum = intent.getIntExtra("ShopNumber", 0);
        String shopName = intent.getStringExtra("ShopName");
        int imageID = intent.getIntExtra("ShopImage", R.drawable.cat);
        String mealName;
        if (imageID == R.drawable.cat) {
            mealName = "cat";
        } else {
            mealName = getMealName(getResources().getResourceName(imageID));
        }
        Log.d("mealName", mealName);

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
    }

    private void initialize() {
        mealList = new HashMap<>();
        String[][] temp = {
                {"breakfast", "早餐"},
                {"lunch", "正餐"},
                {"snack", "小吃"},
                {"supper", "宵夜"},
                {"drink", "飲料"}
        };
        for (String[] meal : temp) {
            mealList.put(meal[0], meal[1]);
        }
        mToolbar = findViewById(R.id.detail_Toolbar);
        mImageView_Shop = findViewById(R.id.shop_thumbnail);
        mTextView_ShopName = findViewById(R.id.detail_shopName);
        mViewPager_Slideshow = findViewById(R.id.shop_image_slider);
        this.deleteDatabase("ShopDataBase.sqlite");
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

        StringBuilder contentBuilder = new StringBuilder();


        ShopDataAdapter mDbHelper = new ShopDataAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor shopData;
        String sqlCmd;
        String colTemp;
        int colIndex = 0;
        try {
            sqlCmd = "SELECT * FROM " + mealList.get(mealName) + " WHERE rowId = " + shopNum;
            Log.d("DetailActivity",sqlCmd);
            shopData = mDbHelper.getDataBySQL(sqlCmd);
            for (String column : shopData.getColumnNames()) {
                colTemp = shopData.getString(colIndex);
                if (!colTemp.equals(null) || !colTemp.equals("")) {
                    contentBuilder.append(column).append("：").append(colTemp).append("\n");
                }
                colIndex++;
            }
        } catch (Exception e) {
            Log.e("getDetailContent", "Exception = " + e);
        }


        this.deleteDatabase("ShopDataBase.sqlite");
        return contentBuilder.toString();
    }

    private ArrayList<Integer> getImageList(String mealName, int shopNum) {
        ArrayList<Integer> imgList = new ArrayList<>();

        int shopImgID;
        int imgIndex = 1;
        int errorCount = 0;
        StringBuilder imgName = new StringBuilder();
        do {
            imgName.setLength(0);
            imgName.append(mealName).append("_")
                    .append(String.format(Locale.CHINESE, "%03d", shopNum))
                    .append(String.format(Locale.CHINESE, "_%02d", imgIndex));
            shopImgID = getShopImgID(imgName.toString());
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

    private int getShopImgID(String name) {
        int resID = 0;
        try {
            resID = getResources().getIdentifier(
                    name, "drawable", getApplicationInfo().packageName);
        } catch (Exception e) {
            Log.d("test.getShopImgID", "e=" + e);
        }
        return (resID > 0 ? resID : R.drawable.cat);
    }

    private String getMealName(String url) {
        return url.substring(url.indexOf("/") + 1, url.length() - 7);
    }
}
