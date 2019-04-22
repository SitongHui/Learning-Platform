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
import com.example.learningplatform.listview.MyPublishListAdapter;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder> {

    private Context mContext;
    private OnItemClickListener mlistener;

    // 列表数据
//    private List<String> list;

    public LinearAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.mlistener = listener;
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
        viewHolder.showName.setText("hello world");
        viewHolder.showPrice.setText("123");
        viewHolder.showDes.setText("这是通信与信息工程学院物联网工程的《物联网概论》");

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
        return 10;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

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

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}
