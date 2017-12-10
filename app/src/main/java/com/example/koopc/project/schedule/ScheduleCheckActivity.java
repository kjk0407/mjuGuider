package com.example.koopc.project.schedule;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.koopc.project.DB.FeedReaderDbHelper;
import com.example.koopc.project.R;

import java.util.ArrayList;
import java.util.HashMap;

// 팝업 액티비티에서 해당 건물에서 듣는 강좌 목록을 불러오기 위한 액티비티
public class ScheduleCheckActivity extends AppCompatActivity {
    Intent intent;
    String buildingName;
    private SQLiteDatabase mDB; // db
    Cursor mCursor; // 커서
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_check);
        intent = this.getIntent();

        // 인텐트의 엑스트라가 널이 아닐 경우 빌딩 이름을 받아온다.
        ListView listView =(ListView)findViewById(R.id.schedule_check_List);
        if(intent.getStringExtra("building_check") != null){
            buildingName = intent.getStringExtra("building_check");
        }
        FeedReaderDbHelper mDBHelper = new FeedReaderDbHelper(this); // 디비 열고
        mDB = mDBHelper.getWritableDatabase();
        mDBHelper.onCreate(mDB);

        // 클래스 디비를 열고 강좌순으로 정렬 후 시간순으로 정렬한다.
        mCursor = mDB.rawQuery("SELECT * FROM class ORDER BY classname ASC, time ASC"  ,null); // 클래스 디비 열자

        mCursor.moveToFirst();
        ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>();
        // 빌딩 이름이 같으면 리스트에 추가함.
        if(mCursor !=null){
            do{
                if(mCursor.getString(7).equals(buildingName)){
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("time",mCursor.getString(6));
                    item.put("classname",mCursor.getString(2)+ " " + mCursor.getString(3));
                    mList.add(item);
                }
            }while(mCursor.moveToNext());
            SimpleAdapter   adapter = new SimpleAdapter(listView.getContext(), mList, android.R.layout.simple_list_item_2,
                    new String[]{"classname","time"}, new int[] {android.R.id.text1,android.R.id.text2});
            listView.setAdapter(adapter);
        }
    }
}
