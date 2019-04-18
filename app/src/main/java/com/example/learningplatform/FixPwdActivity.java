package com.example.learningplatform;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FixPwdActivity extends AppCompatActivity {

    // 原密码
    private EditText password;
    // 新密码
    private EditText newPassword;
    // 确认新密码
    private EditText cNewPassword;
    // 确认按钮
    private Button confirmBtn;

    private String oldPwd, newPwd, cNewPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_pwd);
        init();
    }

    private void init() {
        password = findViewById(R.id.et_pwd);
        newPassword = findViewById(R.id.et_newPwd);
        cNewPassword = findViewById(R.id.et_confirmPwd);
        confirmBtn = findViewById(R.id.confirm_btn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwdRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";

                getEditString();

                if(TextUtils.isEmpty(oldPwd)) {
                    Toast.makeText(FixPwdActivity.this, "请输入原密码", Toast.LENGTH_SHORT).show();
                    return;

                } else if(TextUtils.isEmpty(newPwd)) {
                    Toast.makeText(FixPwdActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;

                } else if(TextUtils.isEmpty(cNewPwd)) {
                    Toast.makeText(FixPwdActivity.this, "请确认新密码", Toast.LENGTH_SHORT).show();
                    return;

                } else if(!newPwd.matches(pwdRegex)) {
                    Toast.makeText(FixPwdActivity.this, "密码格式错误，请输入6-16位数字和字母组合密码", Toast.LENGTH_SHORT).show();
                    return;

                } else if(!newPwd.equals(cNewPwd)) {
                    Toast.makeText(FixPwdActivity.this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    // 跳转到个人信息页面
                    Intent intent = new Intent(FixPwdActivity.this, BottomBarActivity.class);
                    intent.putExtra("id", 2);
                    startActivity(intent);
                    Toast.makeText(FixPwdActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void getEditString() {
        oldPwd = password.getText().toString().trim();
        newPwd = newPassword.getText().toString().trim();
        cNewPwd = cNewPassword.getText().toString().trim();
    }

}
