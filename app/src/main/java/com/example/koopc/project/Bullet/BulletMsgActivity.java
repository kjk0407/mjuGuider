package com.example.koopc.project.Bullet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koopc.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BulletMsgActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference mBulletRef = FirebaseDatabase.getInstance().getReference().child("bulletin");
    DatabaseReference mCurrentRef = null;
    int subnumCount = 0;
    private BulletDTO dto;

    private TextView nameView;
    private TextView contentView;
    private TextView dateView;
    private TextView goodnumView;
    private TextView subnumView;
    private TextView writerView;
    private TextView viewnumView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bullet_msg);

        nameView = (TextView) findViewById(R.id.msg_nameText);//1층
        writerView = (TextView) findViewById(R.id.msg_writerTxt); //2층
        dateView = (TextView) findViewById(R.id.msg_dateTxt);
        viewnumView = (TextView) findViewById(R.id.msg_viewnumTxt);//3층
        goodnumView = (TextView) findViewById(R.id.msg_goodnumTxt);
        subnumView = (TextView) findViewById(R.id.msg_subnumTxt);
        contentView = (TextView) findViewById(R.id.msg_contentText);//4층

        Bundle bundle = getIntent().getExtras();
        dto = bundle.getParcelable("DTO");

        nameView.setText(dto.getMsgTitle());
        dateView.setText("작성 날짜 : " + dto.getMsgDate()); // 현재 시간
        writerView.setText("작성자 : " + dto.getWriter()); // null 이면 익명이고, 로그인하면 아이디 프로필에 사진 올라오게끔 하자
        viewnumView.setText("조회수 : " + dto.getMsgViewNum()); // 백 버튼 누르면 조회수 업 되게 설정
        goodnumView.setText("추천수 : " + dto.getMsgGoodNum()); // 아이디 로그인 시만 가능하도록 설정. ( 코드의 어려움 )


        mCurrentRef = mBulletRef.child(dto.getSubMsg()).child("msgSubMsg"); // 내 댓글에 대한 서브.

//        Toast.makeText(this, mBulletRef.child(dto.getSubMsg()).child("msgSubMsg").toString(), Toast.LENGTH_LONG).show();
        mCurrentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Toast.makeText(getApplicationContext() ,ds.toString(), Toast.LENGTH_LONG).show();
                    subnumCount = subnumCount + 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        subnumView.setText("댓글 수 : " + String.valueOf(subnumCount));
        contentView.setText(dto.getMsgContent());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.msg_backButton :
                break;
            case R.id.msg_goodButton :
                break;
            case R.id.msg_subviewButton :
//
                break;
            default:
                break;
        }
    }
}
