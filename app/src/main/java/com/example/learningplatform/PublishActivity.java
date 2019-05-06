package com.example.learningplatform;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.learningplatform.listview.MyPublishListAdapter;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PublishActivity extends AppCompatActivity {

    private Button testBtn;
    private TextView tvTest;


//    public class GetExample {
//        OkHttpClient client = new OkHttpClient();
//        String run(String url) throws IOException {
//            Request request = new Request.Builder().url(url).build();
//
//            try (Response response = client.newCall(request).execute()) {
//                return response.body().string();
//            }
//        }
//    }
//
//
//    public static void main(String[] args) throws IOException {
//        GetExample example = new GetExample();
//        String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
//    }


    /*// 成功
    String url = "http://wwww.baidu.com";
    OkHttpClient client = new OkHttpClient();
    String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code" + response);
        }
    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        testBtn = findViewById(R.id.btn_test);
        tvTest = findViewById(R.id.tv_test);
    }

}
