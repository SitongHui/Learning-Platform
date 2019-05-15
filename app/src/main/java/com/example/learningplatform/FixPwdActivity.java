package com.example.learningplatform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.learningplatform.app.LoginEntity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FixPwdActivity extends AppCompatActivity {
    private final String TAG = "FixPwdActivity";

    // 原密码
    private EditText password;
    // 新密码
    private EditText newPassword;
    // 确认新密码
    private EditText cNewPassword;
    // 确认按钮
    private Button confirmBtn;
    // 返回按钮
    private Button fixPwdReturnBtn;

    private String oldPwd, newPwd, cNewPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_pwd);

        // 返回按钮
        fixPwdReturnBtn = findViewById(R.id.btn_fix_pwd_return);
        fixPwdReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FixPwdActivity.this, BottomBarActivity.class);
                intent.putExtra("id", 2);
                startActivity(intent);
                finish();
            }
        });

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
                    postData();
                }
            }
        });

    }

    private void getEditString() {
        oldPwd = password.getText().toString().trim();
        newPwd = newPassword.getText().toString().trim();
        cNewPwd = cNewPassword.getText().toString().trim();
    }

    private void postData() {
        int userId = Objects.requireNonNull(this).getSharedPreferences(Constancts.OWNERID, Context.MODE_PRIVATE).getInt("userId", -1);

        getEditString();
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://" + Constancts.IP + "/lp/v1/user/" + userId;
        RequestBody requestBody = new FormBody.Builder()
                .add("password", cNewPwd)
                .build();// add的name和后台读取参数的名字一致

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.message().toString());
                if(response.code() == 200) {
                    // 跳转到登录页面，重新登录
                    Intent intent = new Intent(FixPwdActivity.this, MainActivity.class);
                    Looper.prepare();
                    Toast.makeText(FixPwdActivity.this,"修改成功，请重新登录",Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    Looper.loop();
                } else if (response.code() == 500) {
                    Looper.prepare();
                    Toast.makeText(FixPwdActivity.this, "修改失败，请重新修改", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

}
