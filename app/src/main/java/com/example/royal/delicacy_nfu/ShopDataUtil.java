package com.example.royal.delicacy_nfu;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Created by user on 2017/11/14.
 */

public class ShopDataUtil {
    final private Context mContext;
    private HashMap<String,String> mealList ;
    final String ShopDataBaseName = "ShopDataBase.sqlite";


    ShopDataUtil(Context context) {
        mContext = context;
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
    }

    public int getShopImgID(String meal, int index, int num) {
        //        String path = meal + "_" + String.format(Locale.CHINESE, "%03d", index)
        //        + "_" + String.format(Locale.CHINESE, "%02d", num);
        int resID = 0;
        String path = String.format(Locale.getDefault(), "%s_%03d_%02d", meal, index, num);
        try {
            resID = mContext.getResources()
                    .getIdentifier(path, "drawable", mContext.getApplicationInfo().packageName);
        } catch (Exception e) {
            Log.d("test.getShopImgID", "e=" + e);
        }
        return (resID > 0 ? resID : R.drawable.cat);
    }

    private String getMealNameByImageURL(String url) {
        return url.substring(url.indexOf("/") + 1, url.length() - 7);
    }

    public String getENGMealNameByCHS(String engMeal){
        for (Map.Entry<String,String> entry : mealList.entrySet()) {
            if (Objects.equals(engMeal, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public HashMap<String,String> getMealList(){
        return  this.mealList;
    }
}
