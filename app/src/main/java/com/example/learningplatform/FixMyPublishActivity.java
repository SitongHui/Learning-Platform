package com.example.learningplatform;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.learningplatform.listview.ListViewActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;


public class FixMyPublishActivity extends AppCompatActivity implements View.OnClickListener {
    // 商品图片
//    private ImageView fixPic;
    // 商品名称
    private EditText fixName;
    // 商品价格
    private EditText fixPrice;
    // 联系方式
    private EditText fixTel;
    // 商品描述
    private EditText fixDescribe;

    private String fixGoodsName, fixGoodsPrice, fixGoodsTel, fixGoodsDespribe;

    // 发布按钮
    private Button fixPublishBtn;

    // 返回按钮
    private Button fixGoodsInfoReturnBtn;

    // 书籍图像
    private static final String TAG = "MainActivity";
    @ViewInject(R.id.my_goods_pic)
    private ImageView myGoodsPic;
    @ViewInject(R.id.my_take_pic)
    private Button myTakePic;
    @ViewInject(R.id.my_gallery)
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
        setContentView(R.layout.activity_fix_my_publish);
        ViewUtils.inject(this);

//        fixPic = findViewById(R.id.fix_goods_pic); // todo
        fixName = findViewById(R.id.fix_goods_name);
        fixPrice = findViewById(R.id.fix_goods_price);
        fixTel = findViewById(R.id.fix_goods_tel);
        fixDescribe = findViewById(R.id.fix_goods_describe);
        fixPublishBtn = findViewById(R.id.btn_fix_publish);
        fixGoodsInfoReturnBtn = findViewById(R.id.fix_goods_info_return);

        fixGoodsInfoReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FixMyPublishActivity.this, ListViewActivity.class);
                startActivity(intent);
            }
        });

        // 根据物品信息赋值
        fixName.setText("《物联网工程》");
        fixPrice.setText("10");
        fixTel.setText("18829213052");
        fixDescribe.setText("本书是物联网工程的专业课书籍");

        init();
    }


    private void init() {
        fixPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";

                getEditString();

                if (TextUtils.isEmpty(fixGoodsName)) {
                    Toast.makeText(FixMyPublishActivity.this, "请输入物品名称", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(fixGoodsPrice)) {
                    Toast.makeText(FixMyPublishActivity.this, "请输入物品价格", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(fixGoodsTel)) {
                    Toast.makeText(FixMyPublishActivity.this, "请输入联系方式", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!fixGoodsTel.matches(telRegex)) {
                    Toast.makeText(FixMyPublishActivity.this, "电话号码格式错误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(fixGoodsDespribe)) {
                    Toast.makeText(FixMyPublishActivity.this, "请输入商品描述", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // 发布成功，跳转到主页
                    Intent intent = new Intent(FixMyPublishActivity.this, BottomBarActivity.class);
                    Toast.makeText(FixMyPublishActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });

    }

    private void getEditString() {
        fixGoodsName = fixName.getText().toString().trim();
        fixGoodsPrice = fixPrice.getText().toString().trim();
        fixGoodsTel = fixTel.getText().toString().trim();
        fixGoodsDespribe = fixDescribe.getText().toString().trim();
    }


    // 图片问题
    @OnClick({R.id.my_take_pic, R.id.my_gallery})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_take_pic:
                autoObtainCameraPermission();
                break;
            case R.id.my_gallery:
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
                    imageUri = FileProvider.getUriForFile(FixMyPublishActivity.this, "com.zz.fileprovider", fileUri);
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
                            imageUri = FileProvider.getUriForFile(FixMyPublishActivity.this, "com.zz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
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
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
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
        myGoodsPic.setImageBitmap(bitmap);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


}
