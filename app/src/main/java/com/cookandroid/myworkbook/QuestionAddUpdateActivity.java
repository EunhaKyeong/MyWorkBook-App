package com.cookandroid.myworkbook;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class QuestionAddUpdateActivity extends Activity {
    final static int TAKE_PICTURE = 1;

    private QuestionHelper db;
    private String examPK, mode;
    private int position = -1;
    HashMap<String, Object> question;
    private Button btnImage, btnAddUpdate, cancelBtn;
    private TextView tvTitle;
    private EditText etQTitle, etQContent, etQAnswer;
    private ImageView ivQContent;
    private Bitmap questionBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_add_update_activity);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(QuestionAddUpdateActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

        db = new QuestionHelper(getApplicationContext());
        btnImage = findViewById(R.id.btnImage);
        btnAddUpdate = findViewById(R.id.btnAddUpdate);
        cancelBtn = findViewById(R.id.cancelBtn);
        tvTitle = findViewById(R.id.tvTitle);
        etQTitle = findViewById(R.id.etQTitle);
        etQContent = findViewById(R.id.etQContent);
        etQAnswer = findViewById(R.id.etQAnswer);
        ivQContent = findViewById(R.id.ivQContent);

        //?????? ???????????? ???????????? ?????????
        Intent intent = getIntent();
        if (intent.getStringExtra("mode").equals("CREATE")) {   //?????? ?????? ????????????
            this.mode = "CREATE";
            this.examPK = intent.getStringExtra("examPK");
        } else {    //?????? ?????? ????????????
            this.mode = "UPDATE";
            this.question = (HashMap<String, Object>) intent.getSerializableExtra("question");
            this.position = intent.getIntExtra("position", -1);
            tvTitle.setText("?????? ????????????");
            etQTitle.setText(question.get("questionTitle").toString());
            etQAnswer.setText(question.get("answer").toString());
            if (question.get("questionImg")==null) {
                etQContent.setText(question.get("questionDesc").toString());
            } else {
                byte[] compressedImg = (byte[]) question.get("questionImg");
                questionBitmap = BitmapFactory
                        .decodeByteArray(compressedImg, 0, compressedImg.length);
                ivQContent.setImageBitmap(questionBitmap);
                ivQContent.setVisibility(View.VISIBLE);
                ivQContent.setEnabled(true);
                etQContent.setVisibility(View.GONE);
                etQContent.setEnabled(false);
            }
            btnAddUpdate.setText("????????????");
        }

        //????????? ???????????? ????????? ????????? ????????? ????????? ??????.
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });

        //?????? or ???????????? ????????? ????????? ???
        btnAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title, content="", answer;
                byte[] questionImg = null;

                title = etQTitle.getText().toString();
                if (etQContent.isEnabled()) {   //????????? ????????? ????????????
                    content = etQContent.getText().toString();
                } else {    //????????? ????????? ????????????
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    questionBitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
                    questionImg = stream.toByteArray();
                }
                answer = etQAnswer.getText().toString();

                String questionPK = "";
                boolean result = true;

                switch (mode) {
                    case "CREATE":  //?????? ????????? ??????
                        long insertedPK = db.addQuestion(title, questionImg, content, answer, examPK);
                        if (insertedPK>=0) {
                            questionPK = String.valueOf(insertedPK);
                        } else {
                            result = false;
                        }
                        break;

                    case "UPDATE":  //?????? ????????? ??????
                        int updateResult = db.modifyQuestion(question.get("questionPK").toString(), title, questionImg, content, answer);
                        if (updateResult!=0) {
                            questionPK = question.get("questionPK").toString();
                        } else {
                            result = false;
                        }
                        break;
                }

                //DB??? Insert or Update ??? ????????? ?????? ?????? ?????? ????????? ????????? ????????????
                if (result) {   //DB??? Insert or Update??? ??????????????? ????????? ??????.
                    Intent outIntent =
                            new Intent(QuestionAddUpdateActivity.this, QuestionActivity.class);
                    outIntent.putExtra("questionPK", questionPK);
                    outIntent.putExtra("position", position);
                    outIntent.putExtra("questionTitle", title);
                    outIntent.putExtra("questionImg", questionImg);
                    outIntent.putExtra("questionDesc", content);
                    outIntent.putExtra("answer", answer);
                    setResult(RESULT_OK, outIntent);
                    Toast.makeText(QuestionAddUpdateActivity.this,
                            "??????!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {    //DB??? Insert or Update ??? ????????? ???????????? ??????.
                    Toast.makeText(QuestionAddUpdateActivity.this,
                            "????????? ??????????????????. ?????? ??? ??? ??????????????????.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //?????? ????????? ????????? ?????? ?????? ???????????? ??????.
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //???????????? ????????? ????????? ????????? ??????????????? ?????????
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
            case TAKE_PICTURE:
                if (resultCode==RESULT_OK&&intent.hasExtra("data")) {
                    questionBitmap = (Bitmap) intent.getExtras().get("data");
                    EditText etQContent = findViewById(R.id.etQContent);
                    ImageView ivQContent = findViewById(R.id.ivQContent);

                    if (questionBitmap!=null) {
                        etQContent.setVisibility(View.GONE);
                        etQContent.setEnabled(false);
                        ivQContent.setImageBitmap(questionBitmap);
                        ivQContent.setVisibility(View.VISIBLE);
                        ivQContent.setEnabled(true);
                    } else {
                        etQContent.setVisibility(View.VISIBLE);
                        etQContent.setEnabled(true);
                        ivQContent.setVisibility(View.GONE);
                        ivQContent.setEnabled(false);
                    }
                }
                break;
        }
    }
}
