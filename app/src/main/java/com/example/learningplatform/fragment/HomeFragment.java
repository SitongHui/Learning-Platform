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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningplatform.GoodsInfoActivity;
import com.example.learningplatform.R;
import com.example.learningplatform.listview.MyPublishListAdapter;

public class HomeFragment extends Fragment {

    private EditText searchEditText;
    private RecyclerView mRecyclerView;


    public HomeFragment() {}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);


        // 所有商品的发布RecyclerView
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.addItemDecoration(new MyDecoration()); // 添加分割线
        mRecyclerView.setAdapter(new LinearAdapter(getActivity(), new LinearAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
//                Toast.makeText(getActivity(), "click" + pos, Toast.LENGTH_SHORT).show();
                // 跳转到商品信息页面
                Intent intent = new Intent(getContext(), GoodsInfoActivity.class);
                startActivity(intent);
            }
        }));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置搜索图片大小
        searchEditText = view.findViewById(R.id.search);
        Drawable drawableSearch = getResources().getDrawable(R.drawable.icon_search);
        drawableSearch.setBounds(0, 0, 70, 70);
        searchEditText.setCompoundDrawables(null, null, drawableSearch, null);

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



