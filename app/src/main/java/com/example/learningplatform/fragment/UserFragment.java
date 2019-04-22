package com.example.learningplatform.fragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningplatform.FixPwdActivity;
import com.example.learningplatform.FixUserInfoActivity;
import com.example.learningplatform.R;
import com.example.learningplatform.app.userInfo;
import com.example.learningplatform.listview.ListViewActivity;

public class UserFragment extends Fragment {

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


    public UserFragment() {}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userInfo user = new userInfo();
        user.setUserName("惠惠");
        user.setTelephone("13659113748");

        // 给用户信息赋值
        userName = view.findViewById(R.id.user_name);
        userPhone = view.findViewById(R.id.user_phone);
        userSchoolName = view.findViewById(R.id.user_school_name);
        userSex = view.findViewById(R.id.user_sex);

        userName.setText("惠惠");
        userPhone.setText("13659113748");
        userSchoolName.setText("西邮");
        userSex.setText("女");


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

    }
}


