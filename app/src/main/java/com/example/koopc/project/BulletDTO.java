package com.example.koopc.project;

/**
 * Created by kooPC on 2017-11-17.
 */

// 데이터를 주고받는 클래스
public class BulletDTO {
    private int resID;
    private String title;
    private String content;

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
