package com.example.learningplatform;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class FixMyPublishActivity extends AppCompatActivity {
    // 商品图片
    private ImageView fixPic;
    // 商品名称
    private EditText fixName;
    // 商品价格
    private EditText fixPrice;
    // 联系方式
    private EditText fixTel;
    // 商品描述
    private EditText fixDescribe;

    private String fixGoodsName, fixGoodsPrice, fixGoodsTel, fixGoodsDespribe;

    // 发布按钮
    private Button fixPublishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_my_publish);

        fixPic = findViewById(R.id.fix_goods_pic); // todo
        fixName = findViewById(R.id.fix_goods_name);
        fixPrice = findViewById(R.id.fix_goods_price);
        fixTel = findViewById(R.id.fix_goods_tel);
        fixDescribe = findViewById(R.id.fix_goods_describe);
        fixPublishBtn = findViewById(R.id.btn_fix_publish);
        init();
    }

    private void init() {
        fixPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";

                getEditString();

                if (TextUtils.isEmpty(fixGoodsName)) {
                    Toast.makeText(FixMyPublishActivity.this, "请输入物品名称", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(fixGoodsPrice)) {
                    Toast.makeText(FixMyPublishActivity.this, "请输入物品价格", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(fixGoodsTel)) {
                    Toast.makeText(FixMyPublishActivity.this, "请输入联系方式", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!fixGoodsTel.matches(telRegex)) {
                    Toast.makeText(FixMyPublishActivity.this, "电话号码格式错误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(fixGoodsDespribe)) {
                    Toast.makeText(FixMyPublishActivity.this, "请输入商品描述", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // 发布成功，跳转到主页
                    Intent intent = new Intent(FixMyPublishActivity.this, BottomBarActivity.class);
                    Toast.makeText(FixMyPublishActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });

    }

    private void getEditString() {
        fixGoodsName = fixName.getText().toString().trim();
        fixGoodsPrice = fixPrice.getText().toString().trim();
        fixGoodsTel = fixTel.getText().toString().trim();
        fixGoodsDespribe = fixDescribe.getText().toString().trim();
    }
}
