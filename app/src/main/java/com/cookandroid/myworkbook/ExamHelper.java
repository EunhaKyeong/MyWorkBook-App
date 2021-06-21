package com.cookandroid.myworkbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myWorkBookDB.db";
    public static final String TABLE = "exam";
    public static final String EXAMPK = "examPK";
    public static final String EXAMTITLE = "examTitle";
    public static final String TIMELIMIT = "timeLimit";
    public static final String USERPK = "userPK";
    private SQLiteDatabase sqlDB = this.getWritableDatabase();

    public ExamHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //exam insert
    public long addExam(String examTitle, String timeLimit, int userPK) {
        ContentValues cv = new ContentValues();
        cv.put(EXAMTITLE, examTitle);
        cv.put(TIMELIMIT, timeLimit);
        cv.put(USERPK, userPK);

        long result = sqlDB.insert(TABLE, null, cv);
        System.out.println(result);

        if (result == -1) {
            return result;
        } else {
            return result;
        }
    }

    //exam 정보 select
    public Cursor getAllExams(int userPK) {
        String sql = "select * from exam WHERE userPK=?;";
        Cursor cursor = sqlDB.rawQuery(sql, new String[]{String.valueOf(userPK)});

        return cursor;
    }

    //select userPK
    public int getUserPKByquestionPK(int questionPK) {
        String sql = "SELECT userPK FROM exam WHERE examPK=(SELECT examPK FROM question WHERE questionPK=" +
                questionPK + ")";
        Cursor cursor = sqlDB.rawQuery(sql, null);
        cursor.moveToFirst();

        return cursor.getInt(cursor.getColumnIndex("userPK"));
    }

    //exam update
    public int modifyExam(String examPK, String examTitle, String timeLimit) {
        ContentValues values = new ContentValues();
        values.put("examTitle", examTitle);
        values.put("timeLimit", timeLimit);

        return sqlDB.update(TABLE, values,"examPK=?", new String[]{examPK});
    }

    //exam delete
    public int removeExam(String examPK) {
        int rowCnt = sqlDB.delete(TABLE,"examPK=?",new String[]{examPK});

        return rowCnt;
    }
}
