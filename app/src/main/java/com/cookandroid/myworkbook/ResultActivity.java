package com.cookandroid.myworkbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class ResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        Intent intent = getIntent();
        String examTitle = intent.getStringExtra("examTitle");
        int elapsedTime = intent.getIntExtra("elapsedTime", 0);
        ArrayList<Integer> shuffledPK = intent.getIntegerArrayListExtra("shuffledPK");
        ArrayList<String> submitAnswer = intent.getStringArrayListExtra("submitAnswer");
    }
}
