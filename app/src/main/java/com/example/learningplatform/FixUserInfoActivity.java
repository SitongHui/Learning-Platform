package com.example.learningplatform;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.learningplatform.app.UserEntity;
import com.example.learningplatform.utils.FileUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FixUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "FixUserInfoActivity";

    private EditText telIcon;
    private EditText schoolIcon;
    private RadioGroup myRadioGrop;
    private Button confirmBtn;
    // 返回按钮
    private Button fixUserInfoReturnBtn;

    private String tel, school, gender;

    private UserEntity.DataBean user = null ;

    // 图片地址
    String mImgUri;

    // 书籍图像
    @ViewInject(R.id.user_pic)
    private ImageView myGoodsPic;
    @ViewInject(R.id.user_take_pic)
    private Button myTakePic;
    @ViewInject(R.id.user_gallery)
    private Button myTakeGallery;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_user_info);
        ViewUtils.inject(this);

        String avatar = getIntent().getStringExtra("avatar");
        Glide.with(this).load(avatar).placeholder(R.drawable.hello).into(myGoodsPic);
        String gender = getIntent().getStringExtra("gender");

        telIcon = findViewById(R.id.et_tel);
        schoolIcon = findViewById(R.id.et_school);

        Drawable drawableTel=getResources().getDrawable(R.drawable.icon_tel);
        drawableTel.setBounds(0,0,50,50);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        telIcon.setCompoundDrawables(drawableTel,null,null,null);

        Drawable drawableSchool = getResources().getDrawable(R.drawable.icon_school);
        drawableSchool.setBounds(0,0,70,70);//第一0是距左边距离，第二0是距上边距离，70、70分别是长宽
        schoolIcon.setCompoundDrawables(drawableSchool,null,null,null);

        // 性别控件
        myRadioGrop = findViewById(R.id.rg_sex);

        // 确定修改按钮
        confirmBtn = findViewById(R.id.confirm_btn);

        // 返回按钮
        fixUserInfoReturnBtn = findViewById(R.id.btn_fix_user_info_return);
        fixUserInfoReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FixUserInfoActivity.this, BottomBarActivity.class);
                intent.putExtra("id", 1);
                startActivity(intent);
                finish();
            }
        });

        init();
    }

    private void init() {
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";

                getEditString();
                if (TextUtils.isEmpty(tel)) {
                    Toast.makeText(FixUserInfoActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return;

                }else if (TextUtils.isEmpty(school)) {
                    Toast.makeText(FixUserInfoActivity.this, "请输入学校名称", Toast.LENGTH_SHORT).show();
                    return;

                } else if (!tel.matches(telRegex)) {
                    Toast.makeText(FixUserInfoActivity.this, "电话号码格式错误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;

                }  else {
                    fixData();
                }
            }
        });
    }

    private void getEditString() {
        tel = telIcon.getText().toString().trim();
        school = schoolIcon.getText().toString().trim();

        RadioButton radioButton = myRadioGrop.findViewById(myRadioGrop.getCheckedRadioButtonId());
        gender = radioButton.getText().toString();
    }

    private void fixData() {
        int userId = Objects.requireNonNull(this).getSharedPreferences(Constancts.OWNERID, Context.MODE_PRIVATE).getInt("userId", -1);

        getEditString();
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://" + Constancts.IP + "/lp/v1/user/" + userId;

        MultipartBody requestBody = null;
        if (this.mImgUri == null) {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)//上传图片格式一般都是这个格式MediaType.parse("multipart/form-data")
                    .addFormDataPart("phone", tel)
                    .addFormDataPart("department", school)
                    .addFormDataPart("gender", gender)
                    .build();
        } else {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"),new File(this.mImgUri));
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)//上传图片格式一般都是这个格式MediaType.parse("multipart/form-data")
                    .addFormDataPart("phone", tel)
                    .addFormDataPart("department", school)
                    .addFormDataPart("gender", gender)
                    .addFormDataPart("avatar","faceImage.jpg", fileBody)
                    .build();
        }

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
                Log.d(TAG, "onResponse: " + response.message().toString());
                if(response.code() == 200) {
                    // 跳转到个人信息页面
                    Intent intent = new Intent(FixUserInfoActivity.this, BottomBarActivity.class);
                    intent.putExtra("id", 1);
                    Looper.prepare();
                    Toast.makeText(FixUserInfoActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                    Looper.loop();

                } else if (response.code() == 500) {
                    Looper.prepare();
                    Toast.makeText(FixUserInfoActivity.this, "修改失败，请重新输入", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

    // 图片问题
    @OnClick({R.id.user_take_pic, R.id.user_gallery})

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_take_pic:
                autoObtainCameraPermission();
                break;
            case R.id.user_gallery:
                autoObtainStoragePermission();
                break;
            default:
        }
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(FixUserInfoActivity.this, "com.zz.fileprovider", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(FixUserInfoActivity.this, "com.zz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtils.showShort(this, "请允许打开相机！！");
                }
                break;


            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    ToastUtils.showShort(this, "请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "com.zz.fileprovider", new File(newUri.getPath()));
                        }
                        this.mImgUri = FileUtils.getRealPathFromUri(FixUserInfoActivity.this, data.getData());
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        this.mImgUri = FileUtils.getRealPathFromUri(FixUserInfoActivity.this,cropImageUri);
                        showImages(bitmap);
                    }
                    break;
                default:
            }
        }
    }


    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    private void showImages(Bitmap bitmap) {
//        myGoodsPic.setImageBitmap(bitmap);
        Glide.with(this).load(bitmap).placeholder(R.drawable.hello).into(myGoodsPic);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


}
