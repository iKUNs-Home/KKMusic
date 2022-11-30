package com.ikunkun.kunmusic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ikunkun.kunmusic.comn.CommunityMessageInfo;
import com.ikunkun.kunmusic.views.CommunityFragment;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PostingActivity  extends AppCompatActivity implements View.OnClickListener{
    private EditText message;
    private Button posting_out;

    public void onCreate(Bundle savedInstanceState) {
//        绘制界面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_posting);
        message= findViewById(R.id.posting_message);
        posting_out= findViewById(R.id.posting_out);
        posting_out.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        String getmessage=message.getText().toString().trim();
//        获取账户信息
        CommunityMessageInfo CommunityMessageInfo=new CommunityMessageInfo();
        CommunityMessageInfo.setMessage(getmessage);
//        判断是否登录账号，否则使用游客身份发帖
        if(LoginActivity.tempuser!=null) {
            CommunityMessageInfo.setUserName(LoginActivity.tempuser.getUserName());
        }
        else{
            CommunityMessageInfo.setUserName("KunMusic's 游客");
        }
//        发帖时间
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        CommunityMessageInfo.setMessageTime(date.format(formatter));
        boolean flag = CommunityMessageInfo.save();
        if (flag) {
            Toast.makeText(this,"发布成功", Toast.LENGTH_SHORT).show();
//            Intent intent_Posting_to_Community = new Intent(PostingActivity.this, CommunityFragment.class);    //切换User Activity至Login Activity
//            startActivity(intent_Posting_to_Community);
            Message msg = CommunityFragment.handler.obtainMessage();
            msg.what=1;
            CommunityFragment.handler.sendMessage(msg);
            finish();
        }else{
            Toast.makeText(this,"发布失败", Toast.LENGTH_SHORT).show();
        }
    }
}
