package com.cookandroid.myworkbook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamGridAdapter extends BaseAdapter {
    private static final int LAYOUT_RESOURCE_ID = R.layout.exam_grid_item;
    private AdapterView.OnItemLongClickListener btnItemLongClickListener;
    private Context context;
    private ArrayList<HashMap<String, Object>> exams;
    private ExamHelper dbHelper;
    private int userPK;

    public ExamGridAdapter(Context context, ArrayList<HashMap<String, Object>> exams) {
        this.context = context;
        this.exams = exams;
        this.dbHelper = new ExamHelper(context);
    }

    public ExamGridAdapter(Context context, ArrayList<HashMap<String, Object>> exams, int userPK) {
        this.context = context;
        this.exams = exams;
        this.dbHelper = new ExamHelper(context);
        this.userPK = userPK;
    }

    @Override
    public int getCount() {
        return exams.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ExamGridViewHolder viewHolder;
        if (convertView==null) {
            convertView = LayoutInflater.from(this.context).inflate(LAYOUT_RESOURCE_ID, parent, false);

            viewHolder = new ExamGridViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ExamGridViewHolder) convertView.getTag();
        }

        final HashMap<String, Object> exam = exams.get(position);
        viewHolder.btnExam.setText(exam.get("examTitle").toString());
        viewHolder.tvId.setText(exam.get("examPK").toString());
        viewHolder.tvTimeLimit.setText(exam.get("examPK").toString());

        //?????? ????????? ?????? ????????? ?????? -> ??????/?????? ?????? ???????????????!
        viewHolder.btnExam.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println(viewHolder.btnExam.getText().toString());
                //Popup menu ??????
                PopupMenu popup = new PopupMenu(context, view);
                popup.getMenuInflater().inflate(R.menu.update_delete_menu, popup.getMenu());

                //popup click ?????????(??????, ??????)
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String examPK = "";
                        switch (menuItem.getItemId()) {
                            case R.id.delete:   //??????
                                examPK = exams.get(position).get("examPK").toString();
                                if (dbHelper.removeExam(examPK)!=0) {
                                    exams.remove(position);
                                    notifyDataSetChanged();
                                }
                                break;
                            case R.id.update:   //??????
                                ExamDialog updateExamDialog = new ExamDialog(context, userPK, exams, position);
                                updateExamDialog.show();
                        }
                        return false;
                    }
                });
                //Popup ?????????
                popup.show();

                return true;
            }
        });

        viewHolder.btnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("examPK", exams.get(position).get("examPK").toString());
                intent.putExtra("examTitle", exams.get(position).get("examTitle").toString());
                try {
                    intent.putExtra("timeLimit", exams.get(position).get("timeLimit").toString());
                } catch(NullPointerException e) {
                    intent.putExtra("timeLimit", "");
                }

                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void setButtonItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        btnItemLongClickListener = listener;
    }
}
