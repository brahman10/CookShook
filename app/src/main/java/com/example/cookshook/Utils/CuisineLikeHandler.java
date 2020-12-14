package com.example.cookshook.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CuisineLikeHandler extends SQLiteOpenHelper {
    private static String DB_NAME = "databasecuisine5";
    private static int DB_VERSION = 3;
    private SQLiteDatabase db;

    public static final String WISHTABLE_TABLE = "cuisinelist";
    public static final String COLUMN_ID = "cuisine_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_IMG = "img";
    public CuisineLikeHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        String exe = "CREATE TABLE IF NOT EXISTS " + WISHTABLE_TABLE
                + "(" + COLUMN_ID + " TEXT primary key, "
                + COLUMN_IMG + " TEXT NOT NULL, "
                + COLUMN_USER_ID + " TEXT NOT NULL "

                + ")";

        db.execSQL(exe);


    }
    public boolean setwishTable(HashMap<String, String> map) {
        db = getWritableDatabase();
        if (isInLiketable(map.get(COLUMN_ID),map.get(COLUMN_USER_ID))) {
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, map.get(COLUMN_ID));
            values.put(COLUMN_IMG, map.get(COLUMN_IMG));
            values.put(COLUMN_USER_ID, map.get(COLUMN_USER_ID));
            db.insert(WISHTABLE_TABLE, null, values);
            return true;
        }
    }

    public boolean isInLiketable(String id, String user_id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + WISHTABLE_TABLE + " where " + COLUMN_ID + " LIKE '" + id+"'" +" and " + COLUMN_USER_ID + " LIKE '" + user_id+"'" ;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) return true;

        return false;
    }



    public ArrayList<HashMap<String, String>> getCuisineAll(String user_id) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + WISHTABLE_TABLE + " where " + COLUMN_USER_ID + " LIKE '" + user_id+"'";
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            map.put(COLUMN_USER_ID, cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
            map.put(COLUMN_IMG,cursor.getString(cursor.getColumnIndex(COLUMN_IMG)));
            list.add(map);
            cursor.moveToNext();
        }
        db.close();
        return list;
    }

    public ArrayList<HashMap<String, String>> getCuisine(int product_id, String user_id) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + WISHTABLE_TABLE + " where " + COLUMN_ID + " = " + product_id + " and " + COLUMN_USER_ID + " = " +  user_id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            map.put(COLUMN_USER_ID, cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
            map.put(COLUMN_IMG,cursor.getString(cursor.getColumnIndex(COLUMN_IMG)));
            list.add(map);
            cursor.moveToNext();
        }
        db.close();
        return list;
    }


    public void removeItemFromWishtable(String id, String user_id) {
        db = getReadableDatabase();
        db.execSQL("delete from " + WISHTABLE_TABLE + " where " + COLUMN_ID + " LIKE '" + id+"'" + " and " + COLUMN_USER_ID + " LIKE '" + user_id+"'" );
        db.close();

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
}

