package com.prime.awitd.contactbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SantaClaus on 18/01/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Member.db";
    private static final String TABLE_NAME = "member_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "TEAM";
    public static final String COL_4 = "MOBILE";
    public static final String COL_5 = "HOME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COL_1 + " TEXT PRIMARY KEY," + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 + " TEXT," + COL_5 + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String id, String name, String team, String mobile, String home) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, team);
        contentValues.put(COL_4, mobile);
        contentValues.put(COL_5, home);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    public Cursor getName() {
        String selectQuery = "SELECT " + COL_2 + " FROM " + TABLE_NAME;
        Log.e("Select Name Query...", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getDetail(String name) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + "='" + name + "'";
        Log.e("Select All from Member", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public boolean updateData(String id, String name, String team, String mobile, String home) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, team);
        contentValues.put(COL_4, mobile);
        contentValues.put(COL_5, home);
        db.update(TABLE_NAME, contentValues, " ID = ? ", new String[]{id});
        return true;
    }
}
