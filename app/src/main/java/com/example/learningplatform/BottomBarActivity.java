package com.example.learningplatform;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
        setContentView(R.layout.activity_bottom_bar);

        fragmentManager = getSupportFragmentManager();
        this.setDefaultFrag();
        bindView();


        // 图片大小设置
        homeIcon = findViewById(R.id.txt_home);
        addIcon = findViewById(R.id.txt_add);
        userIcon = findViewById(R.id.txt_user);

        Drawable drawableHome=getResources().getDrawable(R.drawable.tab_menu_home);
        drawableHome.setBounds(0,0,80,80);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        homeIcon.setCompoundDrawables(null,drawableHome,null,null); // 图片出现的位置

        Drawable drawableAdd=getResources().getDrawable(R.drawable.tab_menu_add);
        drawableAdd.setBounds(0,0,70,70);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        addIcon.setCompoundDrawables(null,drawableAdd,null,null);

        Drawable drawableUser=getResources().getDrawable(R.drawable.tab_menu_user);
        drawableUser.setBounds(0,0,70,70);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        userIcon.setCompoundDrawables(null,drawableUser,null,null);

        int id = getIntent().getIntExtra("id", 0);
        if (id == 1) {
            tabUser.setSelected(true); // 从修改个人信息返回到个人信息页面时对应的底部导航栏亮起
            tabHome.setSelected(false);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new UserFragment())
                    .addToBackStack(null).commit();
        } else if (id == 2) { // 从修改密码页面返回到个人信息页面
            tabUser.setSelected(true);
            tabHome.setSelected(false);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new UserFragment())
                    .addToBackStack(null).commit();
        }  else if (id == 3) { // 从我的页面返回到个人信息页面
            tabUser.setSelected(true);
            tabHome.setSelected(false);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new UserFragment())
                    .addToBackStack(null).commit();
        }
    }



    //UI组件初始化与事件绑定
    private void bindView() {
//        topBar = findViewById(R.id.txt_top);
        tabHome = findViewById(R.id.txt_home);
        tabAdd = findViewById(R.id.txt_add);
        tabUser = findViewById(R.id.txt_user);
        ly_content = findViewById(R.id.fragment_container);

        tabHome.setSelected(true);

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


    private void setDefaultFrag(){

        this.f1 = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", "aaa");
        f1.setArguments(bundle);

        this.f2 = new AddFragment();
        Bundle bundle2= new Bundle();
        bundle2.putString("name", "bbb");
        f2.setArguments(bundle2);

        this.f3 = new UserFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("name", "ccc");
        f3.setArguments(bundle3);

        // 设置默认页面
        fragmentManager.beginTransaction().add(R.id.fragment_container, f1).commit();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch(v.getId()){
            case R.id.txt_home:
                selected();
                tabHome.setSelected(true);
                transaction.replace(R.id.fragment_container,f1);
                break;

            case R.id.txt_add:
                selected();
                tabAdd.setSelected(true);
                transaction.replace(R.id.fragment_container,f2);

                break;

            case R.id.txt_user:
                selected();
                tabUser.setSelected(true);
                transaction.replace(R.id.fragment_container,f3);
                break;
        }

        transaction.commit();
    }


}
