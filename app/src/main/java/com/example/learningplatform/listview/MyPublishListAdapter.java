package com.example.learningplatform.listview;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningplatform.BottomBarActivity;
import com.example.learningplatform.Constancts;
import com.example.learningplatform.FixMyPublishActivity;
import com.example.learningplatform.FixPwdActivity;
import com.example.learningplatform.R;
import com.example.learningplatform.app.GoodsEntity;
import com.example.learningplatform.app.MyApp;
import com.example.learningplatform.listview.ListViewActivity;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.example.learningplatform.R.layout.activity_goods_info;
import static com.example.learningplatform.R.layout.mypublish_layout_list_item;

public class MyPublishListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<GoodsEntity.GoodsInfo> goodsList;

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
        public ImageView imageView;
        public TextView tvTitle, btnFix, btnDel;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) { // 每行列表长得什么样子
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.mypublish_layout_list_item, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.iv);
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            holder.btnFix = convertView.findViewById(R.id.btn_fix);
            holder.btnDel = convertView.findViewById(R.id.btn_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //  给控件赋值
        holder.tvTitle.setText(goodsList.get(position).getName());

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
                Context ctx = MyPublishListAdapter.this.mContext;

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                builder.setTitle("警告")
                        .setMessage("确认删除本条发布信息?")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GoodsEntity.GoodsInfo goods = goodsList.get(position);
                                deleteGoodsById(goods.getId());
                                Toast.makeText(MyPublishListAdapter.this.mContext, "已删除", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MyPublishListAdapter.this.mContext, "已取消", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        return convertView;
    }

    private void deleteGoodsById(final int id) {
        String url = "http://"+ Constancts.IP +"/lp/v1/goods/" + id;
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

                System.out.print(0);

                new Runnable(){
                    @Override
                    public void run() {
                        MyPublishListAdapter.this.notifyDataSetChanged();
                    }
                };
            }
        });
    }


}
