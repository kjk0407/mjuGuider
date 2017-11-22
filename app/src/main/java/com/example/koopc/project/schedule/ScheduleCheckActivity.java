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
        ListView listView =(ListView)findViewById(R.id.schedule_check_List);
        if(intent.getStringExtra("building_check") != null){
            buildingName = intent.getStringExtra("building_check");
        }
        FeedReaderDbHelper mDBHelper = new FeedReaderDbHelper(this); // 디비 열고
        mDB = mDBHelper.getWritableDatabase();
        mDBHelper.onCreate(mDB);

        mCursor = mDB.rawQuery("SELECT * FROM class ORDER BY classname ASC, time ASC"  ,null); // 클래스 디비 열자

        mCursor.moveToFirst();
        ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>(); //
        if(mCursor !=null){
            do{
                if(mCursor.getString(7).equals(buildingName)){
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("time",mCursor.getString(6));
                    item.put("classname",mCursor.getString(2));
                    mList.add(item);
                }
            }while(mCursor.moveToNext());
            SimpleAdapter   adapter = new SimpleAdapter(listView.getContext(), mList, android.R.layout.simple_list_item_2,
                    new String[]{"classname","time"}, new int[] {android.R.id.text1,android.R.id.text2});
            listView.setAdapter(adapter);
        }
    }
}
