package com.example.koopc.project.Bullet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koopc.project.R;

public class BulletMsgActivity extends AppCompatActivity {
    private TextView nameView;
    private TextView contentView;
    private EditText addTitleView;
    private EditText addContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // watchMode 일 경우
        if(this.getIntent().getStringExtra("type").toString().equals("watchMode")) {
            setContentView(R.layout.activity_bullet_msg);

            nameView = (TextView) findViewById(R.id.msg_nameText);
            contentView = (TextView) findViewById(R.id.msg_contentText);

            nameView.setText(this.getIntent().getStringExtra("name").toString());
            contentView.setText(this.getIntent().getStringExtra("content").toString());

            //addMode일 경우
        } else if(this.getIntent().getStringExtra("type").toString().equals("addMode")) {
            setContentView(R.layout.activity_bullet_msgadd);

            addTitleView = (EditText) findViewById(R.id.bullet_msgAddTitle);
            addContentView = (EditText) findViewById(R.id.bullet_msgAddContent);
        } else {
            // 오류 메세지를 출력하고 비정상 종료 할것.
        }
    }

    // 메세지 추가하기
    public void bullet_msgAddButton(View view) {
        Intent intent = new Intent();
        intent.putExtra("sendTitle", addTitleView.getText().toString());
        intent.putExtra("sendContent", addContentView.getText().toString());

        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    // 취소 버튼
    public void bullet_msgCancelButton(View view) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);

        finish();
    }
}
