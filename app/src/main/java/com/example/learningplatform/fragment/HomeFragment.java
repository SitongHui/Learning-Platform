package com.example.learningplatform.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningplatform.GoodsInfoActivity;
import com.example.learningplatform.R;
import com.example.learningplatform.app.Goods;
import com.example.learningplatform.listview.MyPublishListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private final String TAG="HomeFragment";// 随意叫什么名字，只是为了logcat方便过滤

    private EditText searchEditText;
    private RecyclerView mRecyclerView;
    private Button searchBtn;

    public HomeFragment() {}

    private List<Goods> goodsList =new ArrayList<>();
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
//                Toast.makeText(getActivity(), "click" + pos, Toast.LENGTH_SHORT).show();
                // 跳转到商品信息页面
                Intent intent = new Intent(getContext(), GoodsInfoActivity.class);
                Goods goods = goodsList.get(pos);
                Bundle bundle=new Bundle();
                bundle.putParcelable("goods",goods);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);

        getData();

        // 搜索按钮
        searchBtn = view.findViewById(R.id.btn_search);// todo

        return view;
    }

    private void getData() {
        String url = "http://127.0.0.1:3000/lp/v1/goods";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.toString());
                String result ="[{\"desc\":\"大学英语数据\",\"id\":1,\"name\":\"英语书\",\"price\":25.6},{\"desc\":\"大学数学书据\",\"id\":1,\"name\":\"数学书\",\"price\":25.6},{\"desc\":\"大学娃娃书据\",\"id\":1,\"name\":\"娃娃书\",\"price\":25.6}]";
                applyData2Ui(result); // 此行和上面一行到时删除
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.body().toString());
//                Gson~~~
//                applyData2Ui(response.body().toString()); // todo
            }
        });
    }

    private void applyData2Ui(String result) {
        Gson gson=new Gson();
        goodsList .addAll((List<Goods>)gson.fromJson(result,new TypeToken<List<Goods>>(){}.getType()));
        // 刷新视图
        adapter.notifyDataSetChanged();
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



