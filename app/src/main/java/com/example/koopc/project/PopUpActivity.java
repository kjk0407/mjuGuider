package com.example.koopc.project;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopUpActivity extends AppCompatActivity {
    ImageView mBuildingImageView; // 건물 이미지 -- 추후 구현 예정
    TextView mBuildingNameView; // 건물 이름
    TextView mBuildingDescriptionView; // 건물 약식 서술
    TextView mEventView;//이벤트 약식 설명
    ListView mIconList; // 건물 아이콘 리스트 -- 추후 구현 예정
    String buildingName;
    String latitude;
    String longitude;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); // 참조 데이터베이스 선언 ( 그냥 선언시 루트 베이스에서 찾는다. )
    DatabaseReference buildingNameRef = mRootRef.child("building"); // 참조 데이터베이스 내 차일드 값 받기.
//    DatabaseReference eventNameRef = mRootRef.child("event"); // 참조 데이터베이스 내 차일드 값 받기.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_up);

        mBuildingNameView = (TextView) findViewById(R.id.popup_buildingName);
//        mBuildingDescriptionView = (TextView) findViewById(R.id.popup_buildingDescription);
//        mEventView = (TextView)findViewById(R.id.popup_event);

        latitude = this.getIntent().getStringExtra("gpsLatitude");// 그냥 이거 latitude랑
        longitude = this.getIntent().getStringExtra("gpsLongitude"); // longitude 따로 받았다.

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


}

