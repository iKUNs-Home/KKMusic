package com.ikunkun.kunmusic.comn;
import org.litepal.crud.LitePalSupport;

/**
 * 用户信息
 */
public class UserInfo extends LitePalSupport {
    private String userName;                  //用户名
    private String userPwd;                   //用户密码
    private String userId;                       //用户ID号



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public UserInfo(String userName, String userPwd, String usreId) {
        this.userName = userName;
        this.userPwd = userPwd;
        this.userId = usreId;
    }

    public UserInfo() {
    }

}