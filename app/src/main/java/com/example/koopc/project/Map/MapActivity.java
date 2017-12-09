package com.example.koopc.project.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.koopc.project.Multimedia.BGM;
import com.example.koopc.project.FCM.FirebaseMessagingService;
import com.example.koopc.project.R;
import com.example.koopc.project.schedule.ScheduleActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skp.Tmap.TMapCircle;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    private TMapView tMapView; //tmap
    private Context mContext;
    private TMapGpsManager gps; // gps 매니저
    private TMapCircle tcircle; // 똥그라미!
    static final String TMAP_KEY = "6f1d2856-9bbc-37fe-852b-5b38da058aea"; // 내 티맵 키

    //fire base에서 빌딩들의 Latlag
    private  DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); // 참조 데이터베이스 선언 ( 그냥 선언시 루트 베이스에서 찾는다. )
    private  DatabaseReference buildingNameRef = mRootRef.child("building"); // 참조 데이터베이스 내 차일드 값 받기.
    private  DataSnapshot snapshot; // snapshot을 전역?으로 돌림.

    private ArrayList<TMapPoint> pointArray = new ArrayList<TMapPoint>(); // 각 빌딩들의 좌표 저장
    private ArrayList<String> buildingArray = new ArrayList<String>(); // 각 빌딩들의 이름 저장.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mContext = getApplicationContext();
        tcircle = null;

        tMapView = (TMapView)findViewById(R.id.tmapView); // xml불러옴
        tMapView.setSKPMapApiKey(TMAP_KEY); // 내 tmap키

        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN); // 티맵 언어. 5개정도 다른 언어 가능하더라
        tMapView.setIconVisibility(true); // 아이콘을 보여라
        tMapView.setZoomLevel(15); // 줌을 얼마나 할것인가?

        tMapView.setTrackingMode(true); // 트래킹 모드래 뭔지 모름 근데 쓰래
        tMapView.setSightVisible(true); // 시야각 보이는 거
        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                if(gps !=null && tcircle != null){ // GPS 있을 때

                    float[] distance = new float[2]; // 서클 중심과 마커 로케이션간의 거리
                    Location.distanceBetween(tMapMarkerItem.latitude,tMapMarkerItem.longitude,
                            tcircle.getCenterPoint().getLatitude(),tcircle.getCenterPoint().getLongitude(),distance); // 이게 디스턴스에 거리를 넣어줌.
                    if(distance[0]<= tcircle.getRadius()){ // 반지름안에 있으면 있는거고
                        Intent intent = new Intent("intent_PopupAction");
                        intent.putExtra("gpsLatitude", String.valueOf(tMapMarkerItem.latitude));
                        intent.putExtra("gpsLongitude", String.valueOf(tMapMarkerItem.longitude));
                        intent.putExtra("map_buildingName",tMapMarkerItem.getCalloutTitle());
                        startActivity(intent);
                        intent.putExtra("popupType", "true");
                    }else{ // 없을 경우 호출
                        Intent intent = new Intent("intent_PopupAction");
                        intent.putExtra("gpsLatitude", String.valueOf(tMapMarkerItem.latitude));
                        intent.putExtra("gpsLongitude", String.valueOf(tMapMarkerItem.longitude));
                        intent.putExtra("map_buildingName",tMapMarkerItem.getCalloutTitle());
                        startActivity(intent);
                        intent.putExtra("popupType", "false");
                    }
                }else{ // gps를 받지 못했을 때
                    Intent intent = new Intent("intent_PopupAction");
                    intent.putExtra("gpsLatitude", String.valueOf(tMapMarkerItem.latitude));
                    intent.putExtra("gpsLongitude", String.valueOf(tMapMarkerItem.longitude));
                    intent.putExtra("map_buildingName",tMapMarkerItem.getCalloutTitle());
                    startActivity(intent);
                    intent.putExtra("popupType", "false");
                }

            }
        });

        // 안드로이드 버젼 확인
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int result = PermissionChecker.checkCallingOrSelfPermission(this, permission);
                if (result == PermissionChecker.PERMISSION_GRANTED) ;
                else {
                    ActivityCompat.requestPermissions(this, permissions, 1);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;



        gps = new TMapGpsManager(this); // gps매니저 부름
        gps.setMinTime(100); // 갱신 시간
        gps.setMinDistance(1); // 1미터 움직일 떄마다 갱신 좀 늘려야할까요?
        gps.setProvider(gps.NETWORK_PROVIDER); // 네트워크를 통해 gps를 부름  --> 얘는 건물안에 있을 때 정확 // 자매품 gps provider도 있음
        gps.setLocationCallback(); // 로케이션 콜백이라고 뭐냐 그거 그거임
        gps.OpenGps(); // 지피에스를 엽니다.

        // 그 마커 클릭하면 설명뜨고 옆에 버튼 누르면 이거 호출됨.  --> 지금 우리 앱은 안드로이드 표시로 되있음

    }

    @Override
    protected void onStart() {
        super.onStart();
        buildingNameRef.addValueEventListener(new ValueEventListener() { // 빌딩 이름 레퍼른스 불러다가
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                snapshot = dataSnapshot; // 데이터 스냅샷이 우리가 쓰는 커서 느낌
                showMarkerPoint();
            } // 데이터 체인지 될때라기 보다는 그냥 실행할때 마커 보여줌

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        SharedPreferences opt = getSharedPreferences("Option",MODE_PRIVATE);
        int isMute = opt.getInt("isMute",0);
        MenuItem menu = (MenuItem)findViewById(R.id.OptionMenu);
        if(menu != null) {
            if (isMute == 0) {
                menu.setTitle("음악 끄기");
            } else {
                menu.setTitle("음악 켜기");
            }
        }
    }

    // 맵이 제거되었을 경우 마커의 업데이트를 중단한다.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int result = PermissionChecker.checkCallingOrSelfPermission(this, permission);
                if (result == PermissionChecker.PERMISSION_GRANTED) ;
                else {
                    ActivityCompat.requestPermissions(this, permissions, 1);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        gps.getClass();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu,menu);
        // 음악 호출출
       SharedPreferences opt = getSharedPreferences("Option",MODE_PRIVATE);
        int isMute = opt.getInt("isMute",0);
        if(isMute == 0) {
            menu.getItem(3).setTitle("음악 끄기");
        }else{
            menu.getItem(3).setTitle("음악 켜기");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId() ) {
            // 길찾기 입력
            case R.id.NaveMenu:
                Intent intent = new Intent(this, NavListActivity.class);
                startActivityForResult(intent, 0);
                return true;
            case R.id.ResetMenu:
                // 경로를 모두 지우고 마커 다시 호출
                showMarkerPoint();
                tMapView.removeTMapPath();
                return true;
            case R.id.ScheduleMenu:
                //스케쥴로 이동
                Intent intent2 = new Intent(MapActivity.this,ScheduleActivity.class);
                startActivity(intent2);
                return true;
            case R.id.OptionMenu:
                // 음악 설정.
                SharedPreferences opt = getSharedPreferences("Option",MODE_PRIVATE);
                SharedPreferences.Editor editor = opt.edit();
                int isMute = opt.getInt("isMute",0);
                if(isMute == 0){
                    editor.putInt("isMute",1);
                    item.setTitle("음악 켜기");
                    Intent mute = new Intent(this, BGM.class);
                    stopService(mute);
                }else{
                    editor.putInt("isMute",0);
                    item.setTitle("음악 끄기");
                    Intent mute = new Intent(this, BGM.class);
                    startService(mute);
                }
                editor.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //마커들을 띄워줍시다.
    public void showMarkerPoint(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; // 마커 크기 2분의 1로 줄인다. 이거 그대로 두니까 ㅈ같이 커서 반으로 쭐였어
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher_round,options); // 애는 그냥 화면에 나오는 아이콘
        Bitmap i_bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher,options); // 얘는 클릭하면 설명이랑 같이 오른쪽 버튼 그림
        for(DataSnapshot ds : snapshot.getChildren()){ // 위에서 받은 데이터 스냅샷 끝날때까지
            Double latitude = new Double(ds.child("buildingLatitude").getValue().toString()); // 위도? 받고
            Double longitude = new Double(ds.child("buildingLongitude").getValue().toString()); // 경도 받자
            String buildingName = ds.child("buildingName").getValue().toString(); // 빌딩 이름
            String buildingDescription = ds.child("buildingDescription").getValue().toString(); // 빌딩 설명

            TMapPoint point = new TMapPoint(latitude, longitude); // TmapPoint가 구글맵에서 LatLag?엿던가? 그거임
            pointArray.add(point);
            buildingArray.add(buildingName);
            markerSetting(buildingName,buildingDescription,point,bitmap,i_bitmap); // 이거 밑에서 써서 다시 쓰기 귀찮
        }
    }

    public void markerSetting(String buildingName,String buildingDescription ,TMapPoint point, Bitmap bitmap, Bitmap i_bitmap){
        TMapMarkerItem item = new TMapMarkerItem(); // 마커 아이템 만듭니다.
        item.setTMapPoint(point); // 받은 위도 경도에 세팅
        item.setName(buildingName); // 아이템이름을 빌딩이름으로
        item.setVisible(item.VISIBLE); // 화면에 띄워주고
        item.setIcon(bitmap); // 아이콘을 위에서 설정한걸로 설정
        item.setCalloutTitle(buildingName); // 누르면 뜨는 타이틀 설정
        item.setCalloutSubTitle(buildingDescription); // 마커 누르면 뜨는 설명 설정
        item.setCanShowCallout(true); // 뭔지 모르는데 쓰래
//        item.setAutoCalloutVisible(true); // 얘는 시작할때 클릭한 상태로 부를꺼냐 그거임
        item.setCalloutRightButtonImage(i_bitmap); // 오른쪽 버튼 아이콘으로 적용

        tMapView.addMarkerItem(buildingName,item); // 아이디 빌딩이름으로 정하고 마커 맵에 추가
    }

    @Override
    public void onLocationChange(Location location) { // gps바뀌면 --> 우리가 이동하면

        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude()); // gps받고 현재 위치 카메라로 설정 --> 얘가 longitude, latitude임
        tMapView.removeAllTMapCircle(); // 이전에 넣었던 모든 똥그라미 지우고
        tMapView.setZoomLevel(17); // 줌 땡기고
        tcircle = new TMapCircle(); // 서클 만들고
        tcircle.setCenterPoint(gps.getLocation()); // 똥그라미의 중심에는 우리가 있다.
        tcircle.setRadius(30); // 30미터 반경
        tcircle.setAreaColor(Color.parseColor("#880000ff")); // 컬러는 파랑이
        tcircle.setAreaAlpha(4); // 투명도
        tMapView.addTMapCircle("2",tcircle); // 아이디는 귀찮아서 2넣었음
        tMapView.removeAllTMapPolyLine(); // 모든 폴리라인 지우고

        // FCM 빌딩 정보를 갱신시켜줄 물건.
        float[] distance = new float[2]; // 서클 중심과 마커 로케이션간의 거리
        // 움직일 떄 FCM에 장소를 갱신해 주기 위함.
        for(int i = 0; i < pointArray.size(); i++){
            Location.distanceBetween(pointArray.get(i).getLatitude(),pointArray.get(i).getLongitude(),
                    tcircle.getCenterPoint().getLatitude(),tcircle.getCenterPoint().getLongitude(),distance); // 이게 디스턴스에 거리를 넣어줌.
            if(distance[0]<= tcircle.getRadius()){ // 반지름안에 있으면 있으면
                Log.d("HAHAHAH", buildingArray.get(i));
                FirebaseMessagingService.nowBuilding = buildingArray.get(i).toString(); // FCM에 있는 now 빌딩에 현재 빌딩 상태 저장.
                Log.i("HAHAHAH1",FirebaseMessagingService.nowBuilding);
                break; // 브레이크
            }else if(pointArray.size() == i + 1){ // 반지름 안에 없는 경우 끝까지 조사했는지를 조사하고
                Log.d("TAG", "빌딩 없음");
                FirebaseMessagingService.nowBuilding = ""; // FCM의 빌딩을 초기화 시켜줌.
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                if(gps != null){ // gps가 null이 아니면 경로 표시
                    TMapData tmapData = new TMapData(); // 애가 경로같은거 관할하는 애임
                    tMapView.removeAllMarkerItem(); // 마커 많아서 짜증남

                    //네브리스트에서 받아옴
                    double latitude =  new Double(data.getStringExtra("navBuildingLatitude"));
                    double longitude = new Double(data.getStringExtra("navBuildingLongitude"));
                    TMapPoint point = new TMapPoint(latitude, longitude);
                    String buildingName = data.getStringExtra("navBuildingName");
                    String buildingDescription = data.getStringExtra("navBuildingDescription");

                    // 비트맵 설정들.
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2; // 마커 크기 2분의 1로 줄인다. 반으로 쭐였어
                    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher_round,options); // 애는 그냥 화면에 나오는 아이콘
                    Bitmap i_bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher,options); // 얘는 클릭하면 설명이랑 같이 오른쪽 버튼 그림

                    //마커 설정
                    markerSetting(buildingName,buildingDescription,point,bitmap,i_bitmap);
                    //경로 설정

                    tmapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, gps.getLocation(), new TMapPoint(Double.valueOf(latitude),Double.valueOf(longitude)),
                            new TMapData.FindPathDataListenerCallback() { // 티맵에서 패쓰 정보를 받으면 호출
                                @Override
                                public void onFindPathData(TMapPolyLine PolyLine) {
                                    tMapView.removeTMapPath(); // 새로 그려줌.
                                    tMapView.addTMapPath(PolyLine); // 패쓰 추가.
                                }
                            });
                }else{
                    // GPS 설정을 하지않았거나 gps가 작동하지 않을 경우 호출
                    Toast.makeText(mContext, "GPS 정보를 받지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }else{
                //캔쓸하면 그냥 내비둠.
            }
        }
    }

    public void map_myPosition(View view) {
        if(tcircle == null){
            Toast.makeText(mContext, "아직 GPS를 받지 못헀습니다. 잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
        }else{
            tMapView.setCenterPoint(tcircle.getCenterPoint().getLongitude(),tcircle.getCenterPoint().getLatitude());
        }
    }

    public void map_mjuPosition(View view) {
        if(pointArray.isEmpty()){
            Toast.makeText(mContext, "아직 GPS를 받지 못 헀습니다. 잠시만 기다려 주세요. ", Toast.LENGTH_SHORT).show();
        }else{
            tMapView.setCenterPoint(pointArray.get(0).getLongitude(),pointArray.get(0).getLatitude(),true);
        }
    }
}