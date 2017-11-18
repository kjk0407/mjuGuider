package com.example.koopc.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kooPC on 2017-11-17.
 */

public class BulletAdapter extends BaseAdapter{
    private ArrayList<BulletDTO> listBullet = new ArrayList<>();

    @Override
    public int getCount() {
        return listBullet.size();
    }

    @Override
    public Object getItem(int position) {
        return listBullet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 실제로 아이템을 보여주는 부분이다.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BulletHolder holder;

         if(convertView == null) {
             convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bullet_item, null, false);

             holder = new BulletHolder();
             holder.icon = (ImageView) convertView.findViewById(R.id.Bullet_imageView);
             holder.title = (TextView) convertView.findViewById(R.id.Bullet_title);
             holder.content = (TextView) convertView.findViewById(R.id.Bullet_content);

             convertView.setTag(holder);
         } else {
             holder = (BulletHolder) convertView.getTag();
         }

         BulletDTO dto = listBullet.get(position);

        holder.icon.setImageResource(dto.getResID());
        holder.title.setText(dto.getTitle());
        holder.content.setText(dto.getContent());

        return convertView;
    }

    // 메인에서 어댑터에 데이터 추가하기.
    public void addItem(BulletDTO dto) {
        listBullet.add(dto);
    }
}
