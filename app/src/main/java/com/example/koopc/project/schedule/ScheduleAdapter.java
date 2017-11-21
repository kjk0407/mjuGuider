package com.example.koopc.project.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.koopc.project.FeedReaderDbHelper;

/**
 * Created by vkdlv on 2017-11-22.
 */

public class ScheduleAdapter extends BaseAdapter {
    private SQLiteDatabase mDB; // db
    Cursor mCursor; // 커서
    Context mContext; // 컨텍스트
    int count = 54; // 그리드 칸수
    int flag = 0; // move to next가 있는지 없는지 확인하기 위한 flag
    int timeflag = 0; //  몇교시인지 넣기 위한 플래그

    String[] mWeekTitleIds ={
            "시간",
            "월",
            "화",
            "수",
            "목",
            "금"
    };
    String[] mTimeTitle=
            {
                    "1교시(9:00~9:50)",
                    "2교시(10:00~10:50)",
                    "3교시(11:00~11:50)",
                    "4교시(12:00~12:50)",
                    "5교시(13:00~13:50)",
                    "6교시(14:00~14:50)",
                    "7교시(15:00~15:50)",
                    "8교시(16:00~16:50)"
            };

    ScheduleAdapter (Context context){
        mContext = context; // 컨텍스트 받고
        FeedReaderDbHelper mDBHelper = new FeedReaderDbHelper(context); // 디비 열고
        mDB = mDBHelper.getWritableDatabase();
        mDBHelper.onCreate(mDB);
        mCursor = mDB.rawQuery("SELECT * FROM class",null); // 클래스 디비 열자

        if(mCursor.moveToFirst()) flag = 1; // 커서 무브투 퍼스트로 움직이고 플래그 1로 // 참고로 0이 다음 db가 없다는 뜻
        // 처음으로 갔는데 얘가 있으면 flag 1로 초기화
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null; // 뷰를 널로

        if(convertView == null) // 메인 뷰가 없으면
        {
            v = new TextView(mContext); // 텍스트 뷰 받고

            v.setBackgroundColor(Color.GRAY);
            v.setLayoutParams(new GridView.LayoutParams( 60,60 )); // 솔직히 이부분 진석이가 한거라 잘 몰라염 해햏

        }else{
            if (position < 6) { // 포지션이 그리드 칸 번호 --> 0~5까지는 요일
                v = new TextView(mContext);

                ((TextView)v).setGravity(Gravity.CENTER); // 가운데 배열
                ((TextView)v).setText(mWeekTitleIds[position]); // 요일 배열 넣기
                v.setBackgroundColor(Color.parseColor("#bfd5fc")); // 배경색
                ((TextView) v).setHeight(250); // 셀크기
                v.setLongClickable(false);
            }
            else if (position >= 6 && position < count && position % 6 != 0 ) { // 포지션이 6보다 크고 카운트보다 작아야함. --> 카운트보다 크면 칸보다 많아져서 에러
                if(mCursor !=null){ // 커서가 널이 아니면
                    if(flag == 1){ // 커서를 스타트로 보냈는데 얘가 존재하면 1로 초기화 했었음 위에서
                        String s  = ""; // 스트링 선언하고
                        v = new TextView(mContext);
                        ((TextView) v).setHeight(250);
                        ((TextView)v).setGravity(Gravity.CENTER);

                        s = s + mCursor.getString(2) +mCursor.getString(6); // 각 디비의 강좌 이름을 받는다.
                        v.setBackgroundColor(Color.parseColor(mCursor.getString(5))); // 디비 안에 색상 디비도 존재
                        ((TextView) v).setText(s); // 텍스트뷰에 넣기
                        if(mCursor.moveToNext()){ // 다음으로 이동  --> 있으면 그냥 flag1
                            flag = 1;
                        }else flag = 0; // 안되면 flag 0으로 세팅
                    }else { // db 없으면 생성함.
                        v = new TextView(mContext); // 텍스트 뷰 생성
                        ((TextView)v).setGravity(Gravity.CENTER); // 가운데 정렬
                        ContentValues c = new ContentValues(); // 컨텐트 벨류
                        c.put("grid", position); // 몇번째 그리드 칸인지 db에 추가
                        c.put("classname"," "); // 강좌명을 비우고
                        c.put("location"," "); // 강의실도 비우고
                        c.put("color","#FFFFFF"); // 색은 흰색으로
                        c.put("permission","0"); // 퍼미션은 add되서 각 강좌명등이 입력되면 1로 두고 그냥 빈칸이면 0으로 함.
                        c.put("time"," ");// 시간
                        c.put("building", " ");
                        mDB.insert("class",null,c); // 인서트 한다.
                        ((TextView)v).setText(" " ); // 텍스트 뷰의 내용을 비우고
                        v.setBackgroundColor(Color.parseColor("#FFFFFF")); // 텍스트 뷰의 색을 추가
                        ((TextView) v).setHeight(250); // 크기 250으로 하고
                    }
                }
            }else if (position >= 6 && position < count && position % 6 == 0 ) { // 6의 배수의 칸에 교시 정보가 들어가는것에 주목해서 나머지가 0이면 교시 추가

                v = new TextView(mContext);

                ((TextView)v).setGravity(Gravity.CENTER);
                ((TextView)v).setText(mTimeTitle[timeflag]); // 타임 플래그가 여기서 쓰이죠
                ((TextView)v).setBackgroundColor(Color.parseColor("#fcbfe3"));
                ((TextView) v).setHeight(250);
                v.setClickable(false);
                if(timeflag < 7) { // 6까지만 추가해주고
                    timeflag = timeflag + 1;
                }

            }else {
                v = convertView; // 지금까지 만든 v를 리턴하여 그리드에 추가
            }
        }
        return v;
    }
}
