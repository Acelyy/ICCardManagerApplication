package com.geminno.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xu on 2017/7/19.
 */

public class OpenHelper extends SQLiteOpenHelper {
    public OpenHelper(Context context) {
        super(context, "chepai", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table carpai(id integer PRIMARY KEY AUTOINCREMENT,che String,phone,String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
