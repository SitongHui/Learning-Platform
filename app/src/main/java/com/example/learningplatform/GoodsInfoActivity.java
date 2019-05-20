package com.example.learningplatform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.learningplatform.app.Goods;
import com.example.learningplatform.app.GoodsEntity;
import com.example.learningplatform.app.UserEntity;
import com.example.learningplatform.views.MyImageView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class GoodsInfoActivity extends Activity {
    private final String TAG = "HomeFragment";

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
    // 展示商品图片
    private ImageView showGoodsPic;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    UserEntity.DataBean userInfo = (UserEntity.DataBean) msg.obj;
                    showGoodsTel.setText(userInfo.getPhone());
            }
        }
    };

    // 返回按钮
    private Button goodsInfoReturnBtn;
    private GoodsEntity.GoodsInfo goods = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserData();
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
        showGoodsPic = findViewById(R.id.show_goods_detail_pic);

        showGoodsName.setText(goods.getName());
        showGoodsPrice.setText(goods.getPrice() + "元");
        showGoodsDescribe.setText(goods.getDescription());

        Log.v("zhanghz","url--"+goods.getFaceUrl());
        Glide.with(this).load(goods.getFaceUrl()).placeholder(R.drawable.purple).into(showGoodsPic);

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

    private void getUserData() {
        goods = (GoodsEntity.GoodsInfo) getIntent().getExtras().get("goods");
        int ownerId = goods.getOwnerId();
        String url = "http://"+ Constancts.IP +"/lp/v1/user/" + ownerId;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.v(TAG,message);
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
                Log.d(TAG, "onFailure: "+e.toString());
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
        UserEntity.DataBean userInfo = userEntity.getData();
        // 主线程刷新视图
        Message msg = new Message();
        msg.what = 0;
        msg.obj = userInfo;
        handler.sendMessage(msg);
    }
}
