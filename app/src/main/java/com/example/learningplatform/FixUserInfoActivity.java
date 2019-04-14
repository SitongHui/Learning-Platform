package com.example.learningplatform;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FixUserInfoActivity extends AppCompatActivity {

    private EditText telIcon;
    private EditText schoolIcon;
    private RadioGroup myRadioGrop;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_user_info);

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
        myRadioGrop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                // Toast.makeText(FixUserInfoActivity.this,radioButton.getText(),Toast.LENGTH_SHORT).show(); // 取RadioButton的值
            }
        });

        // 确定修改按钮
        confirmBtn = findViewById(R.id.confirm_btn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人信息页面
                Intent intent = new Intent(FixUserInfoActivity.this, UserInfoActivity.class);
                Toast.makeText(FixUserInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }

}
