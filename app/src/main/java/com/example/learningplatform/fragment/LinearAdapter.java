package com.example.learningplatform.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningplatform.FixMyPublishActivity;
import com.example.learningplatform.GoodsInfoActivity;
import com.example.learningplatform.R;
import com.example.learningplatform.app.Goods;
import com.example.learningplatform.listview.MyPublishListAdapter;

import java.util.List;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder> {

    private Context mContext;
    private OnItemClickListener mlistener;

    // 列表数据
    private List<Goods> goodsList;

    public LinearAdapter(Context context, List<Goods> goodsList, OnItemClickListener listener) {
        this.mContext = context;
        this.mlistener = listener;
        this.goodsList = goodsList;
    }

    @NonNull
    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // 传入的view就是每个item样子的布局
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, viewGroup, false));
    }

    @Override

    // 设置item的相关数据
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder viewHolder, final int i) {
        // 给页面赋值
        viewHolder.showName.setText(goodsList.get(i).getName());
        viewHolder.showPrice.setText(goodsList.get(i).getPrice().toString());
        viewHolder.showDes.setText(goodsList.get(i).getDesc());

        // 添加点击事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // 给每个item添加点击事件
            @Override
            public void onClick(View v) {
                mlistener.onClick(i);
            }
        });

    }


    @Override
    // 列表长度
    public int getItemCount() {
        return goodsList == null ? 0 : goodsList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {

        private TextView showName;
        private TextView showPrice;
        private TextView showDes;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            showName = itemView.findViewById(R.id.show_name);
            showPrice = itemView.findViewById(R.id.show_price);
            showDes = itemView.findViewById(R.id.show_des);
        }
    }

    public interface OnItemClickListener {
        void onClick(int pos);
    }
}
