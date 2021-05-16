package com.cookandroid.myworkbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button goLoginBtn;
    private LoginDialog loginDialog;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginDialog = new LoginDialog(MainActivity.this);

        //상단바 제거
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //로그인하기 버튼
        goLoginBtn = findViewById(R.id.goLoginBtn);
        goLoginBtn.setBackgroundColor(Color.rgb(249, 200, 2));

        //로그인하기 버튼을 클릭하면 로그인 다이얼로그가 나타나는 이벤트.
        goLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.show();
            }
        });
    }

    public void changeIsLogin() {
        this.isLogin = true;
    }
}