package com.ardovic.weatherappprototype.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public final static String DATABASE = "myDb";

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable1(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable1(SQLiteDatabase db) {
        db.execSQL("create table my_table ("
                + "_id integer primary key autoincrement,"
                + "city_id integer,"
                + "city_country_name text" + ");");
    }

    public static boolean isTableExists(SQLiteDatabase database, String tableName) {

        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
}
