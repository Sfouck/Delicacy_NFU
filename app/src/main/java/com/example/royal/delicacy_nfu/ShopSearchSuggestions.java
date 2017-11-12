package com.example.royal.delicacy_nfu;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by user on 2017/11/12.
 */

public class ShopSearchSuggestions extends SearchRecentSuggestionsProvider {
    private static  String TAG = "ShopSearchSuggestions";
    public final static String AUTHORITY = "com.example.royal.delicacy_nfu.ShopSearchSuggestions";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public ShopSearchSuggestions(){
        setupSuggestions(AUTHORITY, MODE);
    }

//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

//        Log.d(TAG, "query: ");
//
//        String[] columns = {"_ID", SearchManager.SUGGEST_COLUMN_TEXT_1};
//        MatrixCursor matrixCursor = new MatrixCursor(columns);
//
//        matrixCursor.addRow(new Object[] {1, "test"});
//        matrixCursor.addRow(new Object[] {2, "test1"});
//

//        return super.query(uri,projection,selection,selectionArgs,sortOrder);
//    }
}
