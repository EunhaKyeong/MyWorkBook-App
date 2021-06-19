package com.cookandroid.myworkbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myWorkBookDB.db";
    private SQLiteDatabase sqlDB = this.getWritableDatabase();;

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
        String query = "SELECT * FROM user WHERE userID='" + id + "' AND password='" + pwd + "';";
        Cursor cursor = sqlDB.rawQuery(query, null);

        return cursor;
    }

    //SELECT nickname
    public String getNickname(int userPK) {
        String query = "SELECT nickname FROM user WHERE userPK=?";
        Cursor cursor = sqlDB.rawQuery(query, new String[] {String.valueOf(userPK)});
        cursor.moveToFirst();

        return cursor.getString(cursor.getColumnIndex("nickname"));
    }

    //exam 정보 select
    public Cursor getExams(int userPK) {
        String query = "SELECT examTitle, examPK FROM exam WHERE userPK=" + userPK + ";";
        Cursor cursor = sqlDB.rawQuery(query, null);

        return cursor;
    }
}
