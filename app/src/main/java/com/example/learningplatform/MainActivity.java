package com.example.learningplatform;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button; // 引入button组件
import android.widget.EditText;
import android.widget.Toast;

import com.example.learningplatform.listview.ListViewActivity;

public class MainActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;

    // 登录按钮
    private Button loginBtn;
    // 注册按钮
    private Button registerBtn;
    // 登录框
    private EditText loginPic;
    // 密码框
    private EditText pwdPic;

    private String loginName, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        // 登录按钮
        loginBtn = findViewById(R.id.btn_login);

        // 注册按钮
        registerBtn = findViewById(R.id.btn_register);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人页面
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 图片大小设置
        loginPic = findViewById(R.id.login);
        pwdPic = findViewById(R.id.password);
        setPicSize();

        // 点击登录按钮进行验证
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(loginName)) { // todo 判断用户名是否存在，对应的用户密码是否正确
                    Toast.makeText(MainActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;

                }else if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    // 跳转到个人页面
                    Intent intent = new Intent(MainActivity.this, BottomBarActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    private void getEditString() {
        loginName = login.getText().toString().trim();
        pwd = password.getText().toString().trim();
    }

    private void setPicSize() {
        Drawable drawableLogin = getResources().getDrawable(R.drawable.icon_login_user);
        drawableLogin.setBounds(0,0,50,50);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        loginPic.setCompoundDrawables(drawableLogin,null,null,null);

        Drawable drawableReg = getResources().getDrawable(R.drawable.icon_pwd);
        drawableReg.setBounds(0,0,50,50);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        pwdPic.setCompoundDrawables(drawableReg,null,null,null);
    }


}
