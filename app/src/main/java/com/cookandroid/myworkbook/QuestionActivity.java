package com.cookandroid.myworkbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionActivity extends Activity {
    private ArrayList<HashMap<String, Object>> questions;
    private QuestionListAdapter listAdapter;
    private String examPK, timeLimit;
    private TextView tvExamTitle, tvTimeLimit;
    private Button plusBtn, btnStart;
    private QuestionHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);

        db = new QuestionHelper(QuestionActivity.this); //qestion table 관련 DB 실행.

        //시험 목록 화면을 통해 전달 받은 데이터
        Intent intent = getIntent();
        this.examPK = intent.getStringExtra("examPK");
        this.timeLimit = intent.getStringExtra("timeLimit");

        tvExamTitle = findViewById(R.id.tvExamTitle);
        plusBtn = findViewById(R.id.plusBtn);
        tvTimeLimit = findViewById(R.id.tvTimeLimit);
        btnStart = findViewById(R.id.btnStart);

        tvExamTitle.setText(intent.getStringExtra("examTitle"));
        if (!timeLimit.equals("00:00:00")) {
            tvTimeLimit.setText("제한시간 : " + this.timeLimit);
        }

        bindGrid(); //list view 화면 구성하는 함수 호출.

        //+ 버튼 누르면 문제를 추가하는 화면으로 이동.
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionActivity.this, QuestionAddUpdateActivity.class);
                intent.putExtra("mode", "CREATE");
                intent.putExtra("examPK", examPK);
                startActivityForResult(intent, 0);
            }
        });

        //시험 시작 버튼을 누르면 시험 진행 화면으로 이동.
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTestIntent = new Intent(QuestionActivity.this, TestActivity.class);
                toTestIntent.putExtra("examPK", examPK);
                toTestIntent.putExtra("examTitle", tvExamTitle.getText().toString());
                toTestIntent.putExtra("timeLimit", timeLimit);

                startActivity(toTestIntent);
            }
        });
    }

    //문제 생성, 수정 기능이 완료된 후 다시 문제 목록으로 돌아왔을 때 변경된 데이터를 화면에 보여줌.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0&&resultCode==RESULT_OK) {    //문제 생성 요청일 때
            HashMap<String, Object> newQuestion = new HashMap<String, Object>();
            newQuestion.put("questionPK", data.getStringExtra("questionPK"));
            newQuestion.put("questionTitle", data.getStringExtra("questionTitle"));
            newQuestion.put("questionImg", data.getByteArrayExtra("questionImg"));
            newQuestion.put("questionDesc", data.getStringExtra("questionDesc"));
            newQuestion.put("answer", data.getStringExtra("answer"));

            questions.add(newQuestion);

        } else if (requestCode==1&&resultCode==RESULT_OK) { //문제 수정 요청일 때
            System.out.println(data.toString());
            int position = data.getIntExtra("position", -1);
            questions.get(position).put("questionTitle", data.getStringExtra("questionTitle"));
            questions.get(position).put("questionImg", data.getByteArrayExtra("questionImg"));
            questions.get(position).put("questionDesc", data.getStringExtra("questionDesc"));
            questions.get(position).put("answer", data.getStringExtra("answer"));
        }

        listAdapter.notifyDataSetChanged();
    }

    //리스트뷰 화면 구성하는 함수.
    private void bindGrid() {
        Cursor cursor = db.getAllQuestions(this.examPK);
        questions = new ArrayList<HashMap<String, Object>>();

        while(cursor.moveToNext()) {
            HashMap<String, Object> question = new HashMap<String, Object>();
            question.put("questionPK", cursor.getInt(cursor.getColumnIndex("questionPK")));
            question.put("questionTitle", cursor.getString(cursor.getColumnIndex("questionTitle")));
            question.put("questionImg", cursor.getBlob(cursor.getColumnIndex("questionImg")));
            question.put("questionDesc", cursor.getString(cursor.getColumnIndex("questionDesc")));
            question.put("answer", cursor.getString(cursor.getColumnIndex("answer")));

            questions.add(question);
        }

        //리스트뷰
        ListView questionLV = findViewById(R.id.lvQuestion);
        // Adapter 추가
        listAdapter = new QuestionListAdapter(this, questions);
        questionLV.setAdapter(listAdapter);
    }
}
