package com.example.koopc.project.schedule;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.koopc.project.FeedReaderDbHelper;
import com.example.koopc.project.R;

public class ScheduleActivity extends AppCompatActivity {
    private SQLiteDatabase mDB;
    GridView GridSchedule;
    ScheduleAdapter adapter;
    int pos;
    Intent intent;
    private Cursor mCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.schedule_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId() ) {
            case R.id.addSubject: // 강좌 추가
                Intent intent = new Intent(ScheduleActivity.this, ScheduleAddActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        GridSchedule = (GridView)findViewById(R.id.schedule);

        intent = new Intent(this, ScheduleAddActivity.class);
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        mDB = mDbHelper.getWritableDatabase();
        mDbHelper.onCreate(mDB);
        adapter = new ScheduleAdapter(this);
        GridSchedule.setAdapter(adapter);
        mCursor = mDB.rawQuery("SELECT grid, permission  FROM class",null); // 필요한 grid랑 퍼미션만 받아옴.
        GridSchedule.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mCursor.moveToFirst();
                String permission = "0";
                if(mCursor !=null) {
                    do {
                        if (mCursor.getString(0).equals(String.valueOf(position))) { // 길게 클릭한 칸이랑 디비의 포지션을 비교해서 같으면
                            permission = mCursor.getString(1); // 전역변수 퍼미션에 디비에 저장된 퍼미션을 삽입
                        }
                    } while (mCursor.moveToNext());

                }
                if(permission.equals("1")) { // 퍼미션이 1이면 안에 데이터가 있다는 뜻이므로
                    Intent intent = new Intent(ScheduleActivity.this, ScheduleUpdateActivity.class); // 업데이트 액티비티로 이동 가능
                    intent.putExtra("gridPos", position);
                    startActivity(intent);
                }
                return true;}
        });
    }
}
