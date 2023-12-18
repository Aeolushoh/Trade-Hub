package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.User;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    private MineFragment mMineFragment;
    private EditText mEtloginactivityPhonecodes;
    private ImageView mIvloginactivityShowcode;

    EditText EditTextname =null;
    EditText EditTextpassword =null;
    private String realCode;

    // 获取共享变量的值
    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
    private static final String TAG = "mysql-myapplication-login";
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEtloginactivityPhonecodes = findViewById(R.id.et_loginactivity_phoneCodes);
        mIvloginactivityShowcode = findViewById(R.id.iv_loginactivity_showCode);
        EditTextname=findViewById(R.id.login_myid);
        EditTextpassword=findViewById(R.id.login_password);
        mIvloginactivityShowcode.setImageBitmap(code.getInstance().createBitmap());
        realCode = code.getInstance().getCode().toLowerCase(); //将验证码用图片的形式显示出来
        mIvloginactivityShowcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvloginactivityShowcode.setImageBitmap(code.getInstance().createBitmap());
                realCode = code.getInstance().getCode().toLowerCase(); //将验证码用图片的形式显示出来
            }
        });



        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("position",1);
                finish();
            }
        });
    }
    public void reg(View view){

        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));

    }






    public void login(View view){

        String phoneCode = mEtloginactivityPhonecodes.getText().toString().toLowerCase();
        if(phoneCode.length()==0){
            Toast.makeText(LoginActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
        }
        else if (!phoneCode.equals(realCode)){
            Toast.makeText(LoginActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
        }
        else{
            new Thread(){
                @Override
                public void run() {
                    UserDao userDao = new UserDao();
                    int msg = 0;
                    User uu = userDao.login(EditTextname.getText().toString(),EditTextpassword.getText().toString());
                    if (uu!=null){
                        msg = 1;
                        Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("position",1);
                        String minename=uu.getName();
                        String minexueyuan=uu.getXueyuan();
                        MyApplication myApplication = (MyApplication) getApplication();
                        myApplication.setSharedVariable(uu.getId());
                        myApplication.setnameVariable(uu.getName());
                        myApplication.setmyidVariable(uu.getMyid());
                        myApplication.setxueyuanVariable(uu.getXueyuan());
                        intent.putExtra("visible","VISIBLE");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // 在登录成功后保存登录状态
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putInt("userid", uu.getId());
                        editor.putString("username", uu.getName());
                        editor.putString("myid", uu.getMyid());
                        editor.putString("xueyuan", uu.getXueyuan());
                        editor.apply();
                        finish();
                        startActivity(intent);
                    }
                    hand1.sendEmptyMessage(msg);
                }
            }.start();
        }
    }


    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
            }
        }
    };
}