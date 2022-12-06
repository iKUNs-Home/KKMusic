package com.ikunkun.kunmusic.comn;
import org.litepal.crud.LitePalSupport;

import java.util.LinkedList;

/**
 * 用户信息
 */
public class UserInfo extends LitePalSupport {
    private String userName;                  //用户名
    private String userPwd;                   //用户密码
    private String userId;                       //用户ID号


    public String getIlikename() {
        return Ilikename;
    }

    public void setIlikename(String ilikename) {
        Ilikename = ilikename;
    }

    public String getIlikesinger() {
        return Ilikesinger;
    }

    public void setIlikesinger(String ilikesinger) {
        Ilikesinger = ilikesinger;
    }

    public String getIlikeurl() {
        return Ilikeurl;
    }

    public void setIlikeurl(String ilikeurl) {
        Ilikeurl = ilikeurl;
    }

    private String Ilikename=new String();
    private String Ilikesinger=new String();
    private String Ilikeurl=new String();
    private String ILikeCoverUrl = new String();

    public String getILikeCoverUrl() {
        return ILikeCoverUrl;
    }

    public void setILikeCoverUrl(String ILikeCoverUrl) {
        this.ILikeCoverUrl = ILikeCoverUrl;
    }

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