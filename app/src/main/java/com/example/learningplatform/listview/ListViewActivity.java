package com.example.learningplatform.listview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.learningplatform.R;

import java.io.LineNumberInputStream;

public class ListViewActivity extends Activity {

    private ListView myPublishListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypublish_listview);

        // 找到我的发布ListView
        myPublishListView = findViewById(R.id.lv_mypubulish);
        myPublishListView.setAdapter(new MyPublishListAdapter(ListViewActivity.this));

    }

}
