package com.example.learningplatform.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.learningplatform.BottomBarActivity;
import com.example.learningplatform.Constancts;
import com.example.learningplatform.FixPwdActivity;
import com.example.learningplatform.FixUserInfoActivity;
import com.example.learningplatform.MainActivity;
import com.example.learningplatform.R;
import com.example.learningplatform.app.GoodsEntity;
import com.example.learningplatform.app.UserEntity;
import com.example.learningplatform.listview.ListViewActivity;
import com.example.learningplatform.utils.FileUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static android.app.Activity.RESULT_OK;

public class UserFragment extends Fragment {
    private final String USER_TAG = "HomeFragment";


    // 用户个人信息
    private TextView userName;
    private TextView userPhone;
    private TextView userSchoolName;
    private TextView userSex;
    // 图片地址
    String mImgUri;

    // 声明我的发布按钮
    private Button publishBtn;
    // 修改个人信息
    private Button fixUserInfoBtn;
    // 修改密码
    private Button btnFixPwdBtn;
    // 退出按钮
    private Button loginOutBtn;

    private UserEntity.DataBean userInfo = new UserEntity.DataBean();


    // 修改头像
    private static final String TAG = "PhotoImageFragment";
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.takePic)
    Button takePic;
    @BindView(R.id.gallery)
    Button gallery;
    Unbinder unbinder;

    private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    UserEntity.DataBean userInfo = (UserEntity.DataBean) msg.obj;
                    userName.setText(userInfo.getUsername());
                    userPhone.setText(userInfo.getPhone());
                    userSchoolName.setText(userInfo.getDepartment());
                    userSex.setText(userInfo.getGender());
                    Glide.with(getActivity()).load(userInfo.getAvatar()).placeholder(R.drawable.hello).into(photo);
            }
        }
    };


    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;


    public UserFragment() {}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserData();


        // 给用户信息赋值
        userName = view.findViewById(R.id.user_name);
        userPhone = view.findViewById(R.id.user_phone);
        userSchoolName = view.findViewById(R.id.user_school_name);
        userSex = view.findViewById(R.id.user_sex);

        userName.setText("惠惠");
        userPhone.setText("13659113748");
        userSchoolName.setText("通信与信息工程学院");
        userSex.setText("女");


        // 我的发布
        publishBtn = view.findViewById(R.id.btn_publish);
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到发布页面
                Intent intent = new Intent(getActivity(), ListViewActivity.class);
                startActivity(intent);
            }
        });

        // 修改个人信息
        fixUserInfoBtn = view.findViewById(R.id.btn_fixUserInfo);
        fixUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人信息页面
                Intent intent = new Intent(getActivity(), FixUserInfoActivity.class);
                startActivity(intent);
            }
        });

        // 修改密码
        btnFixPwdBtn = view.findViewById(R.id.btn_fixPwd);
        btnFixPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到修改密码页面
                Intent intent = new Intent(getActivity(), FixPwdActivity.class);
                startActivity(intent);
            }
        });

        // 退出页面
        loginOutBtn = view.findViewById(R.id.btn_loginOut);
        loginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到登录页面
                Intent intent = new Intent(getContext(), MainActivity.class);
                Toast.makeText(getActivity(),"请重新登录", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }

    private void getUserData() {
//        getEditString();
        int userId = Objects.requireNonNull(getActivity()).getSharedPreferences(Constancts.OWNERID, Context.MODE_PRIVATE).getInt("userId", -1);

        String url = "http://"+ Constancts.IP +"/lp/v1/user/" + userId;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.v(USER_TAG,message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(USER_TAG, "onFailure: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Gson~~~
                applyData2Ui(response.body().string());
            }
        });
    }

    private void applyData2Ui(String result) {
        Gson gson = new Gson();
        UserEntity userEntity = gson.fromJson(result, UserEntity.class);
        userInfo = userEntity.getData();
        // 主线程刷新视图
        Message msg = new Message();
        msg.what = 0;
        msg.obj = userInfo;
        handler.sendMessage(msg);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.takePic, R.id.gallery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.takePic:
                autoObtainCameraPermission();
                break;
            case R.id.gallery:
                autoObtainStoragePermission();
                break;
            default:
        }
    }

    /**
     * 动态申请sdcard读写权限
     */
    private void autoObtainStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
            }
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }
    }

    /**
     * 申请访问相机权限
     */
    private void autoObtainCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    ToastUtils.showShort(getActivity(), "您已经拒绝过一次");
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
            } else {//有权限直接调用系统相机拍照
                if (hasSdcard()) {
                    imageUri = Uri.fromFile(fileUri);
                    //通过FileProvider创建一个content类型的Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(getActivity(), "com.zz.fileprovider", fileUri);
                    }
                    PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                } else {
                    ToastUtils.showShort(getActivity(), "设备没有SD卡！");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //通过FileProvider创建一个content类型的Uri
                            imageUri = FileProvider.getUriForFile(getActivity(), "com.zz.fileprovider", fileUri);
                        }
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.showShort(getActivity(), "设备没有SD卡！");
                    }
                } else {
                    ToastUtils.showShort(getActivity(), "请允许打开相机！！");
                }
                break;
            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    ToastUtils.showShort(getActivity(), "请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: requestCode: " + requestCode + "  resultCode:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.e(TAG, "onActivityResult: resultCode!=RESULT_OK");
            return;
        }
        switch (requestCode) {
            //相机返回
            case CODE_CAMERA_REQUEST:
                cropImageUri = Uri.fromFile(fileCropUri);
                PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                break;
            //相册返回
            case CODE_GALLERY_REQUEST:

                if (hasSdcard()) {
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(getActivity(), data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newUri = FileProvider.getUriForFile(getActivity(), "com.zz.fileprovider", new File(newUri.getPath()));
                    }
                    this.mImgUri = FileUtils.getRealPathFromUri(getActivity(), data.getData());
                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                } else {
                    ToastUtils.showShort(getActivity(), "设备没有SD卡！");
                }
                break;
            //裁剪返回
            case CODE_RESULT_REQUEST:
                Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, getActivity());
                if (bitmap != null) {
                    this.mImgUri = FileUtils.getRealPathFromUri(getActivity(), cropImageUri);
                    showImages(bitmap);
                }
                break;
            default:
        }
    }

    private void showImages(Bitmap bitmap) {
        photo.setImageBitmap(bitmap);
        // 上传头像
        fixAvatar();
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public void fixAvatar() {
        int userId = Objects.requireNonNull(getActivity()).getSharedPreferences(Constancts.OWNERID, Context.MODE_PRIVATE).getInt("userId", -1);

        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://" + Constancts.IP + "/lp/v1/user/" + userId;

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"),new File(this.mImgUri));
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("avatar","faceImage.jpg", fileBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200) {
                    // 发布成功，跳转到主页
                    Intent intent = new Intent(getActivity(), BottomBarActivity.class);
                    Looper.prepare();
                    Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    Looper.loop();

                } else if (response.code() == 500) {
                    Looper.prepare();
                    Toast.makeText(getContext(), "修改失败，请重新修改", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

}


