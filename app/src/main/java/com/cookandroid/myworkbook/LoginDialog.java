package com.cookandroid.myworkbook;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginDialog extends Dialog {
    private Context context;
    private Button loginBtn, signupBtn;
    private UserHelper dbHelper;
    private EditText idEdit, pwdEdit;

    public LoginDialog(MainActivity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog);

        //DB 설정
        dbHelper = new UserHelper(this.getContext());

        //다이얼로그 화면 전체화면으로
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
            idEdit = findViewById(R.id.idEdit);
            pwdEdit = findViewById(R.id.pwdEdit);
            String strId = idEdit.getText().toString();
            String strPwd = pwdEdit.getText().toString();

            //일치하는 회원 정보가 있는지 확인
            Cursor cursor = dbHelper.getUser(strId, strPwd);
            if (cursor.getCount()!=0) {
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
        }
    };

}
