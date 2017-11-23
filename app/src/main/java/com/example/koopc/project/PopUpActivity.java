package com.example.koopc.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.koopc.project.bulletIn.BulletActivity;
import com.example.koopc.project.event.EventActivity;
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
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); // 참조 데이터베이스 선언 ( 그냥 선언시 루트 베이스에서 찾는다. )
    DatabaseReference buildingNameRef = mRootRef.child("building"); // 참조 데이터베이스 내 차일드 값 받기.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_up);

        mBuildingNameView = (TextView) findViewById(R.id.popup_buildingName);
        imageView = (ImageView)findViewById(R.id.popup_buildingImage);
//        mBuildingDescriptionView = (TextView) findViewById(R.id.popup_buildingDescription);
//        mEventView = (TextView)findViewById(R.id.popup_event);

        latitude = this.getIntent().getStringExtra("gpsLatitude");// 그냥 이거 latitude랑
        longitude = this.getIntent().getStringExtra("gpsLongitude"); // longitude 따로 받았다.
        checkBuilding = this.getIntent().getStringExtra("map_buildingName");

        // 스피너 층수들을 배열에저장
        String[] str = getResources().getStringArray(R.array.number);
        Spinner spinner = (Spinner)findViewById(R.id.floor_button);
        final TextView stv = (TextView)findViewById(R.id.floor_text);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //스피너 층에 따른 층간사진 변화를 주기 위한 함수
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()) {
                    case "1층":
                        imageView.setImageResource(R.mipmap.ic_launcher);
                        stv.setText(parent.getItemAtPosition(position).toString());
                        break;
                    case "2층":
                        imageView.setImageResource(R.drawable.university);
                        stv.setText(parent.getItemAtPosition(position).toString());
                        break;
                    case "3층":
                        imageView.setImageResource(R.drawable.university);
                        stv.setText(parent.getItemAtPosition(position).toString());
                        break;
                    case "4층":
                        imageView.setImageResource(R.drawable.university);
                        stv.setText(parent.getItemAtPosition(position).toString());
                        break;
                    case "5층":
                        imageView.setImageResource(R.drawable.university);
                        stv.setText(parent.getItemAtPosition(position).toString());
                        break;
                    case "6층":
                        imageView.setImageResource(R.drawable.university);
                        stv.setText(parent.getItemAtPosition(position).toString());
                        break;
                    case "7층":
                        imageView.setImageResource(R.drawable.university);
                        stv.setText(parent.getItemAtPosition(position).toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //데이터 받기 (변경사항이 있을 경우 즉각 반응하도록 설계되어 있다.)
        buildingNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {// 스냅 샷으로 빌딩의 정보를 받아 for문을 돌린다.
                    if (ds.child("buildingLatitude").getValue().toString().equals(latitude) // 받은 위도와 경도를 디비의 경도들과 비교
                            && ds.child("buildingLongitude").getValue().toString().equals(longitude)) {
                        buildingName = ds.child("buildingName").getValue().toString();
                        mBuildingNameView.setText(buildingName); // 있으면 화면에 표시
                        mBuildingNameView.setBackgroundResource(R.mipmap.ic_launcher); // 이미지 세팅
//                        mBuildingDescriptionView.setText(ds.child("buildingDescription").getValue().toString());

                        // buildingResID 받아서 비교해서 각자 건물에 맞는 사진 추가
                        switch (ds.child("buildingResId").getValue().toString()) {
                            case "engineer_one":
                                mBuildingNameView.setBackgroundResource(R.drawable.engineer_one);
                                break;
                            case "engineer_two":
                                mBuildingNameView.setBackgroundResource(R.drawable.engineer_two);
                                break;
                            case "engineer_three":
                                mBuildingNameView.setBackgroundResource(R.drawable.engineer_three);
                                break;
                            case "engineer_five":
                                mBuildingNameView.setBackgroundResource(R.drawable.engineer_five);
                                break;
                            case "myoungjindang":
                                mBuildingNameView.setBackgroundResource(R.drawable.myoungjindang);
                                break;
                            case "changjogwan":
                                mBuildingNameView.setBackgroundResource(R.drawable.changjogwan);
                                break;
                            case "chaeyukgwan":
                                mBuildingNameView.setBackgroundResource(R.drawable.chaeyukgwan);
                                break;
                            case "haksaenggwan":
                                mBuildingNameView.setBackgroundResource(R.drawable.haksaenggwan);
                                break;
                            case "hambakgwan":
                                mBuildingNameView.setBackgroundResource(R.drawable.hambakgwan);
                                break;
                            case "chasaedae":
                                mBuildingNameView.setBackgroundResource(R.drawable.chasaedae);
                                break;
                            case "hangjungdong":
                                mBuildingNameView.setBackgroundResource(R.drawable.hangjungdong);
                                break;
                            case "bangmok":
                                mBuildingNameView.setBackgroundResource(R.drawable.bangmok);
                                break;
                            case "chaepul":
                                mBuildingNameView.setBackgroundResource(R.drawable.chaepul);
                                break;
                            case "gongdongsilhum":
                                mBuildingNameView.setBackgroundResource(R.drawable.gongdongsilhum);
                                break;
                            default:
                                mBuildingNameView.setBackgroundResource(R.drawable.university);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void eventPress(View view){
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("buildingName", buildingName);
        startActivity(intent);
    }
}