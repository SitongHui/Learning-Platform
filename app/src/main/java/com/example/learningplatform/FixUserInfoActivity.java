package com.example.learningplatform;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.example.learningplatform.fragment.UserFragment;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FixUserInfoActivity extends AppCompatActivity {
    private final String TAG = "FixUserInfoActivity";

    private EditText telIcon;
    private EditText schoolIcon;
    private RadioGroup myRadioGrop;
    private Button confirmBtn;
    // 返回按钮
    private Button fixUserInfoReturnBtn;

    private String tel, school, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_user_info);

        telIcon = findViewById(R.id.et_tel);
        schoolIcon = findViewById(R.id.et_school);

        Drawable drawableTel=getResources().getDrawable(R.drawable.icon_tel);
        drawableTel.setBounds(0,0,50,50);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        telIcon.setCompoundDrawables(drawableTel,null,null,null);

        Drawable drawableSchool = getResources().getDrawable(R.drawable.icon_school);
        drawableSchool.setBounds(0,0,70,70);//第一0是距左边距离，第二0是距上边距离，70、70分别是长宽
        schoolIcon.setCompoundDrawables(drawableSchool,null,null,null);

        // 性别控件
        myRadioGrop = findViewById(R.id.rg_sex);

        // 确定修改按钮
        confirmBtn = findViewById(R.id.confirm_btn);

        // 返回按钮
        fixUserInfoReturnBtn = findViewById(R.id.btn_fix_user_info_return);
        fixUserInfoReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FixUserInfoActivity.this, BottomBarActivity.class);
                intent.putExtra("id", 1);
                startActivity(intent);
                finish();
            }
        });

        init();
    }

    private void init() {
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";

                getEditString();
                if (TextUtils.isEmpty(tel)) {
                    Toast.makeText(FixUserInfoActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return;

                }else if (TextUtils.isEmpty(school)) {
                    Toast.makeText(FixUserInfoActivity.this, "请输入学校名称", Toast.LENGTH_SHORT).show();
                    return;

                } else if (!tel.matches(telRegex)) {
                    Toast.makeText(FixUserInfoActivity.this, "电话号码格式错误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;

                }  else {
                    fixData();
                }
            }
        });
    }

    private void getEditString() {
        tel = telIcon.getText().toString().trim();
        school = schoolIcon.getText().toString().trim();

        RadioButton radioButton = myRadioGrop.findViewById(myRadioGrop.getCheckedRadioButtonId());
        gender = radioButton.getText().toString();
    }

    private void fixData() {
        int userId = Objects.requireNonNull(this).getSharedPreferences(Constancts.OWNERID, Context.MODE_PRIVATE).getInt("userId", -1);

        getEditString();
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://" + Constancts.IP + "/lp/v1/user/" + userId;
        RequestBody requestBody = new FormBody.Builder()
                .add("phone", tel)
                .add("department", school)
                .add("gender", gender)
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
                    // 跳转到个人信息页面
                    Intent intent = new Intent(FixUserInfoActivity.this, BottomBarActivity.class);
                    intent.putExtra("id", 1);
                    Looper.prepare();
                    Toast.makeText(FixUserInfoActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                    Looper.loop();

                } else if (response.code() == 500) {
                    Looper.prepare();
                    Toast.makeText(FixUserInfoActivity.this, "修改失败，请重新输入", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

}
