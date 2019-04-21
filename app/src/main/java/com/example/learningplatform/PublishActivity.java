package com.example.learningplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.learningplatform.listview.MyPublishListAdapter;

public class PublishActivity extends AppCompatActivity {

    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        mSearchView = findViewById(R.id.searchView);
        mListView = findViewById(R.id.listView);
        mListView.setAdapter(new MyPublishListAdapter(PublishActivity.this));
        mListView.setTextFilterEnabled(true);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });

    }
}
