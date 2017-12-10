package com.example.koopc.project.Bullet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.koopc.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BulletActivity extends AppCompatActivity {
    private String searchText;
    private String buildingName;
    private BulletAdapter bulletAdapter;
    private ListView listView;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference bulletNameRef = mRootRef.child("bulletin"); // 참조 데이터베이스 내 차일드 값 받기.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bullet);
        //게시판 추가하기
        buildingName = getIntent().getStringExtra("buildingName"); // 인텐트로 받아온 정보는 파이어베이스의 건물 이름이다.
        bulletAdapter = new BulletAdapter();

        listView = (ListView) findViewById(R.id.Bullet_view);
        listView.setAdapter(bulletAdapter);
        setBulletData();
    }

    // 게시판 데이터 세팅하기
    private void setBulletData() {

        bulletNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bulletAdapter.clear(); // 초기화

                // 서치 텍스트가 없을 경우 -> 전체 텍스트로 초기화한다.
                if(searchText == null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("buildingName").getValue().toString().equals(buildingName)) {
                            BulletDTO dto = new BulletDTO(); // DTO 는 item 값. item 에 해당 데이터 3개 세팅할것.
                            dto.setMessageTitle(ds.child("messageTitle").getValue().toString());
                            dto.setMessageContent(ds.child("messageContent").getValue().toString());
                            dto.setMessageDate(ds.child("messageDate").getValue().toString());
                            dto.setMessageType(ds.child("messageType").getValue().toString());
                            dto.setBuildingName(ds.child("buildingName").getValue().toString());

                            bulletAdapter.addItem(dto); // 세팅한 데이터를 토대로 어댑터에 추가하기.
                        }
                    }
                    // 서치 텍스트가 있을 경우 -> 해당 사항을 반영한다.
                } else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        boolean value = ds.child("messageTitle").getValue().toString().contains(searchText);
                        if (value == true && ds.child("buildingName").getValue().toString().equals(buildingName)) {
                            BulletDTO dto = new BulletDTO(); // DTO 는 item 값. item 에 해당 데이터 3개 세팅할것.
                            dto.setMessageTitle(ds.child("messageTitle").getValue().toString());
                            dto.setMessageContent(ds.child("messageContent").getValue().toString());
                            dto.setMessageDate(ds.child("messageDate").getValue().toString());
                            dto.setMessageType(ds.child("messageType").getValue().toString());
                            dto.setBuildingName(ds.child("buildingName").getValue().toString());

                            bulletAdapter.addItem(dto); // 세팅한 데이터를 토대로 어댑터에 추가하기.
                        }
                    }
                }

                // 어댑터에 붙이고 클릭 리스너 달기.
                listView.setAdapter(bulletAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BulletDTO bulletItem = (BulletDTO) bulletAdapter.getItem(position);
                        String name = bulletItem.getMessageTitle();
                        String content = bulletItem.getMessageContent();

                        Intent intent = new Intent(getApplicationContext(), BulletMsgActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("content", content);
                        intent.putExtra("type", "watchMode");
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // 버튼 클릭 - 추가 이벤트
    public void bulletAddClick(View view) {
        Intent intent = new Intent(getApplicationContext(), BulletMsgActivity.class);
        intent.putExtra("type", "addMode");
        startActivityForResult(intent, 0);
    }

    // 추가 이벤트에서 데이터 받아오고 처리하기.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                // item 설정하고 넘겨주기 후에 게시판에 추가하기
                String item1 = data.getStringExtra("sendTitle").toString();
                String item2 = data.getStringExtra("sendContent").toString();
                String item3 = mDateFormat.format(date);
                String item4 = "normal";
                String item5 = buildingName;

                BulletDTO dto = new BulletDTO(item1, item2, item3, item4, item5);
                bulletNameRef.push().setValue(dto);
                Toast.makeText(getApplicationContext(), "성공적으로 추가됨.", Toast.LENGTH_LONG).show();

                searchText = null;
                setBulletData();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "취소되었음.", Toast.LENGTH_LONG).show();

                searchText = null;
                setBulletData();
            }
        }
    }

    // 검색 버튼
    public void button_searchBullet(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_searchBullet);
        searchText = editText.getText().toString();
        setBulletData();
    }
}
