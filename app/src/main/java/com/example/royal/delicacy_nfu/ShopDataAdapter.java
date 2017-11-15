package com.example.royal.delicacy_nfu;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by user on 2017/11/10.
 */

public class ShopDataAdapter {
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private ShopDataHelper mDbHelper;

    public ShopDataAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new ShopDataHelper(mContext);
    }

    public ShopDataAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (Exception mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public ShopDataAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDb = mDbHelper.getReadableDatabase();
            mDb.beginTransaction();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDb.endTransaction();
        mDb.close();
        mDbHelper.close();
    }

    public Cursor getTableData(String mealName) {
        try {
            String sql = "SELECT * FROM " + mealName;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToFirst();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTableData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getDataBySQL(String sqlCmd){
        try {
            Cursor mCur = mDb.rawQuery(sqlCmd, null);
            if (mCur != null) {
                mCur.moveToFirst();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTableData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
}
