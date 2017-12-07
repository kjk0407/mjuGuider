package com.example.koopc.project.Bullet;

/**
 * Created by kooPC on 2017-11-17.
 */

// 데이터를 주고받는 클래스 - 아이템 개념이라고 생각하면 편하다. (리팩터링 필요할듯)
public class BulletDTO {
    private String messageTitle;
    private String messageContent;
    private String messageDate;
    private String messageType;
    private String buildingName;

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public BulletDTO() {

    }

    public BulletDTO(String messageTitle, String messageContent, String messageDate, String messageType, String buildingName) {
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
        this.messageDate = messageDate;
        this.messageType = messageType;
        this.buildingName = buildingName;
    }
}
