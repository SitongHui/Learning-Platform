package com.example.learningplatform.listview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.learningplatform.FixMyPublishActivity;
import com.example.learningplatform.R;
import com.example.learningplatform.app.GoodsEntity;

import java.util.List;

public class MyPublishListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<GoodsEntity.GoodsInfo> goodsList;
    public interface onItemDeleteListener{
        public void onItemDelete(int pos);
    }
    private onItemDeleteListener listener;
    public void setOnItemDeleteListener(onItemDeleteListener listener){
        this.listener=listener;
    }
    public MyPublishListAdapter(Context context, int resource, List<GoodsEntity.GoodsInfo> goodsList) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.goodsList = goodsList;
    }

    @Override
    public int getCount() { // 列表长度
        return goodsList == null ? 0 : goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public ImageView showBookImage;
        public TextView tvTitle, btnFix, btnDel;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) { // 每行列表长得什么样子
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.mypublish_layout_list_item, null);
            holder = new ViewHolder();
            holder.showBookImage = convertView.findViewById(R.id.my_publish_goods_pic);
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            holder.btnFix = convertView.findViewById(R.id.btn_fix);
            holder.btnDel = convertView.findViewById(R.id.btn_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //  给控件赋值
        holder.tvTitle.setText(goodsList.get(position).getName());
        Glide.with(mContext).load(goodsList.get(position).getFaceUrl()).placeholder(R.drawable.purple).into(holder.showBookImage);

        // 修改按钮进入修改已发布的商品页面
         holder.btnFix.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Context ctx = MyPublishListAdapter.this.mContext;
                 Intent intent = new Intent(ctx, FixMyPublishActivity.class);
                 GoodsEntity.GoodsInfo goods = goodsList.get(position);
                 Bundle bundle=new Bundle();
                 bundle.putParcelable("goods", goods);
                 intent.putExtras(bundle);
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 ctx.startActivity(intent);
             }
         });

         // 删除按钮
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemDelete(position);

            }
        });

        return convertView;
    }

}
