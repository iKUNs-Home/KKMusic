package com.ikunkun.kunmusic;

import static com.ikunkun.kunmusic.MainActivity.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.ikunkun.kunmusic.comn.MusicInfo;
import com.ikunkun.kunmusic.comn.UserInfo;
import com.ikunkun.kunmusic.views.CommunityFragment;
import com.ikunkun.kunmusic.views.MineFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户登录
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mRegisterButton;                   //注册按钮
    private Button mLoginButton;                      //登录按钮
    private CheckBox mRememberCheck;
    public static UserInfo tempuser = new UserInfo();

    //    含下标为0的数组
    private static String[] tempmznames;
    private static String[] tempmzsinger;
    private static String[] tempmzcover;
    private static String[] tempmzurl;
    //    删去下标为0的数组
    private static String[] mznames;
    private static String[] mzsinger;
    private static String[] mzcover;
    private static String[] mzurl;

    List<UserInfo> list2;
    List<MusicInfo> musicInfoList = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        LitePal.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //通过id找到按钮
        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        //通过id找到editText的值
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        //监听按钮
        mRegisterButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //登录按钮
            case R.id.login_btn_login:
                login();
                break;
            //注册按钮
            case R.id.login_btn_register:
                Intent intent_Login_to_Register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_Login_to_Register);
                break;
        }
    }

    /**
     * 登录
     */
    public void login() {
        if (isUserNameAndPwdValid()) {
            //获取用户名
            String user = mAccount.getText().toString().trim();
            //获取密码
            String pwd = mPwd.getText().toString().trim();
            tempuser.setUserName(user);
            List<UserInfo> list = LitePal.where(" userName = ? and userPwd = ?", user, pwd).find(UserInfo.class);
            //登录成功
            if (list.size() > 0) {
                //        从数据库获取数据
                list2 = LitePal.where(" userName = ?", LoginActivity.tempuser.getUserName()).find(UserInfo.class);
//        此方法划分数组下标为0式，为null，需删去
                tempmznames = list2.get(0).getIlikename().split("---");
                tempmzsinger = list2.get(0).getIlikesinger().split("---");
                tempmzcover = list2.get(0).getILikeCoverUrl().split("---");
                tempmzurl = list2.get(0).getIlikeurl().split("---");
//        删去后的数据
                mznames = new String[tempmznames.length - 1];
                mzsinger = new String[tempmznames.length - 1];
                mzcover = new String[tempmznames.length - 1];
                mzurl = new String[tempmznames.length - 1];
                for (int i = 1; i < tempmznames.length; i++) {
                    System.out.println(tempmznames[i] + "---" + tempmzsinger[i]);
                    mznames[i - 1] = tempmznames[i];
                    mzsinger[i - 1] = tempmzsinger[i];
                    mzcover[i - 1] = tempmzcover[i];
                    mzurl[i - 1] = tempmzurl[i];

                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setMusicName(tempmznames[i]);
                    musicInfo.setMusicSinger(tempmzsinger[i]);
                    musicInfo.setPageImg(tempmzcover[i]);
                    musicInfo.setMusicUrl(tempmzurl[i]);
                    musicInfoList.add(musicInfo);
                }

                SharedPreferences sp = getSharedPreferences("iLikeMusicListStatus", MODE_PRIVATE);
                boolean likeIsAdd = sp.getBoolean("iLikeMzActive", false);
                System.out.println("iLikeMzActive " + likeIsAdd);
                if (!likeIsAdd) {
                    App.curUserMusicList.addAll(musicInfoList);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("iLikeMzActive", true);
                    editor.apply();
                }
                musicInfoList.clear();

                //切换界面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                // 将用户名传到xxx
                intent.putExtra("userName", user);
                System.out.println("user" + user);
                startActivity(intent);
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                Message msg = MineFragment.handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("username", user);
                msg.setData(bundle);
                MineFragment.handler.sendMessage(msg);
                finish();
                //设置用户名
//                setContentView(R.layout.nav_header);
//                System.out.println(user);
//                TextView textView=(TextView)findViewById(R.id.nav_name);
//                textView.setText(user);


//                class GameThread implements Runnable {
//                    public void run() {
//                        while (!Thread.currentThread().isInterrupted()) {
//                            try {
//                                Thread.sleep(100);
//                            }
//                            catch (InterruptedException e) {
//                                Thread.currentThread().interrupt();
//                            }   // 使用postInvalidate可以直接在线程中更新界面
//                            .postInvalidate();
//                        }
//                    }}


            } else {
                //登录失败
//                tempuser=null;
                Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isUserNameAndPwdValid() {
        //获取当前输入的用户名和密码信息
        String userName = mAccount.getText().toString().trim();
        String userPwd = mPwd.getText().toString().trim();
        //用户名为空
        if (userName.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (userPwd.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}