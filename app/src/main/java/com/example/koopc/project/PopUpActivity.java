package com.example.koopc.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.koopc.project.Bullet.BulletActivity;
import com.example.koopc.project.schedule.ScheduleCheckActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopUpActivity extends AppCompatActivity {
    TextView mBuildingNameView; // 건물 이름
    ImageView imageView; // 이미지 뷰
    String buildingName;
    String latitude;
    String longitude;
    String checkBuilding;
    String buildingResId;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); // 참조 데이터베이스 선언 ( 그냥 선언시 루트 베이스에서 찾는다. )
    DatabaseReference buildingNameRef = mRootRef.child("building"); // 참조 데이터베이스 내 차일드 값 받기.
//    DatabaseReference eventNameRef = mRootRef.child("event"); // 참조 데이터베이스 내 차일드 값 받기.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        mBuildingNameView = (TextView) findViewById(R.id.popup_buildingName);
        imageView = (ImageView)findViewById(R.id.popup_buildingImage);
        Intent intent = this.getIntent();
        latitude = intent.getStringExtra("gpsLatitude");// 그냥 이거 latitude랑
        longitude = intent.getStringExtra("gpsLongitude"); // longitude 따로 받았다.
        checkBuilding = this.getIntent().getStringExtra("map_buildingName");

        // 스피너 ( 건물 층수 )
        Spinner spinner = (Spinner)findViewById(R.id.floor_button);


        //데이터 받기 (변경사항이 있을 경우 즉각 반응하도록 설계되어 있다.)
        buildingNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {// 스냅 샷으로 빌딩의 정보를 받아 for문을 돌린다.
                    if (ds.child("buildingLatitude").getValue().toString().equals(latitude) // 받은 위도와 경도를 디비의 경도들과 비교
                            && ds.child("buildingLongitude").getValue().toString().equals(longitude)) {
                        buildingName = ds.child("buildingName").getValue().toString();
                        buildingResId = ds.child("buildingResId").getValue().toString();
                        mBuildingNameView.setText(buildingName); // 있으면 화면에 표시
                        mBuildingNameView.setBackgroundResource(R.mipmap.ic_launcher); // 이미지 세팅
//                        mBuildingDescriptionView.setText(ds.child("buildingDescription").getValue().toString());
                        ArrayAdapter<CharSequence> spinnerAdapter = null;

                        imageView.setImageResource(R.drawable.university);
                        Spinner spinner = (Spinner)findViewById(R.id.floor_button);

                        switch (buildingResId){ // buildingResID 받아서 비교해서 각자 건물에 맞는 사진 추가
                            case "engineer_one":
                                mBuildingNameView.setBackgroundResource(R.drawable.engineer_one);
                                spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.engineer_one, R.layout.spinner_item);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            case "1층":
                                                imageView.setImageResource(R.drawable.engineer_one_one_floor);
                                                break;
                                            case "2층":
                                                imageView.setImageResource(R.drawable.engineer_one_two_floor);
                                                break;
                                            case "3층":
                                                imageView.setImageResource(R.drawable.engineer_one_three_floor);
                                                break;
                                            case "4층":
                                                imageView.setImageResource(R.drawable.engineer_one_four_floor);
                                                break;
                                            case "5층":
                                                imageView.setImageResource(R.drawable.engineer_one_five_floor);
                                                break;
                                            case "6층":
                                                imageView.setImageResource(R.drawable.engineer_one_six_floor);
                                                break;
                                            default: //존재하지 않는 층수에는 NOTHING 사진
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "engineer_two":
                                mBuildingNameView.setBackgroundResource(R.drawable.engineer_two);
                                spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.engineer_two, R.layout.spinner_item);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);

                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            case "1층":
                                                imageView.setImageResource(R.drawable.engineer_two_one_floor);
                                                break;
                                            case "2층":
                                                imageView.setImageResource(R.drawable.engineer_two_two_floor);
                                                break;
                                            case "3층":
                                                imageView.setImageResource(R.drawable.engineer_two_three_floor);
                                                break;
                                            case "4층":
                                                imageView.setImageResource(R.drawable.engineer_two_four_floor);
                                                break;
                                            case "5층":
                                                imageView.setImageResource(R.drawable.engineer_two_five_floor);
                                                break;
                                            case "6층":
                                                imageView.setImageResource(R.drawable.engineer_two_six_floor);
                                                break;
                                            case "7층":
                                                imageView.setImageResource(R.drawable.engineer_two_seven_floor);
                                                break;
                                            case "8층":
                                                imageView.setImageResource(R.drawable.engineer_two_eight_floor);
                                                break;
                                            case "지하1층":
                                                imageView.setImageResource(R.drawable.engineer_two_one_underground);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "engineer_three":
                                mBuildingNameView.setBackgroundResource(R.drawable.engineer_three);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            default: //존재하지 않는 층수에는 NOTHING 사진
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "engineer_five":
                                mBuildingNameView.setBackgroundResource(R.drawable.engineer_five);
                                spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.engineer_one, R.layout.spinner_item);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            case "1층":
                                                imageView.setImageResource(R.drawable.engineer_five_one_floor);
                                                break;
                                            case "2층":
                                                imageView.setImageResource(R.drawable.engineer_five_two_floor);
                                                break;
                                            case "3층":
                                                imageView.setImageResource(R.drawable.engineer_five_three_floor);
                                                break;
                                            case "4층":
                                                imageView.setImageResource(R.drawable.engineer_five_four_floor);
                                                break;
                                            case "5층":
                                                imageView.setImageResource(R.drawable.engineer_five_five_floor);
                                                break;
                                            case "6층":
                                                imageView.setImageResource(R.drawable.engineer_five_six_floor);
                                                break;
                                            case "7층":
                                                imageView.setImageResource(R.drawable.engineer_five_seven_floor);
                                                break;
                                            default: //존재하지 않는 층수에는 NOTHING 사진
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "myoungjindang":
                                mBuildingNameView.setBackgroundResource(R.drawable.myoungjindang);
                                spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.myoungjindang, R.layout.spinner_item);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            case "1층":
                                                imageView.setImageResource(R.drawable.myoungjindang_one);
                                                break;
                                            case "2층":
                                                imageView.setImageResource(R.drawable.myoungjindang_two);
                                                break;
                                            case "3층":
                                                imageView.setImageResource(R.drawable.myoungjindang_three);
                                                break;
                                            case "4층":
                                                imageView.setImageResource(R.drawable.myoungjindang_four);
                                                break;
                                            case "5층":
                                                imageView.setImageResource(R.drawable.myoungjindang_five);
                                                break;
                                            case "6층":
                                                imageView.setImageResource(R.drawable.myoungjindang_six);
                                                break;
                                            case "지하1층":
                                                imageView.setImageResource(R.drawable.myoungjindang_one_underground);
                                                break;
                                            default: //존재하지 않는 층수에는 NOTHING 사진
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "changjogwan":
                                mBuildingNameView.setBackgroundResource(R.drawable.changjogwan);
                                spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.changjogwan, R.layout.spinner_item);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            case "1층":
                                                imageView.setImageResource(R.drawable.changjogwan_one_floor);
                                                break;
                                            case "2층":
                                                imageView.setImageResource(R.drawable.changjogwan_two_floor);
                                                break;
                                            case "3층":
                                                imageView.setImageResource(R.drawable.changjogwan_three_floor);
                                                break;
                                            case "4층":
                                                imageView.setImageResource(R.drawable.changjogwan_four_floor);
                                                break;
                                            case "5층":
                                                imageView.setImageResource(R.drawable.changjogwan_five_floor);
                                                break;
                                            case "6층":
                                                imageView.setImageResource(R.drawable.changjogwan_six_floor);
                                                break;
                                            case "7층":
                                                imageView.setImageResource(R.drawable.changjogwan_seven_floor);
                                                break;
                                            case "8층":
                                                imageView.setImageResource(R.drawable.changjogwan_eight_floor);
                                                break;
                                            default: //존재하지 않는 층수에는 NOTHING 사진
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "chaeyukgwan":
                                mBuildingNameView.setBackgroundResource(R.drawable.chaeyukgwan);
                                spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.chaeyukgwan, R.layout.spinner_item);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            case "1층":
                                                imageView.setImageResource(R.drawable.chaeyukgwan_one_floor);
                                                break;
                                            case "2층":
                                                imageView.setImageResource(R.drawable.chaeyukgwan_two_floor);
                                                break;
                                            default:
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "haksaenggwan":
                                mBuildingNameView.setBackgroundResource(R.drawable.haksaenggwan);
                                spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.haksaenggwan, R.layout.spinner_item);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            case "1층":
                                                imageView.setImageResource(R.drawable.haksaenggwan_one);
                                                break;
                                            case "2층":
                                                imageView.setImageResource(R.drawable.haksaenggwan_two);
                                                break;
                                            case "3층":
                                                imageView.setImageResource(R.drawable.haksaenggwan_three);
                                                break;
                                            case "4층":
                                                imageView.setImageResource(R.drawable.haksaenggwan_four);
                                                break;
                                            case "5층":
                                                imageView.setImageResource(R.drawable.haksaenggwan_five);
                                                break;
                                            case "지하1층":
                                                imageView.setImageResource(R.drawable.haksaenggwan_one_underground);
                                                break;
                                            default: //존재하지 않는 층수에는 NOTHING 사진
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "hambakgwan":
                                mBuildingNameView.setBackgroundResource(R.drawable.hambakgwan);
                                spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.hambakgwan, R.layout.spinner_item);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            case "지하1층":
                                                imageView.setImageResource(R.drawable.hambakgwan_one_underground);
                                                break;
                                            case "1층":
                                                imageView.setImageResource(R.drawable.hambakgwan_one);
                                                break;
                                            case "2층":
                                                imageView.setImageResource(R.drawable.hambakgwan_two);
                                                break;
                                            case "3층":
                                                imageView.setImageResource(R.drawable.hambakgwan_three);
                                                break;
                                            case "4층":
                                                imageView.setImageResource(R.drawable.hambakgwan_four);
                                                break;
                                            case "5층":
                                                imageView.setImageResource(R.drawable.hambakgwan_five);
                                                break;
                                            default: //존재하지 않는 층수에는 NOTHING 사진
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "chasaedae":
                                mBuildingNameView.setBackgroundResource(R.drawable.chasaedae);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            default: //존재하지 않는 층수에는 NOTHING 사진
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "hangjungdong":
                                mBuildingNameView.setBackgroundResource(R.drawable.hangjungdong);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){

                                            default: //존재하지 않는 층수에는 NOTHING 사진
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "bangmok":
                                mBuildingNameView.setBackgroundResource(R.drawable.bangmok);
                                spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.bangmok, R.layout.spinner_item);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            case "1층":
                                                imageView.setImageResource(R.drawable.bangmok_one_floor);
                                                break;
                                            case "2층":
                                                imageView.setImageResource(R.drawable.bangmok_two_floor);
                                                break;
                                            case "3층":
                                                imageView.setImageResource(R.drawable.bangmok_three_floor);
                                                break;
                                            case "4층":
                                                imageView.setImageResource(R.drawable.bangmok_four_floor);
                                                break;
                                            default:
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "chaepul":
                                mBuildingNameView.setBackgroundResource(R.drawable.chaepul);
                                spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.chaepul, R.layout.spinner_item);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            case "1층":
                                                imageView.setImageResource(R.drawable.chaepul_one_floor);
                                                break;
                                            case "2층":
                                                imageView.setImageResource(R.drawable.chaepul_two_floor);
                                                break;
                                            case "4층":
                                                imageView.setImageResource(R.drawable.chaepul_four_floor);
                                                break;
                                            default:
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            case "gongdongsilhum":
                                mBuildingNameView.setBackgroundResource(R.drawable.gongdongsilhum);
                                spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gongdongsilhum, R.layout.spinner_item);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            case "1층":
                                                imageView.setImageResource(R.drawable.gongdongsilhum_one);
                                                break;
                                            default: //존재하지 않는 층수에는 NOTHING 사진
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                            default:
                                mBuildingNameView.setBackgroundResource(R.drawable.university);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        switch (parent.getItemAtPosition(position).toString()){
                                            default:
                                                imageView.setImageResource(R.drawable.nothing);
                                                break;
                                        }
                                    }
                                    public void onNothingSelected(AdapterView<?> parent) {}
                                });
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        //데이터 받기 (변경사항이 있을 경우 즉각 반응하도록 설계되어 있다.)
//        eventNameRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()){
//                    if(ds.child("buildingName").getValue().toString().equals(buildingName)){
//                        mEventView.setText(ds.child("eventName").getValue().toString() + " : "+
//                                ds.child("eventDescription").getValue().toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        buildingNameRef.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { // 내가 DB에서 값을 바꿀경우 사용
                //String text = dataSnapshot.getValue(String.class); // 데이터 스냅샷으로 데이터 변동시 바로 작동함.
                //mBuildingNameView.setText(text); // 해당 스냅샷에 있는 데이터 전송

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { // 오류 동작

            }
        });
    }

    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업)
        // 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }

    // 버튼 입력하기
    public void bulletButtonClick(View view) {
        Intent intent = new Intent(this, BulletActivity.class);
        intent.putExtra("buildingName", buildingName);
        startActivity(intent);
    }

    public void schedulePress(View view){
        Intent intent = new Intent(this, ScheduleCheckActivity.class);
        intent.putExtra("building_check",checkBuilding);
        startActivity(intent);
    }

}

