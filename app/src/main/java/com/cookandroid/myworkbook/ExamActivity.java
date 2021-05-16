package com.cookandroid.myworkbook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ExamActivity extends AppCompatActivity {
    private int userPK;
    private String nickname;
    private LoginDialog.MyDBHelper myHelper;
    private SQLiteDatabase sqlDB;
    private Button logoutBtn, plusBtn;
    private TextView nicknameTV;
    private GridLayout folderGrid;

    //DB 연동
    public class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context c) {
            super(c, "myWorkBookDB.db", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) { }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_list);

        //DB 설정
        myHelper = new LoginDialog.MyDBHelper(getApplicationContext());

        //상단바 제거
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //로그인 후 다이얼로그를 통해 전달받은 userPK, nickname으로 필드값 저장.
        Intent intent = getIntent();
        this.userPK = intent.getIntExtra("userPK", 0);
        this.nickname = intent.getStringExtra("nickname");

        logoutBtn = findViewById(R.id.logoutBtn);
        //plusBtn = findViewById(R.id.plusBtn);
        nicknameTV = findViewById(R.id.nicknameTV);
        folderGrid = findViewById(R.id.folderGrid);

        //화면에 닉네임 출력.
        nicknameTV.setText(this.nickname + " 님");

        //getExamTitle() 함수를 호출해 시험명 리스트를 가져옴.
        List<String> examTitles = getExamTitle(this.userPK);
        makeFolder(examTitles);
    }

    //해당 사용자의 시험 목록(examTitle)을 가져오는 함수
    private List<String> getExamTitle(int userPK) {
        List<String> examTitles = new ArrayList<String>();

        sqlDB = myHelper.getReadableDatabase();
        String query = "SELECT examTitle FROM exam WHERE userPK=" + userPK + ";";
        Cursor cursor = sqlDB.rawQuery(query, null);

        while (cursor.moveToNext()) {
            examTitles.add(cursor.getString(0));
        }
        sqlDB.close();

        return examTitles;
    }

    //시험 folder를 만드는 함수
    private void makeFolder(List<String> examTitles) {
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180,
                getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140,
                getResources().getDisplayMetrics());

        for (String title:examTitles) {
            Button folder = new Button(getApplicationContext());
            folder.setBackgroundColor(Color.rgb(255, 242, 190));
            folder.setWidth(width);
            folder.setHeight(height);
            folder.setGravity(Gravity.CENTER);
            folder.setPadding(20, 5, 20, 5);
            folder.setText(title);
            folder.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/bmhannaair.ttf"),
                            Typeface.BOLD);
            folder.setTextSize(Dimension.SP, 20);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(15,15,15,15);  // 왼쪽, 위, 오른쪽, 아래 순서입니다.
            folder.setLayoutParams(params);

            this.folderGrid.addView(folder);
        }
    }
}
