package com.example.learningplatform;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;// 引入button组件

import com.example.learningplatform.listview.ListViewActivity;

public class UserInfoActivity extends AppCompatActivity {

    // 声明我的发布按钮
    private Button publishBtn;
    // 修改个人信息
    private Button fixUserInfoBtn;
    // 修改密码
    private Button btnFixPwdBtn;
    // 退出按钮
    private Button loginOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // 我的发布
        publishBtn = findViewById(R.id.btn_publish);
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到发布页面
                Intent intent = new Intent(UserInfoActivity.this, ListViewActivity.class);
                startActivity(intent);
            }
        });

        // 修改个人信息
        fixUserInfoBtn = findViewById(R.id.btn_fixUserInfo);
        fixUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人信息页面
                Intent intent = new Intent(UserInfoActivity.this, FixUserInfoActivity.class);
                startActivity(intent);
            }
        });

        // 修改密码
        btnFixPwdBtn = findViewById(R.id.btn_fixPwd);
        btnFixPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到修改密码页面
                Intent intent = new Intent(UserInfoActivity.this, FixPwdActivity.class);
                startActivity(intent);
            }
        });

        // 退出页面
        loginOutBtn = findViewById(R.id.btn_loginOut);
        loginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到修改密码页面
                Intent intent = new Intent(UserInfoActivity.this, BottomBarActivity.class);
                startActivity(intent);
            }
        });

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


