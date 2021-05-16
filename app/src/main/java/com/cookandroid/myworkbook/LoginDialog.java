package com.cookandroid.myworkbook;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class LoginDialog extends Dialog {
    private Context context;
    private Button loginBtn, signupBtn;
    private MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    private EditText idEdit, pwdEdit;

    public LoginDialog(@NonNull MainActivity context) {
        super(context);
        this.context = context;
    }

    //DB 연동
    public static class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context c) {
            super(c, "myWorkBookDB.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) { }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = this.getWindow();
        window.setAttributes(lp);

        loginBtn = findViewById(R.id.loginBtn);
        signupBtn= findViewById(R.id.signupBtn);

        loginBtn.setBackgroundColor(Color.rgb(249, 200, 2));
        signupBtn.setBackgroundColor(Color.rgb(249, 200, 2));

        loginBtn.setOnClickListener(loginClickListener);
    }

    private View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //DB 설정
            myHelper = new MyDBHelper(v.getContext());
            sqlDB = myHelper.getWritableDatabase();

            idEdit = findViewById(R.id.idEdit);
            pwdEdit = findViewById(R.id.pwdEdit);
            String strId = idEdit.getText().toString();
            String strPwd = pwdEdit.getText().toString();

            //일치하는 회원 정보가 있는지 확인
            String query = "SELECT * FROM user WHERE userID='" + strId +
                    "' AND password='" + strPwd + "';";
            Cursor cursor = sqlDB.rawQuery(query, null);

            if (cursor.getCount()!=0) {
                System.out.println("로그인 됐습니다.");
                idEdit.setText("");
                pwdEdit.setText("");
                cursor.moveToFirst();
                dismiss();

                Intent intent = new Intent(context, ExamActivity.class);
                intent.putExtra("userPK", cursor.getInt(0));
                intent.putExtra("nickname", cursor.getString(3));
                context.startActivity(intent);
            } else {
                System.out.println("아이디와 비밀번호를 다시 한 번 확인해주세요.");
            }

            sqlDB.close();
        }
    };

}
