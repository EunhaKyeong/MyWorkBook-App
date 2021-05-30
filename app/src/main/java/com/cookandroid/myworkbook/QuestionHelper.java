package com.cookandroid.myworkbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class QuestionHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myWorkBookDB.db";
    public static final String TABLE = "question";
    public static final String QUESTIONPK = "questionPK";
    public static final String QUESTIONTITLE = "questionTitle";
    public static final String QUESTIONIMG = "questionImg";
    public static final String QUESTIONDESC = "questionDesc";
    public static final String ANSWER = "answer";
    public static final String EXAMPK = "examPK";
    private SQLiteDatabase sqlDB = this.getWritableDatabase();

    public QuestionHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //question insert
    public long addQuestion(String questionTitle, byte[] questionImg, String questionDesc, String answer, String examPK) {
        ContentValues cv = new ContentValues();
        cv.put(QUESTIONTITLE, questionTitle);
        cv.put(ANSWER, answer);
        cv.put(EXAMPK, examPK);
        if (questionImg==null) {
            cv.put(QUESTIONDESC, questionDesc);
        } else {
            cv.put(QUESTIONIMG, questionImg);
        }
        long result = sqlDB.insert(TABLE, null, cv);

        if (result==-1) {
            System.out.println("실패!");
        } else {
            System.out.println("성공!");
        }

        return result;
    }

    //question select
    public Cursor getAllQuestions(String examPK) {
        String sql = "SELECT * FROM QUESTION WHERE examPK=?";
        Cursor cursor = sqlDB.rawQuery(sql, new String[]{examPK});

        return cursor;
    }

    //qustion delete
    public int removeQuestion(String questionPK) {
        int rowCnt = sqlDB.delete(TABLE,"questionPK=?",new String[]{questionPK});

        return rowCnt;
    }
}
