package com.ikunkun.kunmusic;
import static com.ikunkun.kunmusic.MainActivity.*;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.ikunkun.kunmusic.comn.UserInfo;
import com.ikunkun.kunmusic.views.CommunityFragment;
import com.ikunkun.kunmusic.views.MineFragment;

import org.litepal.LitePal;
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
    public static UserInfo tempuser=new UserInfo();
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
                Intent intent_Login_to_Register = new Intent(LoginActivity.this,RegisterActivity.class) ;
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
            //切换界面
            Intent intent = new Intent(LoginActivity.this,MainActivity.class) ;
            // 将用户名传到xxx
            intent.putExtra("userName",user);
                System.out.println("user" +user);
            startActivity(intent);
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                Message msg = MineFragment.handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("username",user);
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
            Toast.makeText(this,"用户名不能为空", Toast.LENGTH_SHORT).show();
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