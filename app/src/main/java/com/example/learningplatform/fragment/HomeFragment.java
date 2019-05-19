package com.example.learningplatform.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.learningplatform.Constancts;
import com.example.learningplatform.GoodsInfoActivity;
import com.example.learningplatform.R;
import com.example.learningplatform.app.GoodsEntity;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";

    private EditText searchEditText;
    private RecyclerView mRecyclerView;
    private Button searchBtn;

    public String seachText;

    public HomeFragment() {}

    private List<GoodsEntity.GoodsInfo> goodsList = new ArrayList<>();
    private LinearAdapter adapter = null ; // 先设置为null
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);


        // 所有商品的发布RecyclerView
        mRecyclerView = view.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.addItemDecoration(new MyDecoration()); // 添加分割线
        adapter = new LinearAdapter(getActivity(),goodsList, new LinearAdapter.OnItemClickListener() { // 此时goodsList为空
            @Override
            public void onClick(int pos) {
                // 跳转到商品信息页面
                Intent intent = new Intent(getContext(), GoodsInfoActivity.class);
                GoodsEntity.GoodsInfo goods = goodsList.get(pos);
                Bundle bundle=new Bundle();
                bundle.putParcelable("goods", goods);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);

        getData();

        // 搜索按钮
        searchBtn = view.findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData();
            }
        });

        return view;
    }

    private void getData() {
        String url = "http://"+ Constancts.IP +"/lp/v1/goods";
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

    private void searchData() {
        getEditString();
        String url = "http://"+ Constancts.IP +"/lp/v1/goods?keyword=" + seachText;
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
        GoodsEntity goodsEntity = gson.fromJson(result, GoodsEntity.class);
        if (seachText == null) {
            // 主线程刷新视图
            goodsList.addAll(goodsEntity.getData());
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            // 清空之前的数据
            goodsList.clear();
            goodsList.addAll(goodsEntity.getData());
            // 主线程刷新视图
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }

    private void getEditString() {
        seachText = searchEditText.getText().toString().trim();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置搜索图片大小
        searchEditText = view.findViewById(R.id.search); // todo 赋值
    }

    // RecyclerView分隔线
    class MyDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0 ,getResources().getDimensionPixelOffset(R.dimen.dividerHeight));
        }
    }
}



