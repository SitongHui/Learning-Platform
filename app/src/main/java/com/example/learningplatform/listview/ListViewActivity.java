package com.example.learningplatform.listview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.learningplatform.BottomBarActivity;
import com.example.learningplatform.Constancts;
import com.example.learningplatform.GoodsInfoActivity;
import com.example.learningplatform.R;
import com.example.learningplatform.app.GoodsEntity;
import com.example.learningplatform.fragment.LinearAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class ListViewActivity extends Activity {
    private final String TAG = "ListViewActivity";

    public ListView myPublishListView;
    // 返回按钮
    private Button myPublishReturnBtn;

    private List<GoodsEntity.GoodsInfo> goodsList = new ArrayList<>();
    private MyPublishListAdapter adapter = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypublish_listview);

        // 找到我的发布ListView
        myPublishListView = findViewById(R.id.lv_mypubulish);
        adapter = new MyPublishListAdapter(ListViewActivity.this, R.layout.mypublish_layout_list_item, goodsList);
        myPublishListView.setAdapter(adapter);


        myPublishReturnBtn = findViewById(R.id.btn_my_publish_return);
        myPublishReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListViewActivity.this, BottomBarActivity.class);
                intent.putExtra("id", 2);
                startActivity(intent);
                finish();
            }
        });

        getMyGoodsData();


    }


    private void getMyGoodsData() {
        int userId = Objects.requireNonNull(this).getSharedPreferences(Constancts.OWNERID, Context.MODE_PRIVATE).getInt("userId", -1);

        String url = "http://" + Constancts.IP + "/lp/v1/goods?userId=" + userId;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.v(TAG, message);
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
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Gson~~~
//                Log.d(TAG, "onResponse: " + response.body().string());
//
                applyData2Ui(response.body().string());
            }
        });
    }

    private void applyData2Ui(String result) {
        Gson gson = new Gson();
        GoodsEntity goodsEntity = gson.fromJson(result, GoodsEntity.class);
        // 主线程刷新视图
        goodsList.addAll(goodsEntity.getData());
        myPublishListView.post(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
            }
        });
    }

}
