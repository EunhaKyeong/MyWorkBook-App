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
    private Button plusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);

        Intent intent = getIntent();
        this.examPK = intent.getStringExtra("examPK");
        this.timeLimit = intent.getStringExtra("timeLimit");

        tvExamTitle = findViewById(R.id.tvExamTitle);
        plusBtn = findViewById(R.id.plusBtn);
        tvTimeLimit = findViewById(R.id.tvTimeLimit);

        tvExamTitle.setText(intent.getStringExtra("examTitle"));
        if (!timeLimit.equals("00:00:00")) {
            tvTimeLimit.setText("제한시간 : " + this.timeLimit);
        }

        bindGrid();

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionActivity.this, QuestionAddUpdateActivity.class);
                intent.putExtra("mode", "CREATE");
                intent.putExtra("examPK", examPK);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("reequestionCode: " + requestCode);

        if (requestCode==0&&resultCode==RESULT_OK) {    //문제 생성 요청일 때
            HashMap<String, Object> newQuestion = new HashMap<String, Object>();
            newQuestion.put("questionPK", data.getStringExtra("questionPK"));
            newQuestion.put("questionTitle", data.getStringExtra("questionTitle"));
            newQuestion.put("questionImg", data.getByteArrayExtra("questionImg"));
            newQuestion.put("questionDesc", data.getStringExtra("questionDesc"));
            newQuestion.put("answer", data.getStringExtra("answer"));

            questions.add(newQuestion);

        } else if (requestCode==1&&resultCode==RESULT_OK) { //문제 수정 요청일 때
            int position = Integer.parseInt(data.getStringExtra("questionPK"))-1;
            questions.get(position).put("questionTitle", data.getStringExtra("questionTitle"));
            questions.get(position).put("questionImg", data.getByteArrayExtra("questionImg"));
            questions.get(position).put("questionDesc", data.getStringExtra("questionDesc"));
            questions.get(position).put("answer", data.getStringExtra("answer"));
        }

        listAdapter.notifyDataSetChanged();
    }

    private void bindGrid() {
        QuestionHelper db = new QuestionHelper(QuestionActivity.this);
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
