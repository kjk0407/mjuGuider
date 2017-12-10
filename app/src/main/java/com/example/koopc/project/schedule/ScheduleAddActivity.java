package com.example.koopc.project.schedule;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.koopc.project.DB.FeedReaderDbHelper;
import com.example.koopc.project.R;

import java.util.ArrayList;

public class ScheduleAddActivity extends AppCompatActivity {
    private SQLiteDatabase mDB;
    GridView GridSchedule;
    ScheduleAdapter adapter;
    ArrayList<String> set = new ArrayList<String>(); // 클릭한 그리드 칸들의 position을 저장할 리스트

    Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add);

    }

    @Override
    protected void onStart() {
        super.onStart();
        GridSchedule = (GridView)findViewById(R.id.schedule);

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        mDB = mDbHelper.getWritableDatabase();
        mDbHelper.onCreate(mDB);
        adapter = new ScheduleAdapter(this);
        mCursor = mDB.rawQuery("SELECT grid, permission  FROM class",null); // 스케쥴 액티비티와 마찬가지로 그리드와 퍼미션만 부름
        GridSchedule.setAdapter(adapter);
        GridSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCursor.moveToFirst();
                String permission = "1"; // 퍼미션은 그냥 1로 초기화

                if(mCursor !=null){ // 커서가 널이 아니면
                    do {
                        if(mCursor.getString(0).equals(String.valueOf(position))){ // 클릭한 칸과 디비의 칸이 같은애를 불러다가
                            permission = mCursor.getString(1); // 변수 permission에 디비에서 부른 퍼미션 값을 추가
                        }
                    }while(mCursor.moveToNext());

                    if(permission.equals("0")){ // 디비안에 어떤 정보도 없는 경우 // grid는 제외
                        if(set.contains(String.valueOf(position))) { // 포지션 셋안에 포지션이 존재하면 이미 클릭되있다는 것이니까
                            set.remove(String.valueOf(position)); // 세트에서 포지션 지우고
                            TextView tv = (TextView)view;
                            tv.setBackgroundColor(Color.parseColor("#FFFFFF")); // 색깔 흰색으로

                        }else{ // 텍스트뷰 배경색이 흰색인 애를 클릭
                            TextView tv = (TextView)view;
                            tv.setBackgroundColor(Color.parseColor("#BC8F8F")); // 배경색 바꾸고
                            set.add(String.valueOf(position)); // 세트에 포지션 넣기
                        }
                    }
                }
            }
        });

        //캔슬 버튼을 누를 경우
        findViewById(R.id.cancle_btn).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //추가를 누를 경우
        findViewById(R.id.add_btn).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSubActivity = new Intent(ScheduleAddActivity.this,Add_subjectActivity.class); // add 액티비티로
                intentSubActivity.putStringArrayListExtra("positionSet",set); // 포지션 저장해놓은거 넘기고
                startActivity(intentSubActivity); // 시작
                finish();
            }
        });
    }
}
