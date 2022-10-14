package com.ikunkun.kunmusic.util.music.musicAbstract;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.ikunkun.kunmusic.util.music.MusicEnum ;

import java.net.HttpCookie;
import java.util.List;

//@Getter
//@Setter

public abstract class AbstractMusic {
    public MusicEnum getMusicEnum() {
        return musicEnum;
    }

    public void setMusicEnum(MusicEnum musicEnum) {
        this.musicEnum = musicEnum;
    }

    public String getCurrentRunningMethod() {
        return currentRunningMethod;
    }

    public void setCurrentRunningMethod(String currentRunningMethod) {
        this.currentRunningMethod = currentRunningMethod;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public JSONObject getParameter() {
        return parameter;
    }

    public void setParameter(JSONObject parameter) {
        this.parameter = parameter;
    }

    public String getCookieString() {
        return cookieString;
    }

    public void setCookieString(String cookieString) {
        this.cookieString = cookieString;
    }

    public MusicEnum musicEnum; // 网易云还是QQ 或者其他音乐服务
    private String currentRunningMethod;// 记录当前正在调用的是哪一个方法
    private JSONObject result;// 发送请求返回的json数据
    private JSONObject parameter; // 请求带的参数
    private String cookieString; // 用户的cookie信息

}
