package com.example.learningplatform.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningplatform.BottomBarActivity;
import com.example.learningplatform.Constancts;
import com.example.learningplatform.FixPwdActivity;
import com.example.learningplatform.MainActivity;
import com.example.learningplatform.R;
import com.example.learningplatform.RegisterActivity;

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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {
    // 商品图片
//    private ImageView pic;
    // 商品名称
    private EditText name;
    // 商品价格
    private EditText price;
    // 联系方式
    private EditText tel;
    // 商品描述
    private EditText describe;

    private String goodsName, goodsPrice, goodsTel, goodsDespribe;

    // 发布按钮
    private Button publishBtn;


    // 修改头像
    private static final String TAG = "PhotoImageFragment";
    @BindView(R.id.add_goods_pic)
    ImageView addGoodsPic;
    @BindView(R.id.add_take_pic)
    Button addTakePic;
    @BindView(R.id.add_gallery)
    Button addGallery;
    Unbinder unbinder;


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

    public AddFragment() {}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);


//        pic = view.findViewById(R.id.goods_pic); // todo
        name = view.findViewById(R.id.goods_name);
        price = view.findViewById(R.id.goods_price);
        tel = view.findViewById(R.id.goods_tel);
        describe = view.findViewById(R.id.goods_describe);
        publishBtn = view.findViewById(R.id.btn_new_publish);
        init();
    }

    private void init() {
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";

                getEditString();

                if (TextUtils.isEmpty(goodsName)) {
                    Toast.makeText(getActivity(), "请输入物品名称", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(goodsPrice)) {
                    Toast.makeText(getActivity(), "请输入物品价格", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(goodsTel)) {
                    Toast.makeText(getActivity(), "请输入联系方式", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!goodsTel.matches(telRegex)) {
                    Toast.makeText(getActivity(), "电话号码格式错误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(goodsDespribe)) {
                    Toast.makeText(getActivity(), "请输入商品描述", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    postData();
                }
            }
        });

    }

    private void getEditString() {
        goodsName = name.getText().toString().trim();
        goodsPrice = price.getText().toString().trim();
        goodsTel = tel.getText().toString().trim();
        goodsDespribe = describe.getText().toString().trim();
    }

    private void postData() {
        getEditString();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10L, TimeUnit.SECONDS)
                .build();
        int userId = Objects.requireNonNull(getActivity()).getSharedPreferences(Constancts.SP_NAME, Context.MODE_PRIVATE).getInt("userId", -1);
        String url = "http://"+ Constancts.IP+"/lp/v1/goods";
        RequestBody requestBody = new FormBody.Builder()
                .add("name", goodsName)
                .add("price", goodsPrice)
                .add("description", goodsDespribe)
                .add("userId", userId+"")
                .add("faceUrl", "")
                .build();// todo 头像，电话（登录的用户电话） ownerid如何获取

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.message().toString());
                if(response.code() == 200) {
                    // 发布成功，跳转到主页
                    Intent intent = new Intent(getActivity(), BottomBarActivity.class);
                    Looper.prepare();
                    Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    Looper.loop();

                } else if (response.code() == 500) {
                    Looper.prepare();
                    Toast.makeText(getContext(), "发布失败，请重新发布", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }



    // 打开相机及打开相册操作
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.add_take_pic, R.id.add_gallery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_take_pic:
                autoObtainCameraPermission();
                break;
            case R.id.add_gallery:
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
                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                } else {
                    ToastUtils.showShort(getActivity(), "设备没有SD卡！");
                }
                break;
            //裁剪返回
            case CODE_RESULT_REQUEST:
                Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, getActivity());
                if (bitmap != null) {
                    showImages(bitmap);
                }
                break;
            default:
        }
    }

    private void showImages(Bitmap bitmap) {
        addGoodsPic.setImageBitmap(bitmap);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

}


