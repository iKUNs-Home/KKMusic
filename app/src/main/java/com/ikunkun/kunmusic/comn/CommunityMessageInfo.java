package com.ikunkun.kunmusic.comn;
import org.litepal.crud.LitePalSupport;

public class CommunityMessageInfo extends LitePalSupport {
    private String userName;                  //用户名
    private String message;
    private String messageTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CommunityMessageInfo(String userName, String userPwd, String usreId) {
        this.userName = userName;

    }

    public CommunityMessageInfo() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}