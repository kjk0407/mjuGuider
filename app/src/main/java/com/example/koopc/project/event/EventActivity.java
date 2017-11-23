package com.example.koopc.project.event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.koopc.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EventActivity extends AppCompatActivity {
    String buildingName;
    ListView eventList;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); // 참조 데이터베이스 선언 ( 그냥 선언시 루트 베이스에서 찾는다. )
    DatabaseReference eventNameRef = mRootRef.child("event"); // 참조 데이터베이스 내 차일드 값 받기.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
    }

    protected void onStart() {
        super.onStart();
        eventUpdate();
    }

    private void eventUpdate() {
        buildingName = this.getIntent().getStringExtra("buildingName");
        eventList = (ListView) findViewById(R.id.eventList);

        final ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();

        //데이터 받기 (변경사항이 있을 경우 즉각 반응하도록 설계되어 있다.)
        eventNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("buildingName").getValue().toString().equals(buildingName)) {
                        //이벤트 추가하기
                        HashMap<String, String> item = new HashMap<String, String>();
                        for(int j=0; j<2; j++)  {
                            if(j == 0)  item.put("eventName",ds.child("eventName").getValue().toString());
                            else item.put("eventDescription", ds.child("eventDescription").getValue().toString());
                        }
                        mList.add(item);
                    }
                }

                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), mList, android.R.layout.simple_list_item_2,
                        new String[] {"eventName", "eventDescription"}, new int[] {android.R.id.text1, android.R.id.text2});
                eventList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
