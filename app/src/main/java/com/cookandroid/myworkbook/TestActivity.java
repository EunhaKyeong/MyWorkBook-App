package com.cookandroid.myworkbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class TestActivity extends Activity {
    private final int COUNT_DOWN_INTERVAL = 1000;
    private static int millisinfuter;
    private CountDownTimer countDownTimer;
    private QuestionHelper db;
    private int currentQ = 0;
    ArrayList<String> submitAnswer = new ArrayList<String>();

    private TextView examTitleTV, timeLimitTV, questionDescTV;
    private Button finishBtn;
    private ImageView questionImgIV, backArrow, frontArrow;
    private EditText answerET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        db = new QuestionHelper(TestActivity.this);

        //QuestionActivity를 통해 전달 받는 데이터 : examPK, examTitle, timeLimit
        Intent intent = getIntent();
        String examPK = intent.getStringExtra("examPK");
        String examTitle = intent.getStringExtra("examTitle");
        String timeLimit = intent.getStringExtra("timeLimit");

        examTitleTV = findViewById(R.id.examTitleTV);
        examTitleTV.setText(examTitle);
        timeLimitTV = findViewById(R.id.timLimitTV);

        //타이머 시작
        String[] times = timeLimit.split(":");
        millisinfuter = (Integer.parseInt(times[0])*3600 + Integer.parseInt(times[1])*60 + Integer.parseInt(times[2])) * 1000;
        startTimer();

        //전달 받은 examPK로 questionPK, questionImg, questionDesc SELECT
        ArrayList<Integer> questionPKs = new ArrayList<Integer>();
        ArrayList<byte[]> questionImgs = new ArrayList<byte[]>();
        ArrayList<String> questionDescs = new ArrayList<String>();
        Cursor cursor = db.getImgDesc(examPK);
        while(cursor.moveToNext()) {
            questionPKs.add(cursor.getInt(cursor.getColumnIndex("questionPK")));
            questionImgs.add(cursor.getBlob(cursor.getColumnIndex("questionImg")));
            questionDescs.add(cursor.getString(cursor.getColumnIndex("questionDesc")));
        }

        //examPK 안에 존재하는 문제만큼 shuffle
        ArrayList<Integer> shuffledIndex = new ArrayList<Integer>();
        for (int i=0; i<questionImgs.size(); i++) {
            shuffledIndex.add(i);
        }
        Collections.shuffle(shuffledIndex);

        //shuffledIndex arrayList에서 첫번째 인덱스를 가져와 문제 화면에 나타내기.
        int index = shuffledIndex.get(currentQ);
        questionDescTV = findViewById(R.id.questionDescTV);
        questionImgIV = findViewById(R.id.questionImgIV);
        answerET = findViewById(R.id.answerET);
        changeQuestion(questionImgs.get(index), questionDescs.get(index));

        //화살표 설정(첫번째 문제이므로 다음 버튼만 VISIBLE)
        backArrow = findViewById(R.id.backArrow);
        frontArrow = findViewById(R.id.frontArrow);
        backArrow.setVisibility(View.INVISIBLE);
        frontArrow.setVisibility(View.VISIBLE);

        //다음 문제 화살표 클릭 : 이전 문제에 사용자가 입력한 답 저장 -> 다음 문제로 이동.
        frontArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사용자가 입력한 답 저장
                try {
                    submitAnswer.set(currentQ, answerET.getText().toString());
                } catch(IndexOutOfBoundsException e) {
                    submitAnswer.add(answerET.getText().toString());
                }

                //다음 문제로 변경
                currentQ++;
                int index = shuffledIndex.get(currentQ);
                changeQuestion(questionImgs.get(index), questionDescs.get(index));

                if (currentQ==shuffledIndex.size()-1) {
                    backArrow.setVisibility(View.VISIBLE);
                    frontArrow.setVisibility(View.INVISIBLE);
                    finishBtn.setText("답안지 제출");
                } else {
                    backArrow.setVisibility(View.VISIBLE);
                    frontArrow.setVisibility(View.VISIBLE);
                    finishBtn.setText("나가기");
                }
            }
        });

        //뒤로 가기 화살표를 클릭하면 : 이전 문제에 대해 사용자가 입력한 답 저장 -> 이전 문제로 이동.
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사용자가 입력한 답 저장
                try {
                    submitAnswer.set(currentQ, answerET.getText().toString());
                } catch(IndexOutOfBoundsException e) {
                    submitAnswer.add(answerET.getText().toString());
                }

                //이전 문제 화면으로 이동.
                currentQ--;
                int index = shuffledIndex.get(currentQ);
                changeQuestion(questionImgs.get(index), questionDescs.get(index));

                if (currentQ==0) {
                    backArrow.setVisibility(View.INVISIBLE);
                    frontArrow.setVisibility(View.VISIBLE);
                } else {
                    backArrow.setVisibility(View.VISIBLE);
                    frontArrow.setVisibility(View.VISIBLE);
                }
            }
        });

        //나가기 or 답안지 제출 버튼을 클릭했을 때
        finishBtn = findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishTimer();

                if (finishBtn.getText().equals("답안지 제출")) {
                    //사용자가 입력한 답 저장
                    try {
                        submitAnswer.set(currentQ, answerET.getText().toString());
                    } catch(IndexOutOfBoundsException e) {
                        submitAnswer.add(answerET.getText().toString());
                    }
                    //shuffled된 questionPK 저장.
                    ArrayList<Integer> shuffledPK = new ArrayList<Integer>();
                    for (int index:shuffledIndex) {
                        shuffledPK.add(questionPKs.get(index));
                    }
                    //소요시간 계산
                    String[] elapsedTimeStr = timeLimitTV.getText().toString().split(":");
                    int elapsedTime = Integer.parseInt(elapsedTimeStr[0])*3600 +
                            Integer.parseInt(elapsedTimeStr[1])*60 +
                            Integer.parseInt(elapsedTimeStr[2]);
                    elapsedTime = millisinfuter/1000 - elapsedTime;

                    //resultActivity에 전달할 데이터 : examTitle, 소요시간, shuffledPK, submitAnswer
                    Intent toResultIntent = new Intent(TestActivity.this, ResultActivity.class);
                    toResultIntent.putExtra("examTitle", examTitle);
                    toResultIntent.putExtra("elapsedTime", elapsedTime);
                    toResultIntent.putExtra("shuffledPK", shuffledPK);
                    toResultIntent.putExtra("submitAnswer", submitAnswer);
                    startActivity(toResultIntent);
                }

                finish();
            }
        });
    }

    //시험 중 뒤로가기 버튼 누르면 타이머 종료.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishTimer();
    }

    //카운트 다운 타이머 관련 함수.(카운트 다운 시작) -> android studio에서 제공하는 CountDownTimer 클래스 사용.
    public void startTimer() {
        countDownTimer = new CountDownTimer(millisinfuter, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisUntilFinished = millisUntilFinished / 1000;
                long hour = millisUntilFinished / 3600;
                long minute = (millisUntilFinished-hour*3600) / 60;
                long second = millisUntilFinished - hour*3600 - minute*60;

                if (millisUntilFinished<300)
                    timeLimitTV.setTextColor(Color.rgb(255, 0, 0));
                else
                    timeLimitTV.setTextColor(Color.rgb(0, 0, 0));

                timeLimitTV.setText(String.format("%02d", hour) + ":"
                        + String.format("%02d", minute) + ":" + String.format("%02d", second));

            }

            @Override
            public void onFinish() {
                System.out.println("시험 종료!");
            }
        }.start();
    }

    //카운트 다운 타이머 관련 함수.(카운트 다운 종료) -> android studio에서 제공하는 CountDownTimer 클래스 사용.
    public void finishTimer() {
        countDownTimer.cancel();
    }

    //화면에 문제 보여주는 함수.
    public void changeQuestion(byte[] img, String desc) {
        try { //만약 현재 문제에 대한 사용자가 입력한 답이 존재하면 해당 답을 정답 EditText 부분에 보여줌.
            answerET.setText(submitAnswer.get(currentQ));
        } catch(IndexOutOfBoundsException e) {  //아니면 비어 놓기
            answerET.setText("");
        }

        if (img!=null) {    //문제가 이미지면 ImageView VISIBLE
            questionImgIV.setVisibility(View.VISIBLE);
            questionDescTV.setVisibility(View.GONE);

            byte[] compressedImg = img;
            Bitmap questionBitmap = BitmapFactory
                    .decodeByteArray(compressedImg, 0, compressedImg.length);
            questionImgIV.setImageBitmap(questionBitmap);
        } else {    //문제가 텍스트면 TextView VISIBLE
            questionImgIV.setVisibility(View.GONE);
            questionDescTV.setVisibility(View.VISIBLE);

            String questionDesc = desc;
            questionDescTV.setText(questionDesc);
        }
    }
}
