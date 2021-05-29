package com.cookandroid.myworkbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myWorkBookDB.db";
    private SQLiteDatabase sqlDB;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //user 정보 select
    public Cursor getUser(String id, String pwd) {
        sqlDB = this.getWritableDatabase();
        String query = "SELECT * FROM user WHERE userID='" + id + "' AND password='" + pwd + "';";
        Cursor cursor = sqlDB.rawQuery(query, null);

        return cursor;
    }
    //exam 정보 select
    public Cursor getExams(int userPK) {
        sqlDB = this.getReadableDatabase();
        String query = "SELECT examTitle, examPK FROM exam WHERE userPK=" + userPK + ";";
        Cursor cursor = sqlDB.rawQuery(query, null);

        return cursor;
    }
}
