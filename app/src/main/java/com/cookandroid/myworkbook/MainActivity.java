package com.cookandroid.myworkbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button goLoginBtn;
    private Dialog loginDialog;
    private SQLiteDatabase sqlDB;

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

        MyDBHelper myHelper = new MyDBHelper(this);
        sqlDB = myHelper.getWritableDatabase();

        goLoginBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Cursor cursor = sqlDB.rawQuery("SELECT * FROM user;", null);
                while(cursor.moveToNext()){
                    System.out.println(cursor.getString(1));
                }

                showLoginDialog();

                return false;
            }
        });
    }

    public class MyDBHelper extends SQLiteOpenHelper {
        public MyDBHelper(Context context) {
            super(context, "myWorkBookDB.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) { }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    }

    public void showLoginDialog() {
        Button loginBtn = loginDialog.findViewById(R.id.loginBtn);
        Button signupBtn = loginDialog.findViewById(R.id.signupBtn);

        loginBtn.setBackgroundColor(Color.rgb(249, 200, 2));
        signupBtn.setBackgroundColor(Color.rgb(249, 200, 2));

        loginDialog.show();
    }
}