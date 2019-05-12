package com.example.learningplatform;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.learningplatform.app.Goods;
import com.example.learningplatform.app.userInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivity";
//    private List<userInfo> userInfo = new ArrayList<>();

    // 用户名
    private EditText name;
    // 密码
    private EditText password;
    // 确认密码
    private EditText confirmPwd;
    // 手机号
    private EditText telephone;
    // 学校名称
    private EditText schoolName;
    // 性别
    private RadioGroup genderGroup;

    // 返回按钮
    private Button registerReturnBtn;

    private String userName, pwd, cPwd, phone, department, gender;

    // 确认按钮
    private Button regConfirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regConfirmBtn = findViewById(R.id.reg_confirm_btn);
        registerReturnBtn = findViewById(R.id.btn_register_return);
        registerReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        init();
    }

    private void init(){
        name = findViewById(R.id.reg_name);
        password = findViewById(R.id.reg_pwd);
        confirmPwd = findViewById(R.id.reg_confirm_pwd);
        telephone = findViewById(R.id.reg_tel);
        schoolName = findViewById(R.id.reg_school_name);
        genderGroup = findViewById(R.id.reg_sex);

//        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton radioButton = group.findViewById(checkedId);
//                // Toast.makeText(FixUserInfoActivity.this,radioButton.getText(),Toast.LENGTH_SHORT).show(); // 取RadioButton的值
//            }
//        });

        regConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
                String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
                String pwdRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";

                getEditString();

                // 判断输入框的内容
                if(TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;

                } else if(TextUtils.isEmpty(pwd)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;

                } else if(!pwd.matches(pwdRegex)) {
                    Toast.makeText(RegisterActivity.this, "密码格式错误，请输入6-16位数字和字母组合密码", Toast.LENGTH_LONG).show();
                    return;

                } else if(TextUtils.isEmpty(cPwd)) {
                    Toast.makeText(RegisterActivity.this, "请确认新密码", Toast.LENGTH_SHORT).show();
                    return;

                } else if(!pwd.equals(cPwd)) {
                    Toast.makeText(RegisterActivity.this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    return;

                } else if(!phone.matches(telRegex)) {
                    Toast.makeText(RegisterActivity.this, "电话号码格式错误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;

                } else if(TextUtils.isEmpty(phone)) {
                    Toast.makeText(RegisterActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return;

                } else if(TextUtils.isEmpty(department)) {
                    Toast.makeText(RegisterActivity.this, "请输入学校名称", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    regConfirmBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            postData();
                        }
                    });
                }

            }
        });
    }

    // 获取注册信息
    private void getEditString(){
        userName = name.getText().toString().trim();
        pwd = password.getText().toString();
        cPwd = confirmPwd.getText().toString();
        phone = telephone.getText().toString().trim();
        department = schoolName.getText().toString().trim();

        RadioButton radioButton = genderGroup.findViewById(genderGroup.getCheckedRadioButtonId());
        gender = radioButton.getText().toString();
    }

    private void postData() {
        getEditString();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10L, TimeUnit.SECONDS)
                .build();
        String url = "http://192.168.1.104:3000/lp/v1/user/register";
        RequestBody requestBody = new FormBody.Builder()
                .add("username", userName)
                .add("password", pwd)
                .add("phone", phone)
                .add("department", department)
                .add("gender", gender)
                .build();// add的name和后台读取参数的名字一致

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
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
                    // 跳转到登录页面
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    Looper.loop();
                } else if (response.code() == 500) {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, "注册失败，该用户名已存在", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }
}

