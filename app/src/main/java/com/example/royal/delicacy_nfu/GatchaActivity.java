package com.example.royal.delicacy_nfu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by user on 2017/11/12.
 */

public class GatchaActivity extends AppCompatActivity implements OnItemSelectedListener {
    private Toolbar mToolbar;
    private Spinner mSpinner;
    private Button mGatchaButton;
    private ShopDataAdapter mDbHelper;
    private String selectedMealName;
    private ShopDataUtil mShopDataUtil;
    HashMap<String,String> mealList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gatcha);

        initialize();

        //Toolbar
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setTitle(R.string.toolbar_title_gatcha);

        //Spinner

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shops_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        //Button
        mGatchaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新增一個意圖(Intent)物件
                Intent intent = new Intent();
                String intentMealName = mShopDataUtil.getENGMealNameByCHS(selectedMealName);
                //隨機拿一個商店的資料
                String[] shopData = getRandomShopData(intentMealName).split(",");
                //設定去哪
                intent.setClass(getApplicationContext(), DetailActivity.class);
                //放入資料
                intent.putExtra("ShopNumber", Integer.valueOf(shopData[0]));
                intent.putExtra("ShopName", shopData[1]);
                intent.putExtra("ShopImage",
                        mShopDataUtil.getShopImgID(
                                intentMealName, Integer.valueOf(shopData[0]), 1));
                intent.putExtra("MealName",intentMealName);

                //把intent傳給詳細資料版面Activity
                startActivity(intent);
            }
        });

    }

    private void initialize() {
        mShopDataUtil = new ShopDataUtil(getApplicationContext());
        mealList = mShopDataUtil.getMealList();
        mToolbar = findViewById(R.id.gatcha_Toolbar);
        mDbHelper = new ShopDataAdapter(this);
        mDbHelper.createDatabase();
        mSpinner = findViewById(R.id.shops_spinner);
        mGatchaButton = findViewById(R.id.gatcha_button);
        selectedMealName = "";
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedMealName = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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

    private String getRandomShopData(String mealName) {
        ShopDataAdapter mDbHelper = new ShopDataAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor shopData;
        String sqlCmd;
        HashMap<String, String> rowTemp = new HashMap<>();
        int rowCount = 0;
        String result = "0,cat";
        try {
            sqlCmd = "SELECT _ID,店名 FROM " + mealName;
            shopData = mDbHelper.getDataBySQL(sqlCmd);
            shopData.moveToFirst();
            do {
                rowTemp.put(shopData.getString(0), shopData.getString(1));
                rowCount++;
            } while (shopData.moveToNext());

            String randomShopNum = String.valueOf((new Random()).nextInt(rowCount) + 1);
            result = randomShopNum + "," + rowTemp.get(randomShopNum);
        } catch (Exception e) {
            Log.e("getDetailContent", "Exception = " + e);
        }


        this.deleteDatabase("ShopDataBase.sqlite");
        return result;
    }
}
