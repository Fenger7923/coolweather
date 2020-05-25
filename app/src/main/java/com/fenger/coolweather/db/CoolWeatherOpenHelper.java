package com.fenger.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * com.fenger.coolweather.db
 * Created by fenger
 * in 2020/5/10
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    /**
     * 省级建表
     */
    public static final String CREATE_PROVINCE = "create table Province (" + "id integer primary key autoincrement, "
            + "province_name text, "
            + "province_code text)";

    /**
     * 市级建表
     */
    public static final String CREATE_CITY = "create table City (" + "id integer primary key autoincrement, "
            + "city_name text, "
            + "city_code text, "
            + "province_id integer)";

    /**
     * 县级建表
     */
    public static final String CREATE_COUNTRY = "create table Country (" + "id integer primary key autoincrement, "
            + "country_name text, "
            + "country_code text, "
            + "city_id integer)";

    public CoolWeatherOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
