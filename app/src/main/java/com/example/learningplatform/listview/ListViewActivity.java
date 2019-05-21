package com.example.learningplatform.listview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningplatform.BottomBarActivity;
import com.example.learningplatform.Constancts;
import com.example.learningplatform.R;
import com.example.learningplatform.app.GoodsEntity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

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
    // 返回为空值时
    private TextView emptyList;

    private List<GoodsEntity.GoodsInfo> goodsList = new ArrayList<>();
    private MyPublishListAdapter adapter = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypublish_listview);
        emptyList = findViewById(R.id.empty_my_publish);

        // 找到我的发布ListView
        myPublishListView = findViewById(R.id.lv_mypubulish);
        adapter = new MyPublishListAdapter(ListViewActivity.this, R.layout.mypublish_layout_list_item, goodsList);
        myPublishListView.setAdapter(adapter);

        adapter.setOnItemDeleteListener(new MyPublishListAdapter.onItemDeleteListener() {
            @Override
            public void onItemDelete(int pos) {
                createAlert(pos);
            }
        });

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

    private void createAlert(final int pos) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("警告")
                .setMessage("确认删除本条发布信息?")
                .setIcon(R.drawable.warning)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GoodsEntity.GoodsInfo goods = goodsList.get(pos);
                        deleteGoodsById(goods.getId());
                        Toast.makeText(ListViewActivity.this, "已删除", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ListViewActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void deleteGoodsById(final int id) {
        String url = "http://" + Constancts.IP + "/lp/v1/goods/" + id;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                goodsList.removeIf(new Predicate<GoodsEntity.GoodsInfo>() {
                    @Override
                    public boolean test(GoodsEntity.GoodsInfo goodsInfo) {
                        return goodsInfo.getId() == id;
                    }
                });
                myPublishListView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
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
                int data = goodsList.size();
                if (data == 0) {
                    emptyList.setVisibility(View.VISIBLE);
                    myPublishListView.setVisibility(View.GONE);
                }
                else {
                    emptyList.setVisibility(View.GONE);
                    myPublishListView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

}
