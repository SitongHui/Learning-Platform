package com.example.learningplatform;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.learningplatform.fragment.AddFragment;
import com.example.learningplatform.fragment.HomeFragment;
import com.example.learningplatform.fragment.UserFragment;

public class BottomBarActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView homeIcon;
    private TextView addIcon;
    private TextView userIcon;

    // fragment
    private TextView topBar;
    private TextView tabHome;
    private TextView tabAdd;
    private TextView tabUser;

    private FrameLayout ly_content;

    private HomeFragment f1;
    private AddFragment f2;
    private UserFragment f3;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_bottom_bar);

        fragmentManager = getFragmentManager();
        this.setDefaultFrag();
//        bindView();


        // 图片大小设置
        homeIcon = findViewById(R.id.txt_home);
        addIcon = findViewById(R.id.txt_add);
        userIcon = findViewById(R.id.txt_user);

        Drawable drawableHome=getResources().getDrawable(R.drawable.icon_home);
        drawableHome.setBounds(0,0,80,80);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        homeIcon.setCompoundDrawables(null,drawableHome,null,null); // 图片出现的位置

        Drawable drawableAdd=getResources().getDrawable(R.drawable.icon_add);
        drawableAdd.setBounds(0,0,70,70);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        addIcon.setCompoundDrawables(null,drawableAdd,null,null);

        Drawable drawableUser=getResources().getDrawable(R.drawable.icon_user);
        drawableUser.setBounds(0,0,70,70);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        userIcon.setCompoundDrawables(null,drawableUser,null,null);
    }



    //UI组件初始化与事件绑定
    private void bindView() {
        topBar = findViewById(R.id.txt_top);
        tabHome = findViewById(R.id.txt_home);
        tabAdd = findViewById(R.id.txt_add);
        tabUser = findViewById(R.id.txt_user);
        ly_content = findViewById(R.id.fragment_container);

        tabHome.setOnClickListener((View.OnClickListener) this);
        tabUser.setOnClickListener((View.OnClickListener) this);
        tabAdd.setOnClickListener((View.OnClickListener) this);

    }

    //重置所有文本的选中状态
    public void selected(){
        tabHome.setSelected(false);
        tabAdd.setSelected(false);
        tabUser.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }
        if(f3!=null){
            transaction.hide(f3);
        }
    }

    private void setDefaultFrag(){

        this.f2 = new AddFragment();
        this.f1 = new HomeFragment();
        this.f3 = new UserFragment();

        fragmentManager.beginTransaction().add(R.id.fragment_container, f1).commit();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch(v.getId()){
            case R.id.txt_home:
                selected();
                tabHome.setSelected(true);
                if(f1 == null){
//                    f1 = new HomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", "aaa");
                    f1.setArguments(bundle);
                    transaction.replace(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;

            case R.id.txt_add:
                selected();
                tabAdd.setSelected(true);
                if(f2 == null){
//                    f2 = new AddFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", "bbb");
                    f2.setArguments(bundle);
                    transaction.replace(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;

            case R.id.txt_user:
                selected();
                tabUser.setSelected(true);
                if(f3 == null){
//                    f3 = new UserFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", "ccc");
                    f3.setArguments(bundle);
                    transaction.replace(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;
        }

        transaction.commit();
    }


}
