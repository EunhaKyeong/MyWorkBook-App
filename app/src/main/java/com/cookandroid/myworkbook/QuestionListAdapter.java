package com.cookandroid.myworkbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionListAdapter extends BaseAdapter {
    private static final int LAYOUT_RESOURCE_ID = R.layout.question_list_item;
    private Context context;
    ArrayList<HashMap<String, Object>> questions;

    public QuestionListAdapter(Context context, ArrayList<HashMap<String, Object>> questions) {
        this.context = context;
        this.questions = questions;
    }

    @Override
    public int getCount() {
        return questions.size();
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
    public View getView(int position , View view, ViewGroup viewGroup) {
        QuestionListViewHolder questionListViewHolder;
        HashMap<String, Object> question = questions.get(position);

        if (view==null) {
            view = LayoutInflater.from(this.context)
                    .inflate(LAYOUT_RESOURCE_ID, viewGroup, false);

            questionListViewHolder = new QuestionListViewHolder(view);
            view.setTag(questionListViewHolder);
        } else {
            questionListViewHolder = (QuestionListViewHolder) view.getTag();
        }

        questionListViewHolder.tvQNo.setText(String.valueOf(position+1));
        questionListViewHolder.tvQTitle.setText(question.get("questionTitle").toString());
        questionListViewHolder.tvQAnswer.setText(question.get("answer").toString());

        questionListViewHolder.tvQTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                QuestionHelper db = new QuestionHelper(context);

                //Popup menu 생성
                PopupMenu popup = new PopupMenu(context, view);
                popup.getMenuInflater().inflate(R.menu.update_delete_menu, popup.getMenu());

                //popup click 이벤트(삭제, 수정)
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete:   //삭제
                                String questionPK = questions.get(position).get("questionPK").toString();
                                if (db.removeQuestion(questionPK)!=0) {
                                    questions.remove(position);
                                    notifyDataSetChanged();
                                }
                                break;
                            case R.id.update:   //수정
                                Intent intent = new Intent(context, QuestionAddUpdateActivity.class);
                                intent.putExtra("mode", "UPDATE");
                                intent.putExtra("question", questions.get(position));
                                ((Activity) context).startActivityForResult(intent, 1);
                        }

                        return false;
                    }
                });
                //Popup 보이기
                popup.show();

                return true;
            }
        });

        return view;
    }


}
