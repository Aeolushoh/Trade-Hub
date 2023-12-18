package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.User;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "mysql-myapplication-register";
    EditText userName = null;
    EditText userMyid = null;
    EditText userPassword = null;
    EditText userXueyuan = null;
    EditText userSPassword = null;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        userName = findViewById(R.id.name);
        userMyid = findViewById(R.id.myid);
        userPassword = findViewById(R.id.password);
        userSPassword=findViewById(R.id.spassword);
        userXueyuan=findViewById(R.id.xueyuan);
    }

    public void register(View view){



        String cname = userName.getText().toString();
        String cusername = userMyid.getText().toString();
        String cpassword = userPassword.getText().toString();
        String cphone = userXueyuan.getText().toString();
        String spassword = userSPassword.getText().toString();
        if(!cpassword.equalsIgnoreCase(spassword)) {
            Toast.makeText(getApplicationContext(),"两次输入的密码不一致！",Toast.LENGTH_LONG).show();
            return;
        }
        if(cname.length() < 2 || cusername.length() < 2 || cpassword.length() < 2||cusername.length()!=14
                ||containsChinese(cusername)
                ||containsChinese(cpassword)){
            Toast.makeText(getApplicationContext(),"输入信息不符合要求请重新输入",Toast.LENGTH_LONG).show();
            return;
        }


        User user = new User();

        user.setName(cname);
        user.setMyid(cusername);
        user.setPassword(cpassword);
        user.setXueyuan(cphone);

        new Thread(){
            @Override
            public void run() {
                int msg = 0;
                UserDao userDao = new UserDao();
                User uu = userDao.findUser(user.getMyid());
                if(uu != null){
                    msg = 1;
                }
                else{
                    boolean flag = false;
                    try {
                        flag = userDao.register(user);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    if(flag){
                        msg = 2;
                }
                }
                hand.sendEmptyMessage(msg);

            }
        }.start();


    }
    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_LONG).show();
            } else if(msg.what == 1) {
                Toast.makeText(getApplicationContext(),"该账号已经存在，请换一个账号",Toast.LENGTH_LONG).show();
            } else if(msg.what == 2) {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                //将想要传递的数据用putExtra封装在intent中
                intent.putExtra("a","注册");
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        }
    };
    public boolean containsChinese(String str) {
        String regex = "[\u4e00-\u9fa5]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        return matcher.find();
    }

}