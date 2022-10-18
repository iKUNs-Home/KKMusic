package com.ikunkun.kunmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ikunkun.kunmusic.comn.UserInfo;

import org.litepal.LitePal;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener  {
    //用户名编辑
    private EditText mAccount;
    //密码编辑
    private EditText mPwd;
    private EditText mPwd2;
    //注册按钮
    private Button mRegisterButton;

    public void onCreate(Bundle savedInstanceState){
//        绘制界面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //通过id找到editText的值
        mAccount = (EditText) findViewById(R.id.register_edit_account);
        mPwd = (EditText) findViewById(R.id.register_edit_pwd);
        mPwd2 = (EditText) findViewById(R.id.register_edit_pwd2);
        //通过id找到按钮
        mRegisterButton = (Button) findViewById(R.id.register_btn_register);
        //监听按钮
        mRegisterButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        //约束：必须同意条款才能完成注册
        CheckBox clause=findViewById(R.id.register_clause);
        if(clause.isChecked()){
            register();
        }
        else{
            Toast.makeText(this, "请同意《KKM协议》相关条款", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        if (isUserNameAndPwdValid()){
            String userName=mAccount.getText().toString().trim();
            String userPwd=mPwd.getText().toString().trim();
            String userPwd2=mPwd2.getText().toString().trim();
            //获取当前时间作为id
            long id=System.currentTimeMillis();
            //判断确认密码是否与密码相同
            if(!userPwd.equals(userPwd2)){
                Toast.makeText(this, "两次输入密码不同", Toast.LENGTH_SHORT).show();
            }
            else{
                //判断用户是否存在
                boolean isExist = LitePal.isExist(UserInfo.class, "userName = ? and userPwd = ?", userName, userPwd);
                if (isExist) {
                    Toast.makeText(this, "用户已经存在，不能重复注册", Toast.LENGTH_SHORT).show();
                }
                else{
                    UserInfo userInfo = new UserInfo(userName, userPwd, id);
                    //保存数据
                    boolean flag = userInfo.save();
                    if (flag) {
                        Toast.makeText(this,"注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent_Register_to_Login = new Intent(RegisterActivity.this, LoginActivity.class);    //切换User Activity至Login Activity
                        startActivity(intent_Register_to_Login);
                        finish();
                    }else{
                        Toast.makeText(this,"注册失败", Toast.LENGTH_SHORT).show();
                    }
                }
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

}
