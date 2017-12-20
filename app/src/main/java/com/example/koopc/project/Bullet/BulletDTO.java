package com.example.koopc.project.Bullet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kooPC on 2017-11-17.
 */

// 데이터를 주고받는 클래스 - 아이템 개념이라고 생각하면 편하다.
public class BulletDTO implements Parcelable {
    private String msgTitle;
    private String msgContent;
    private String msgDate;



    private String msgSbjName;
    private String writer;
    private String msgViewNum;
    private String msgGoodNum;
    private String subMsg;

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgSbjName() { return msgSbjName; }

    public void setMsgSbjName(String msgSbjName) { this.msgSbjName = msgSbjName; }

    public String getMsgViewNum() {
        return msgViewNum;
    }

    public void setMsgViewNum(String msgViewNum) {
        this.msgViewNum = msgViewNum;
    }

    public String getMsgGoodNum() { return msgGoodNum; }

    public void setMsgGoodNum(String msgGoodNum) {
        this.msgGoodNum = msgGoodNum;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public BulletDTO() { }

    public BulletDTO(Parcel in) {
        readFromParcel(in);
    }

    public BulletDTO(String msgTitle, String msgContent, String messageDate, String msgSbjName, String writer, String msgViewNum, String msgGoodNum, String subMsg) {
        this.msgTitle = msgTitle;
        this.msgDate = messageDate;
        this.writer = writer;
        this.msgViewNum = msgViewNum;
        this.msgGoodNum = msgGoodNum;
        this.subMsg = subMsg;
        this.msgContent = msgContent;
        this.msgSbjName = msgSbjName;
    }

    // 실제 오브젝트의 작업을 하는 메소드로 오브젝트의 각 요소를 parcel 해준다.
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msgTitle);
        dest.writeString(msgDate);
        dest.writeString(writer);
        dest.writeString(msgViewNum);
        dest.writeString(msgGoodNum);
        dest.writeString(subMsg);
        dest.writeString(msgContent);
        dest.writeString(msgSbjName);
    }

    // writeToParcel 이랑 순서 정확하게 맞출것 . (안그러면 직렬화 된거 엉켜서 코드 폭발함)
    private void readFromParcel(Parcel in){
        msgTitle = in.readString();
        msgDate = in.readString();
        writer = in.readString();
        msgViewNum = in.readString();
        msgGoodNum = in.readString();
        subMsg = in.readString();
        msgContent = in.readString();
        msgSbjName = in.readString();
    }


    // Parcel 하려는 오브젝트의 종류 정의
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BulletDTO createFromParcel(Parcel in) {
            return new BulletDTO(in);
        }

        public BulletDTO[] newArray(int size) {
            return new BulletDTO[size];
        }
    };
}
