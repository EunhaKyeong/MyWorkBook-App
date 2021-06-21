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

public class ResultActivity extends Activity {
    private TextView examTitleTV, elapsedTimeTV;
    private ListView resultLV;
    private Button goExamListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        QuestionHelper questionDB = new QuestionHelper(ResultActivity.this);
        ExamHelper examDB = new ExamHelper(ResultActivity.this);
        UserHelper userDB = new UserHelper(ResultActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        //TestActivity를 통해 전달받은 데이터 : examTitle, 소요시간, shuffled된 questionPK, 제출 답안
        Intent intent = getIntent();
        String examTitle = intent.getStringExtra("examTitle");
        int elapsedTime = intent.getIntExtra("elapsedTime", 0);
        ArrayList<Integer> shuffledPKs = intent.getIntegerArrayListExtra("shuffledPK");
        ArrayList<String> submitAnswers = intent.getStringArrayListExtra("submitAnswer");

        //TextView에 examTitle 보여주기
        examTitleTV = findViewById(R.id.examTitleTV);
        examTitleTV.setText(examTitle);

        //TextView에 소요시간 보여주기
        int hour = elapsedTime / 3600;
        int minute = (elapsedTime % 3600) / 60;
        int seconds = elapsedTime % 60;
        elapsedTimeTV = findViewById(R.id.elapsedTimeTV);
        elapsedTimeTV.setText(elapsedTimeTV.getText().toString() + String.format("%02d", hour) + ":" +
                String.format("%02d", minute) + ":" +  String.format("%02d", seconds));

        //shuffledPK를 이용해 DB에서 정답 데이터 가져오기->(questionPK: {answer= , title=})
        Cursor cursor = questionDB.getTitleAnswers(
                shuffledPKs.toString().substring(1, shuffledPKs.toString().length()-1));
        HashMap<Integer, HashMap<String, String>> TitleAnswersHM = new HashMap<Integer, HashMap<String, String>>();
        while(cursor.moveToNext()) {
            HashMap<String, String> titleAnswerHm = new HashMap<String, String>();
            titleAnswerHm.put("title", cursor.getString(cursor.getColumnIndex("questionTitle")));
            titleAnswerHm.put("answer", cursor.getString(cursor.getColumnIndex("answer")));

            TitleAnswersHM.put(cursor.getInt(cursor.getColumnIndex("questionPK")), titleAnswerHm);
        }

        //리스트뷰 화면 만들기
        resultLV = findViewById(R.id.resultLV);
        ResultListAdapter listAdapter =
                new ResultListAdapter(this, shuffledPKs, submitAnswers, TitleAnswersHM);
        resultLV.setAdapter(listAdapter);

        //시험 목록 버튼 클릭 -> 시험 목록 화면으로 이동
        goExamListBtn = findViewById(R.id.goExamListBtn);
        goExamListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userPK = examDB.getUserPKByquestionPK(shuffledPKs.get(0));
                String nickname = userDB.getNickname(1);

                Intent goExamActivity = new Intent(ResultActivity.this, ExamActivity.class);
                goExamActivity.putExtra("userPK", userPK);
                goExamActivity.putExtra("nickname", nickname);
                startActivityForResult(goExamActivity, 0);

                finish();
            }
        });
    }
}
