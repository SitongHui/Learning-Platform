package com.example.learningplatform.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningplatform.BottomBarActivity;
import com.example.learningplatform.FixPwdActivity;
import com.example.learningplatform.R;

public class AddFragment extends Fragment {
    // 商品图片
    private ImageView pic;
    // 商品名称
    private EditText name;
    // 商品价格
    private EditText price;
    // 联系方式
    private EditText tel;
    // 商品描述
    private EditText describe;

    private String goodsName, goodsPrice, goodsTel, goodsDespribe;

    // 发布按钮
    private Button publishBtn;

    public AddFragment() {}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pic = view.findViewById(R.id.goods_pic); // todo
        name = view.findViewById(R.id.goods_name);
        price = view.findViewById(R.id.goods_price);
        tel = view.findViewById(R.id.goods_tel);
        describe = view.findViewById(R.id.goods_describe);
        publishBtn = view.findViewById(R.id.btn_new_publish);
        init();
    }

    private void init() {
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";

                getEditString();

                if (TextUtils.isEmpty(goodsName)) {
                    Toast.makeText(getActivity(), "请输入物品名称", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(goodsPrice)) {
                    Toast.makeText(getActivity(), "请输入物品价格", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(goodsTel)) {
                    Toast.makeText(getActivity(), "请输入联系方式", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!goodsTel.matches(telRegex)) {
                    Toast.makeText(getActivity(), "电话号码格式错误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(goodsDespribe)) {
                    Toast.makeText(getActivity(), "请输入商品描述", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // 发布成功，跳转到主页
                    Intent intent = new Intent(getActivity(), BottomBarActivity.class);
                    Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });

    }

    private void getEditString() {
        goodsName = name.getText().toString().trim();
        goodsPrice = price.getText().toString().trim();
        goodsTel = tel.getText().toString().trim();
        goodsDespribe = describe.getText().toString().trim();
    }

}


