package com.example.koopc.project.schedule;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.koopc.project.DB.FeedReaderDbHelper;
import com.example.koopc.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Add_subjectActivity extends AppCompatActivity {

    private int tb1,tb2,tb3,tb4,tb5,tb6,tb7,tb8,tb9,tb10; // 토글 버튼
    String classColor = "#febfbf"; // 클래스 컬러
    ArrayList<String> set = null; // 포지션 셋
    String subjectName = null; // 과목 명
    String building = null;

    private SQLiteDatabase mDB; // 데이터 베이스

    Spinner spinner; // 화면에 표시할 스피너

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); // 참조 데이터베이스 선언 ( 그냥 선언시 루트 베이스에서 찾는다. )
    DatabaseReference buildingNameRef = mRootRef.child("building"); // 참조 데이터베이스 내 차일드 값 받기.

    String[] mWeekTitleIds ={
            "월요일",
            "화요일",
            "수요일",
            "목요일",
            "금요일"
    };

    String[] mTimeTitle= {
            "1교시",
            "2교시",
            "3교시",
            "4교시",
            "5교시",
            "6교시",
            "7교시",
            "8교시"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        Intent intent = this.getIntent();
        spinner = (Spinner)findViewById(R.id.class_add_spinner);
        final List<String> spinner_items = new ArrayList<>();
        final ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinner_items);

        if(intent.getStringArrayListExtra("positionSet")!=null){ // add쪽에서 클릭된 그리드 칸들의 리스트 받음
            set = intent.getStringArrayListExtra("positionSet");
        }
        if(intent.getStringExtra("subjectName")!=null){ // 업데이트에서 보낸 서브젝트 이름을 받음 요 if문들이 add인지 update인지 구분해주는 것임.
            subjectName = intent.getStringExtra("subjectName");
        }
        FeedReaderDbHelper mDBHelper = new FeedReaderDbHelper(this);
        mDB = mDBHelper.getWritableDatabase();
        buildingNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    spinner_items.add(ds.child("buildingName").getValue().toString());
                }
                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinner_adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //스피너 클릭 리스너
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                building = parent.getItemAtPosition(position).toString();//빌딩 이름 넣기
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 닫기 눌렀을 때
        findViewById(R.id.class_add_cancle).setOnClickListener (new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // 확인 눌렀을 때
        findViewById(R.id.class_add_ok).setOnClickListener ( new Button.OnClickListener() {
            public void onClick(View v) {

                String className;
                String classNumber;
                String professor;

                EditText editText1 ;
                //edit text의 텍스트 받아오기
                editText1 = (EditText)findViewById(R.id.class_add_classname_show);
                className = editText1.getText().toString();

                editText1 = (EditText)findViewById(R.id.class_add_classnumber_show);
                classNumber = editText1.getText().toString();

                editText1 = (EditText)findViewById(R.id.class_add_professor_show) ;
                professor = editText1.getText().toString();

                //값들 넣기
                ContentValues c = new ContentValues();
                c.put("classname",className);
                c.put("location",classNumber);
                c.put("professor",professor);
                c.put("color",classColor);
                c.put("permission","1");
                c.put("building",building);

                if(subjectName!=null) { // 서브젝트 이름이 널이 아니면 --> 업데이트에서 인텐트가 넘어 왔으면
                    mDB.update("class", c, "classname= '" + subjectName.toString()+"'", null); // 클래스이름이 같은 애들의 정보를 다 바꿔주고
                }else if( set!=null) { // add에서 넘어왔으면
                    for(int i = 0; i < set.size(); i++) { // 칸 마다 다 넣어줘야 하니까 for문 돌림.
                        int grid = Integer.parseInt(set.get(i));
                        int timetable = grid / 6;
                        int weektable = grid % 6;
                        String s = mWeekTitleIds[weektable-1] + "  " + mTimeTitle[timetable-1];
                        c.put("time",s);
                        mDB.update("class" ,c, "grid= '" + set.get(i)+"'", null);
                    }
                }
                finish();
            }
        });

        //밑에는 색상 추가하는 토글버튼 추가할 때 긁어온 애들

        ((ToggleButton)findViewById(R.id.color_button1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.color.col1d);
                    classColor = "#febfbf";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=0;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;

                }
                else if(tb1==1)
                {
                    buttonView.setBackgroundResource(R.color.col1d);
                    classColor = "#febfbf";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=0;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;
                }
                else
                {
                    buttonView.setBackgroundResource(R.color.col1);
                    classColor = "#febfbf";
                }
            }
        });


        ((ToggleButton)findViewById(R.id.color_button2)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.color.col2d);
                    classColor = "#fde5c0";
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=0;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;

                }
                else if(tb2==1)
                {
                    buttonView.setBackgroundResource(R.color.col2d);
                    classColor = "#fde5c0";
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=0;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;
                }  else {
                    buttonView.setBackgroundResource(R.color.col2);
                    classColor = "#fde5c0";
                }
            }
        });

        ((ToggleButton)findViewById(R.id.color_button3)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.color.col3d);
                    classColor = "#effdc0";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=0;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;

                }
                else if(tb3==1)
                {
                    buttonView.setBackgroundResource(R.color.col3d);
                    classColor = "#effdc0";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=0;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;
                }  else {
                    buttonView.setBackgroundResource(R.color.col3);
                    classColor = "#effdc0";
                }
            }
        });

        ((ToggleButton)findViewById(R.id.color_button4)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.color.col4d);
                    classColor = "#cafdc0";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=0;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;

                }
                else if(tb4==1)
                {
                    buttonView.setBackgroundResource(R.color.col4d);
                    classColor = "#cafdc0";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=0;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;
                } else {
                    buttonView.setBackgroundResource(R.color.col4);
                    classColor = "#cafdc0";
                }
            }
        });

        ((ToggleButton)findViewById(R.id.color_button5)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.color.col5d);
                    classColor = "#bffdd7";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=0;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;

                }
                else if(tb5==1)
                {
                    buttonView.setBackgroundResource(R.color.col5d);
                    classColor = "#bffdd7";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=0;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;
                }  else {
                    buttonView.setBackgroundResource(R.color.col5);
                    classColor = "#bffdd7";
                }
            }
        });

        ((ToggleButton)findViewById(R.id.color_button6)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.color.col6d);
                    classColor = "#bffbfc";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=0;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;

                }
                else if(tb6==1)
                {
                    buttonView.setBackgroundResource(R.color.col6d);
                    classColor = "#bffbfc";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=0;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=1;
                }  else {
                    buttonView.setBackgroundResource(R.color.col6);
                    classColor = "#bffbfc";
                }
            }
        });

        ((ToggleButton)findViewById(R.id.color_button7)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.color.col7d);
                    classColor = "#bfd5fc";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=0;
                    tb8=1;
                    tb9=1;
                    tb10=1;

                }
                else if(tb7==1)
                {
                    buttonView.setBackgroundResource(R.color.col7d);
                    classColor = "#bfd5fc";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=0;
                    tb8=1;
                    tb9=1;
                    tb10=1;
                }  else {
                    buttonView.setBackgroundResource(R.color.col7);
                    classColor = "#bfd5fc";
                }
            }
        });

        ((ToggleButton)findViewById(R.id.color_button8)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.color.col8d);
                    classColor = "#cabffc";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=0;
                    tb9=1;
                    tb10=1;

                }
                else if(tb8==1)
                {
                    buttonView.setBackgroundResource(R.color.col8d);
                    classColor = "#cabffc";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=0;
                    tb9=1;
                    tb10=1;
                }  else {
                    buttonView.setBackgroundResource(R.color.col8);
                    classColor = "#cabffc";
                }
            }
        });

        ((ToggleButton)findViewById(R.id.color_button9)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.color.col9d);
                    classColor = "#f1bffc";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=9;
                    tb10=1;

                }
                else if(tb9==1)
                {
                    buttonView.setBackgroundResource(R.color.col9d);
                    classColor = "#f1bffc";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    ((ToggleButton)findViewById(R.id.color_button10)).setBackgroundResource(R.color.col10);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=0;
                    tb10=1;
                }  else {
                    buttonView.setBackgroundResource(R.color.col9);
                    classColor = "#f1bffc";
                }
            }
        });

        ((ToggleButton)findViewById(R.id.color_button10)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.color.col10d);
                    classColor = "#fcbfe3";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=0;

                }
                else if(tb10==1)
                {
                    buttonView.setBackgroundResource(R.color.col10d);
                    classColor = "#fcbfe3";
                    ((ToggleButton)findViewById(R.id.color_button2)).setBackgroundResource(R.color.col2);
                    ((ToggleButton)findViewById(R.id.color_button3)).setBackgroundResource(R.color.col3);
                    ((ToggleButton)findViewById(R.id.color_button4)).setBackgroundResource(R.color.col4);
                    ((ToggleButton)findViewById(R.id.color_button5)).setBackgroundResource(R.color.col5);
                    ((ToggleButton)findViewById(R.id.color_button6)).setBackgroundResource(R.color.col6);
                    ((ToggleButton)findViewById(R.id.color_button7)).setBackgroundResource(R.color.col7);
                    ((ToggleButton)findViewById(R.id.color_button8)).setBackgroundResource(R.color.col8);
                    ((ToggleButton)findViewById(R.id.color_button9)).setBackgroundResource(R.color.col9);
                    ((ToggleButton)findViewById(R.id.color_button1)).setBackgroundResource(R.color.col1);
                    tb1=1;
                    tb2=1;
                    tb3=1;
                    tb4=1;
                    tb5=1;
                    tb6=1;
                    tb7=1;
                    tb8=1;
                    tb9=1;
                    tb10=0;
                }  else {
                    buttonView.setBackgroundResource(R.color.col10);
                    classColor = "#fcbfe3";
                }
            }
        });
    }
}
