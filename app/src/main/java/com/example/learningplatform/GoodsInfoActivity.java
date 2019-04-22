package com.example.learningplatform;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsInfoActivity extends Activity {

    // 拨号按钮
    private Button dial;
    // 展示商品名称
    private TextView showGoodsName;
    // 展示商品价格
    private TextView showGoodsPrice;
    // 展示商品描述
    private TextView showGoodsDescribe;
    // 展示联系电话
    private TextView showGoodsTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);

        // 给商品信息赋值
        showGoodsName = findViewById(R.id.show_goods_name);
        showGoodsPrice = findViewById(R.id.show_goods_price);
        showGoodsDescribe = findViewById(R.id.show_goods_describe);
        showGoodsTel = findViewById(R.id.show_goods_tel);

        showGoodsName.setText("商品2");
        showGoodsPrice.setText("10" + "元");
        showGoodsDescribe.setText("这个学习资料是真的好，上面还有学长学姐的笔记，价格实惠，绝对超值！");
        showGoodsTel.setText("15291020570");

        dial = findViewById(R.id.btn_dial);
        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调出拨号界面
                Intent Intent =  new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + showGoodsTel.getText().toString().trim()));//跳转到拨号界面，同时传递电话号码
                startActivity(Intent);
            }
        });

    }
}
