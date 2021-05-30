package com.cookandroid.myworkbook;

import android.view.View;
import android.widget.TextView;

public class QuestionListViewHolder {
    public TextView tvQNo, tvQTitle, tvQAnswer;

    public QuestionListViewHolder(View view) {
        tvQNo = view.findViewById(R.id.tvQNo);
        tvQTitle = view.findViewById(R.id.tvQTitle);
        tvQAnswer = view.findViewById(R.id.tvQAnswer);
    }
}
