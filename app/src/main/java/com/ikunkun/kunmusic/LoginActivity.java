package com.ikunkun.kunmusic;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.ikunkun.kunmusic.comn.UserInfo;
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
            case R.id.login_btn_login://登录按钮
                login();
                break;
            case R.id.login_btn_register://这注册按钮
                Intent intent_Login_to_Register = new Intent(LoginActivity.this,RegisterActivity.class) ;    //切换Login Activity至User Activity
                startActivity(intent_Login_to_Register);
                break;
        }
    }

    /**
     * 登录
     */
    public void login() {
        //获取用户名
        String user=mAccount.getText().toString().trim();
        //获取密码
        String pwd=mPwd.getText().toString().trim();
        List<UserInfo> list= LitePal.where(" userName = ? and userPwd = ?",user,pwd).find(UserInfo.class);
        //登录成功
        if (list.size()>0){
//            Intent intent = new Intent(LoginActivity.this,InfoActivity.class) ;//切换界面
//            intent.putExtra("userName",user);//将用户名传到InfoActivity
//            startActivity(intent);
            Toast.makeText(this,"登录成功", Toast.LENGTH_SHORT).show();
        }else{//登录失败
            Toast.makeText(this,"登录失败", Toast.LENGTH_SHORT).show();
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