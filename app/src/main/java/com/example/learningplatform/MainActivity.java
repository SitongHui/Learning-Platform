package com.example.learningplatform;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // 引入button组件
import android.widget.EditText;

import com.example.learningplatform.listview.ListViewActivity;

public class MainActivity extends AppCompatActivity {

    // 登录按钮
    private Button loginBtn;
    // 登录框
    private EditText loginPic;
    // 密码框
    private EditText pwdPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 登录按钮
        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人页面
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class); // todo
                startActivity(intent);
            }
        });

        // 图片大小设置
        loginPic = findViewById(R.id.login);
        pwdPic = findViewById(R.id.password);

        Drawable drawableLogin=getResources().getDrawable(R.drawable.icon_login_user);
        drawableLogin.setBounds(0,0,50,50);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        loginPic.setCompoundDrawables(drawableLogin,null,null,null);

        Drawable drawableReg=getResources().getDrawable(R.drawable.icon_pwd);
        drawableReg.setBounds(0,0,50,50);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        pwdPic.setCompoundDrawables(drawableReg,null,null,null);

    }



//    private void setListeners() {
//        OnClick onClick = new OnClick();
//        radioBtn.setOnClickListener(onClick);
//
//    }
//
//    private class OnClick implements View.OnClickListener{
//
//        @Override
//        public void onClick(View v) {
//            Intent intent = null;
//            switch (v.getId()){
//                case R.id.btn_radioBtn;
//                    // 跳转到个人信息页面
//                    intent = new Intent(MainActivity.this, FixUserInfoActivity.class);
//
//                break;
//            }
//            startActivity(intent);
//        }
//    }

}
