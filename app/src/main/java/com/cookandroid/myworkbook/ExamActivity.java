package com.cookandroid.myworkbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamActivity extends Activity {
    private ExamHelper dbHelper;
    private int userPK;
    private String nickname;
    private TextView nicknameTV;
    private Button plusBtn;
    private ExamDialog addExamDialog;
    private ArrayList<HashMap<String, Object>> exams = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_activity);

        //DB 인스턴스 생성
        dbHelper = new ExamHelper(ExamActivity.this);

        //로그인 후 다이얼로그를 통해 전달받은 userPK, nickname으로 필드값 저장.
        Intent intent = getIntent();
        this.userPK = intent.getIntExtra("userPK", 0);
        this.nickname = intent.getStringExtra("nickname");
        exams = getExamInfo(this.userPK);

        nicknameTV = findViewById(R.id.nicknameTV);
        plusBtn = findViewById(R.id.plusBtn);

        //화면에 닉네임 출력.
        nicknameTV.setText(this.nickname + "님");

        //그리드뷰 만들기
        bindGrid();

        //+버튼 누르면 시험 폴더를 생성하는 대화상자 보여주기
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExamDialog = new ExamDialog(ExamActivity.this, userPK, exams);
                addExamDialog.show();
            }
        });
    }

    private void bindGrid() {
        //그리드뷰
        GridView examGV = findViewById(R.id.examGridView);
        // Adapter 추가
        ExamGridAdapter gridAdapter = new ExamGridAdapter(this, this.exams, this.userPK);
        examGV.setAdapter(gridAdapter);
    }

    //해당 사용자의 examTitle과 examID를 가져오는 함수
    private ArrayList<HashMap<String, Object>> getExamInfo(int userPK) {
        ArrayList<HashMap<String, Object>> examInfo = new ArrayList<HashMap<String, Object>>();
        Cursor exams = dbHelper.getAllExams(userPK);

        while(exams.moveToNext()) {
            HashMap<String, Object> tempExam = new HashMap<String, Object>();
            tempExam.put("examPK", exams.getInt(exams.getColumnIndex("examPK")));
            tempExam.put("examTitle", exams.getString(exams.getColumnIndex("examTitle")));
            tempExam.put("timeLimit", exams.getString(exams.getColumnIndex("timeLimit")));

            examInfo.add(tempExam);
        }

        return examInfo;
    }
}
