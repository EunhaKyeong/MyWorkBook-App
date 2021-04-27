package com.cookandroid.myworkbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button goLoginBtn;
    private Dialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        goLoginBtn = findViewById(R.id.goLoginBtn);
        goLoginBtn.setBackgroundColor(Color.rgb(249, 200, 2));

        loginDialog = new Dialog(MainActivity.this);
        loginDialog.setContentView(R.layout.login);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(loginDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = loginDialog.getWindow();
        window.setAttributes(lp);

        goLoginBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showLoginDialog();

                return false;
            }
        });
    }

    public void showLoginDialog() {
        Button loginBtn = loginDialog.findViewById(R.id.loginBtn);
        Button signupBtn = loginDialog.findViewById(R.id.signupBtn);

        loginBtn.setBackgroundColor(Color.rgb(249, 200, 2));
        signupBtn.setBackgroundColor(Color.rgb(249, 200, 2));

        loginDialog.show();
    }
}