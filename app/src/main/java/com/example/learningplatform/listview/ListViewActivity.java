package com.example.learningplatform.listview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.learningplatform.BottomBarActivity;
import com.example.learningplatform.FixPwdActivity;
import com.example.learningplatform.R;

import java.io.LineNumberInputStream;

public class ListViewActivity extends Activity {

    private ListView myPublishListView;
    // 返回按钮
    private Button myPublishReturnBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypublish_listview);

        // 找到我的发布ListView
        myPublishListView = findViewById(R.id.lv_mypubulish);
        myPublishListView.setAdapter(new MyPublishListAdapter(ListViewActivity.this));

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

    }

}
