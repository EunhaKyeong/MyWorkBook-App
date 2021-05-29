package com.cookandroid.myworkbook;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExamGridViewHolder {
    public Button btnExam;
    public TextView tvId, tvTimeLimit;

    public ExamGridViewHolder(View a_view) {
        this.btnExam = a_view.findViewById(R.id.btnExam);
        this.tvId = a_view.findViewById(R.id.tvId);
        this.tvTimeLimit = a_view.findViewById(R.id.tvTimeLimit);
    }
}
