package com.example.royal.delicacy_nfu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by user on 2017/11/5.
 */

public class DetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTextView_ShopName;
    private ImageView mImageView_Shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initialize();

        Intent intent = getIntent();
        String shopName = intent.getStringExtra("ShopName");
        Integer imageID = intent.getIntExtra("ShopImage", R.drawable.ic_menu_camera);


        //Toolbar
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setTitle(shopName);

        //ImageView
        mImageView_Shop.setImageResource(imageID);
        mImageView_Shop.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //TextView
        mTextView_ShopName.setText(shopName);
    }

    private void initialize() {
        mToolbar = findViewById(R.id.detail_Toolbar);
        mImageView_Shop = findViewById(R.id.shop_thumbnail);
        mTextView_ShopName = findViewById(R.id.detail_shopName);
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
}
