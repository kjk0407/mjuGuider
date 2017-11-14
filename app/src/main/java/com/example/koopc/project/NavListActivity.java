package com.example.koopc.project;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class NavListActivity extends AppCompatActivity {
    private ListView listView;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); // 참조 데이터베이스 선언 ( 그냥 선언시 루트 베이스에서 찾는다. )
    DatabaseReference buildingNameRef = mRootRef.child("building"); // 참조 데이터베이스 내 차일드 값 받기.

    SimpleAdapter adapter;
    static Intent navIntent; // intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_nav_list);
        listView = (ListView)findViewById(R.id.navList);
        navIntent = this.getIntent();
        refresh(); // refresh
    }

    private void refresh(){
        buildingNameRef.addValueEventListener(new ValueEventListener() { // buildRef를 이용함.

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String,String>>(); //
                for(DataSnapshot ds : dataSnapshot.getChildren()){// 스냅 샷으로 빌딩의 정보를 받아 for문을 돌린다.
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("buildingName", ds.child("buildingName").getValue().toString());

                    mList.add(item);
                }
                adapter = new SimpleAdapter(listView.getContext(), mList, android.R.layout.simple_list_item_1,
                        new String[]{"buildingName"}, new int[] {android.R.id.text1});
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String s = listView.getAdapter().getItem(position).toString();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){// 스냅 샷으로 빌딩의 정보를 받아 for문을 돌린다.
                            String snapshotName = "{buildingName="+ds.child("buildingName").getValue().toString()+"}";
                            if(snapshotName.equals(s)){

                                navIntent.putExtra("navBuildingLatitude",ds.child("buildingLatitude").getValue().toString());
                                navIntent.putExtra("navBuildingLongitude",ds.child("buildingLongitude").getValue().toString());
                                navIntent.putExtra("navBuildingName",ds.child("buildingName").getValue().toString());
                                navIntent.putExtra("navBuildingDescription",ds.child("buildingDescription").getValue().toString());
                                setResult(Activity.RESULT_OK,navIntent);
                                finish();
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void navCancel(View v){
        finish();
    }
}
