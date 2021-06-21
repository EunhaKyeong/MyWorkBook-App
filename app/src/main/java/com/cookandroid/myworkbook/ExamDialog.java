package com.cookandroid.myworkbook;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamDialog extends Dialog {
    private String mode;
    private ExamHelper examHelper;
    private Context context;
    private int userPK, position;
    private TextView tvTitle;
    private CheckBox timeLimitCB;
    private LinearLayout timeLimitLayout;
    private Button btnAddUpdate, cancelBtn;
    private EditText examNameET, hourET, minuteET, secondET;
    private ArrayList<HashMap<String, Object>> exams = new ArrayList<HashMap<String, Object>>();

    //시험 폴더 추가 다이얼로그
    public ExamDialog(Context context, int userPK, ArrayList<HashMap<String, Object>> exams) {
        super(context);
        this.mode = "ADD";
        this.context = context;
        this.examHelper = new ExamHelper(context);
        this.userPK = userPK;
        this.exams = exams;
    }

    //시험 폴더 수정 다이얼로그
    public ExamDialog(Context context, int userPK, ArrayList<HashMap<String, Object>> exams, int position) {
        super(context);
        this.mode="UPDATE";
        this.context = context;
        this.examHelper = new ExamHelper(context);
        this.userPK = userPK;
        this.exams = exams;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_dialog);

        //다이얼로그 화면 전체화면으로
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = this.getWindow();
        window.setAttributes(lp);

        tvTitle = findViewById(R.id.tvTitle);
        timeLimitCB = findViewById(R.id.timeLimitCB);
        timeLimitLayout = findViewById(R.id.timeLimitLayout);
        btnAddUpdate = findViewById(R.id.btnAddUpdate);
        cancelBtn = findViewById(R.id.cancelBtn);
        examNameET = findViewById(R.id.examNameET);
        hourET = findViewById(R.id.hourET);
        minuteET = findViewById(R.id.minuteET);
        secondET = findViewById(R.id.secondET);

        //시험 폴더 수정 다이얼로그에서 화면 설정.
        if (this.mode=="UPDATE") {
            HashMap<String, Object> exam = this.exams.get(this.position);
            tvTitle.setText("시험 폴더 수정");
            examNameET.setText(exam.get("examTitle").toString());
            if (exam.get("timeLimit")!=null) {
                timeLimitLayout.setVisibility(View.VISIBLE);
                String[] timeLimits = (exam.get("timeLimit").toString()).split(":");
                timeLimitCB.setChecked(true);
                hourET.setText(timeLimits[0]);
                minuteET.setText(timeLimits[1]);
                secondET.setText(timeLimits[2]);
            }
            btnAddUpdate.setText("수정");
        }

        //타이머 설정 버튼을 클릭했을 때 시간을 설정할 수 있는 레이아웃이 보이도록 하는 이벤트함수.
        timeLimitCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (timeLimitCB.isChecked()) {
                    timeLimitLayout.setVisibility(View.VISIBLE);
                } else {
                    timeLimitLayout.setVisibility(View.GONE);
                }
            }
        });

        //추가 버튼을 클릭하면 시험 폴더 생성되는 이벤트 함수.
        btnAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String examTitle = "", timeLimit = null;
                HashMap<String, Object> exam = new HashMap<String, Object>();

                examTitle = examNameET.getText().toString();
                if (timeLimitCB.isChecked()) {
                    String hour, minute, second;
                    hour = hourET.getText().toString();
                    minute = minuteET.getText().toString();
                    second = secondET.getText().toString();
                    timeLimit = hour + ":" + minute + ":" + second;
                }

                if (mode=="ADD") {  //추가
                    long lastinsertId = examHelper.addExam(examTitle, timeLimit, userPK);
                    if (lastinsertId!=-1) {
                        exam.put("examPK", lastinsertId);
                        exam.put("examTitle", examTitle);
                        exam.put("timeLimit", timeLimit);
                        ExamGridAdapter examGridAdapter = new ExamGridAdapter(context, exams);
                        exams.add(exam);
                        examGridAdapter.notifyDataSetChanged();
                        dismiss();
                    } else {
                        Toast.makeText(context, "Insert 실패!", Toast.LENGTH_SHORT).show();
                    }
                } else {    //수정
                    int rowCnt = examHelper.modifyExam(
                            String.valueOf(exams.get(position).get("examPK")), examTitle, timeLimit);
                    if (rowCnt!=0) {
                        HashMap<String, Object> updatedExam = exams.get(position);
                        updatedExam.put("examTitle", examTitle);
                        updatedExam.put("timeLimit", timeLimit);
                        ExamGridAdapter examGridAdapter = new ExamGridAdapter(context, exams);
                        examGridAdapter.notifyDataSetChanged();
                        dismiss();
                    } else {
                        Toast.makeText(context, "Insert 실패!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //취소 버튼을 클릭하면 다이얼로그가 종료되는 이벤트 함수.
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
