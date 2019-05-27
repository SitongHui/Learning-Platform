package com.example.learningplatform.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.learningplatform.Constancts;
import com.example.learningplatform.FixPwdActivity;
import com.example.learningplatform.FixUserInfoActivity;
import com.example.learningplatform.MainActivity;
import com.example.learningplatform.R;
import com.example.learningplatform.app.UserEntity;
import com.example.learningplatform.listview.ListViewActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class UserFragment extends Fragment {
    private final String USER_TAG = "HomeFragment";


    // 用户个人信息
    private TextView userName;
    private TextView userPhone;
    private TextView userSchoolName;
    private TextView userSex;

    // 声明我的发布按钮
    private Button publishBtn;
    // 修改个人信息
    private Button fixUserInfoBtn;
    // 修改密码
    private Button btnFixPwdBtn;
    // 退出按钮
    private Button loginOutBtn;

    private UserEntity.DataBean userInfo = new UserEntity.DataBean();


    // 修改头像
    private static final String TAG = "PhotoImageFragment";
    @BindView(R.id.photo)
    ImageView photo;
    Unbinder unbinder;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    UserEntity.DataBean userInfo = (UserEntity.DataBean) msg.obj;
                    userName.setText(userInfo.getUsername());
                    userPhone.setText(userInfo.getPhone());
                    userSchoolName.setText(userInfo.getDepartment());
                    userSex.setText(userInfo.getGender());
                    Glide.with(getActivity()).load(userInfo.getAvatar()).placeholder(R.drawable.hello).into(photo);
            }
        }
    };

    public UserFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserData();


        // 给用户信息赋值
        userName = view.findViewById(R.id.user_name);
        userPhone = view.findViewById(R.id.user_phone);
        userSchoolName = view.findViewById(R.id.user_school_name);
        userSex = view.findViewById(R.id.user_sex);

        // 我的发布
        publishBtn = view.findViewById(R.id.btn_publish);
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到发布页面
                Intent intent = new Intent(getActivity(), ListViewActivity.class);
                startActivity(intent);
            }
        });

        // 修改个人信息
        fixUserInfoBtn = view.findViewById(R.id.btn_fixUserInfo);
        fixUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人信息页面
                Intent intent = new Intent(getActivity(), FixUserInfoActivity.class);
                intent.putExtra("avatar", userInfo.getAvatar());
                intent.putExtra("gender", userInfo.getGender());
                startActivity(intent);
            }
        });

        // 修改密码
        btnFixPwdBtn = view.findViewById(R.id.btn_fixPwd);
        btnFixPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到修改密码页面
                Intent intent = new Intent(getActivity(), FixPwdActivity.class);
                startActivity(intent);
            }
        });

        // 退出页面
        loginOutBtn = view.findViewById(R.id.btn_loginOut);
        loginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到登录页面
                Intent intent = new Intent(getContext(), MainActivity.class);
                Toast.makeText(getActivity(), "请重新登录", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }

    private void getUserData() {
//        getEditString();
        int userId = Objects.requireNonNull(getActivity()).getSharedPreferences(Constancts.OWNERID, Context.MODE_PRIVATE).getInt("userId", -1);

        String url = "http://" + Constancts.IP + "/lp/v1/user/" + userId;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.v(USER_TAG, message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(USER_TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Gson~~~
                applyData2Ui(response.body().string());
            }
        });
    }

    private void applyData2Ui(String result) {
        Gson gson = new Gson();
        UserEntity userEntity = gson.fromJson(result, UserEntity.class);
        userInfo = userEntity.getData();
        // 主线程刷新视图
        Message msg = new Message();
        msg.what = 0;
        msg.obj = userInfo;
        handler.sendMessage(msg);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}


