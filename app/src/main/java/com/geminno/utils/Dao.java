package com.geminno.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.geminno.iccardmanagerapplication.Car;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xu on 2017/7/19.
 */

public class Dao {
    private OpenHelper dbHelper;

    public Dao(Context context) {
        dbHelper = new OpenHelper(context);
    }

    public List<Car> getChe() {
        List<Car> list = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select * from  carpai ";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Car car = new Car();
            car.setCph(cursor.getString(1));
            car.setPhone(cursor.getString(2));
            list.add(car);
        }
        cursor.close();
        return list;
    }

    public boolean isExists(String name) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select count(*)from  carpai where che=? ";
        Cursor cursor = database.rawQuery(sql, new String[]{name});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count > 0) {
            return true;
        }
        return false;
    }

    public void closeDb() {
        dbHelper.close();
    }

    public void saveInfos(String chehao, String phone) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "insert into  carpai" +
                "(che,phone) values (?,?)";
        Object[] bindArgs = {chehao, phone};
        database.execSQL(sql, bindArgs);

    }

    public void delete(String name) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        database.delete("carpai", "che=?", new String[]{name});
        database.close();
    }
}
