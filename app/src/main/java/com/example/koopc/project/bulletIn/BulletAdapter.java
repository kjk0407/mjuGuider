package com.example.koopc.project.bulletIn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koopc.project.R;

import java.util.ArrayList;

/**
 * Created by kooPC on 2017-11-17.
 */

public class BulletAdapter extends BaseAdapter implements View.OnClickListener {
    private ArrayList<BulletDTO> listBullet = new ArrayList<>();

    @Override
    public void onClick(View v) {

    }

    // 리스트 객체 내 item 갯수 반환
    @Override
    public int getCount() {
        return listBullet.size();
    }

    // 전달받은 포지션 위치에 해당하는 객체의 item을 객체 형태로 반환
    @Override
    public Object getItem(int position) {
        return listBullet.get(position);
    }

    // 전달받은 포지션의 위치에 해당하는 리스트의 행ID 반환
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 실제로 아이템을 보여주는 부분이다. ( view 재사용 )
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BulletHolder holder;

         if(convertView == null) { // 처음에 보이는 항목의 갯수만큼 inflate() 함수를 실행할 것.
             // XML 로 된 Layout 을 View 객체로 전환함.
             convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bullet_item, null, false);

             // 홀더에 저장하고 태그를 달아주어 꺼내오기.
             holder = new BulletHolder();
             holder.icon = (ImageView) convertView.findViewById(R.id.Bullet_imageView);
             holder.title = (TextView) convertView.findViewById(R.id.Bullet_title);
             holder.date = (TextView) convertView.findViewById(R.id.Bullet_date);

             convertView.setTag(holder);
         } else {
             holder = (BulletHolder) convertView.getTag();
         }

         BulletDTO dto = listBullet.get(position);

//        holder.icon.setImageResource(dto.getResID()); (관리자의 공지와 일반 메세지를 타입에 따라 구분하기.)
        holder.title.setText(dto.getMessageTitle());
        holder.date.setText(dto.getMessageDate());

        return convertView;
    }

    // 메인에서 어댑터에 데이터 추가하기.
    public void addItem(BulletDTO dto) {
        listBullet.add(dto);
    }

    // 저장된 리스트 뷰 초기화하기.
    public void clear() { listBullet.clear(); }
}
