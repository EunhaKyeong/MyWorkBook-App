package com.cookandroid.myworkbook;

import android.view.View;
import android.widget.TextView;

public class ResultListViewHolder {
    TextView questionNoTV, questionTitleTV, submitAnswerTV, answerTV;

    public ResultListViewHolder(View view) {
        questionNoTV = view.findViewById(R.id.questionNoTV);
        questionTitleTV = view.findViewById(R.id.questionTitleTV);
        submitAnswerTV = view.findViewById(R.id.submitAnswerTV);
        answerTV = view.findViewById(R.id.answerTV);
    }
}
