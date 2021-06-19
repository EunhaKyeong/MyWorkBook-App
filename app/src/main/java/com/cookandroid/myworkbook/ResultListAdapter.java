package com.cookandroid.myworkbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultListAdapter extends BaseAdapter {
    private static final int LAYOUT_RESOURCE_ID = R.layout.result_list_item;
    private Context context;
    ArrayList<Integer> shuffledPKs;
    ArrayList<String> submitAnswers;
    HashMap<Integer, HashMap<String, String>> TitleAnswers;

    public ResultListAdapter(Context context, ArrayList<Integer> shuffledPKs,
                             ArrayList<String> submitAnswers,
                             HashMap<Integer, HashMap<String, String>> TitleAnswers) {
        this.context = context;
        this.shuffledPKs = shuffledPKs;
        this.submitAnswers = submitAnswers;
        this.TitleAnswers = TitleAnswers;
    }

    @Override
    public int getCount() {
        return shuffledPKs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ResultListViewHolder resultListViewHolder;

        if (view==null) {
            view = LayoutInflater.from(this.context)
                    .inflate(LAYOUT_RESOURCE_ID, viewGroup, false);

            resultListViewHolder = new ResultListViewHolder(view);
            view.setTag(resultListViewHolder);
        } else {
            resultListViewHolder = (ResultListViewHolder) view.getTag();
        }

        int questionPK = shuffledPKs.get(position);
        resultListViewHolder.questionNoTV.setText(String.valueOf(position+1));
        HashMap<String, String> titleAnswer = TitleAnswers.get(questionPK);
        resultListViewHolder.questionTitleTV.setText(titleAnswer.get("title"));
        resultListViewHolder.answerTV.setText(titleAnswer.get("answer"));
        resultListViewHolder.submitAnswerTV.setText(submitAnswers.get(position));

        return view;
    }
}
