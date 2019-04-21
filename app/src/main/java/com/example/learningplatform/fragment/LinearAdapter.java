package com.example.learningplatform.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningplatform.R;

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

        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, viewGroup, false));// 传入的view就是每个item样子的布局
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder viewHolder, final int i) {
        viewHolder.mtv.setText("hello world"); // 设置值

        viewHolder.mtv.setOnClickListener(new View.OnClickListener() { // 给每个item添加点击事件
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,"click^" + i, Toast.LENGTH_SHORT).show();
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

        private TextView mtv;// todo 点击该listView进入详情页面

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mtv = itemView.findViewById(R.id.show_name);

        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}
