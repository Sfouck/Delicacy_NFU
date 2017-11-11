package com.example.royal.delicacy_nfu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by user on 2017/11/5.
 */

public class DetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTextView_ShopName;
    private ImageView mImageView_Shop;
    private ViewPager mViewPager_Slideshow;
    private ShopDataAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initialize();

        Intent intent = getIntent();
        int shopNum = intent.getIntExtra("ShopNumber", 0) + 1;
        String shopName = intent.getStringExtra("ShopName");
        int imageID = intent.getIntExtra("ShopImage", R.drawable.ic_menu_camera);


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
        mTextView_ShopName.setText(shopName);

        //ViewPage
        ArrayList<Integer> imgList = getImageList(shopNum);
        ImageSlideAdapter adapter = new ImageSlideAdapter(getSupportFragmentManager(), imgList);
        mViewPager_Slideshow.setAdapter(adapter);
    }

    private void initialize() {
        mToolbar = findViewById(R.id.detail_Toolbar);
        mImageView_Shop = findViewById(R.id.shop_thumbnail);
        mTextView_ShopName = findViewById(R.id.detail_shopName);
        mViewPager_Slideshow = findViewById(R.id.shop_image_slider);
        mDbHelper = new ShopDataAdapter(this);
        mDbHelper.createDatabase();
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

    private ArrayList<Integer> getImageList(int shopNum) {
        ArrayList<Integer> imgList = new ArrayList<>();

        int shopImgID;
        int imgIndex = 1;
        int errorCount = 0;
        StringBuilder imgName = new StringBuilder();
        do {
            imgName.setLength(0);
            imgName.append("breakfast_")
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
}
