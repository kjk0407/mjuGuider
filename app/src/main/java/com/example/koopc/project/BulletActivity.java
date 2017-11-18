package com.example.koopc.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BulletActivity extends AppCompatActivity {
    private String buildingName;
    private BulletAdapter bulletAdapter;
    private ListView listView;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference bulletNameRef = mRootRef.child("bulletin"); // 참조 데이터베이스 내 차일드 값 받기.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bullet);

        buildingName = getIntent().getStringExtra("buildingName");
        Toast.makeText(this,buildingName,Toast.LENGTH_LONG).show();

        //게시판
        bulletAdapter = new BulletAdapter();
        listView = (ListView) findViewById(R.id.Bullet_view);
        listView.setAdapter(bulletAdapter);
        setBulletData();
    }

    private void setBulletData() {

        bulletNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int resId = getResources().getIdentifier("ic_launcher", "mipmap", getPackageName()); // 디폴트 그림 받기

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("buildingName").getValue().toString().equals(buildingName)) {
                        BulletDTO dto = new BulletDTO();
                        dto.setResID(resId);
                        dto.setTitle(ds.child("messageName").getValue().toString());
                        dto.setContent(ds.child("messageContent").getValue().toString());

                        bulletAdapter.addItem(dto);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
