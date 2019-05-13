package com.example.learningplatform;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningplatform.app.Goods;
import com.example.learningplatform.app.GoodsEntity;

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

    // 返回按钮
    private Button goodsInfoReturnBtn;
    private GoodsEntity.GoodsInfo goods = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        goods = (GoodsEntity.GoodsInfo) getIntent().getExtras().get("goods");
        // 返回按钮
        goodsInfoReturnBtn = findViewById(R.id.goods_info_return);
        goodsInfoReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsInfoActivity.this, BottomBarActivity.class);
                intent.putExtra("id", 3);
                startActivity(intent);
                finish();
            }
        });

        // 给商品信息赋值
        showGoodsName = findViewById(R.id.show_goods_name);
        showGoodsPrice = findViewById(R.id.show_goods_price);
        showGoodsDescribe = findViewById(R.id.show_goods_describe);
        showGoodsTel = findViewById(R.id.show_goods_tel);

        showGoodsName.setText(goods.getName());
        showGoodsPrice.setText(goods.getPrice() + "元");

        showGoodsTel.setText("15291020570");

        dial = findViewById(R.id.btn_dial);
        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调出拨号界面，同时传递电话号码
                Intent Intent =  new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + showGoodsTel.getText().toString().trim()));
                startActivity(Intent);
            }
        });

    }
}
