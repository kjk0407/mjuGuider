package com.example.koopc.project.Bullet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.koopc.project.R;

public class BulletMsgAddActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText addTitleView;
    private EditText addContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bullet_msgadd);

        addTitleView = (EditText) findViewById(R.id.bullet_msgAddTitle);
        addContentView = (EditText) findViewById(R.id.bullet_msgAddContent);

        Button msgAddButton = (Button) findViewById(R.id.msg_addButton);
        msgAddButton.setOnClickListener(this);

        Button msgCancelButton = (Button) findViewById(R.id.msg_cancelButton);
        msgCancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.msg_addButton:
                intent.putExtra("sendTitle", addTitleView.getText().toString());
                intent.putExtra("sendContent", addContentView.getText().toString());

                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.msg_cancelButton:
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                break;
        }
    }
}
