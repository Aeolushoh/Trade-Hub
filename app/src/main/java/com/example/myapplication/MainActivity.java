package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private HomeFragment mHomeFragment;
    private MessageFragment mMessageFragment;
    private MineFragment mMineFragment;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isFirstLogin()) {
            // 如果是首次登录，弹出使用说明窗口
            showInstructionsDialog();

            // 设置已经登录过的标志
            setFirstLoginFlag(false);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // 用户已经登录，获取用户数据
            MyApplication myApplication = (MyApplication) getApplication();
            String username = preferences.getString("username", null);
            String myid = preferences.getString("myid", null);
            int userid = preferences.getInt("userid", 0);
            String xueyuan = preferences.getString("xueyuan", null);
            myApplication.setSharedVariable(userid);
            myApplication.setxueyuanVariable(xueyuan);
            myApplication.setmyidVariable(myid);
            myApplication.setnameVariable(username);
            // 获取其他用户数据，根据需要获取更多
            // 执行相应的操作，比如显示欢迎消息
        } else {
            // 用户未登录，执行登录操作
            // 比如跳转到登录界面
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigstionview);
        Intent intent=getIntent();
        int position=intent.getIntExtra("position",0);
        if(position==1) bottomNavigationView.setSelectedItemId(R.id.mine);
        else if(position==0) bottomNavigationView.setSelectedItemId(R.id.home);
        else if(position==2) bottomNavigationView.setSelectedItemId(R.id.message);
        selectedFragment(position);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.home){
                    selectedFragment(0);
                }else if(item.getItemId()==R.id.mine){
                    selectedFragment(1);
                }else if(item.getItemId()==R.id.message){
                    selectedFragment(2);
                }
                return true;
            }
        });
    }
    private void selectedFragment(int position){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction);
        if(position==0) {
            if (mHomeFragment == null) {
                mHomeFragment = new HomeFragment();
                fragmentTransaction.add(R.id.content, mHomeFragment);
            } else {
                fragmentTransaction.show(mHomeFragment);
            }
        }else if(position==1){
            if(mMineFragment==null){
                mMineFragment=new MineFragment();
                fragmentTransaction.add(R.id.content,mMineFragment);
            }else{
                fragmentTransaction.show(mMineFragment);
            }
        }else if(position==2){
            if(mMessageFragment==null){
                mMessageFragment=new MessageFragment();
                fragmentTransaction.add(R.id.content,mMessageFragment);
            }else{
                fragmentTransaction.show(mMessageFragment);
            }
        }
        fragmentTransaction.commit();
    }
    private void hideFragment(FragmentTransaction fragmentTransaction){
        if(mHomeFragment!=null){
            fragmentTransaction.hide(mHomeFragment);
        }
        if(mMineFragment!=null){
            fragmentTransaction.hide(mMineFragment);
        }
        if(mMessageFragment!=null){
            fragmentTransaction.hide(mMessageFragment);
        }
    }


    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("您真的要退出TradeHub吗？")
                .setNegativeButton("暂时不要", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }
    private boolean isFirstLogin() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        return sharedPreferences.getBoolean("first_login", true);
    }

    private void setFirstLoginFlag(boolean value) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first_login", value);
        editor.apply();
    }
    private void showInstructionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("使用说明")
                .setMessage("欢迎使用TradeHub！\n" +
                        "这里是一些使用说明：\n"
                        + "1. 发布商品在发现右上角,请先登录.\n"
                        + "2. 个人中心请点击文字实现相应功能.\n"
                        + "3. 请严格遵守社区规范.\n"
                        + "©version:1.1")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 用户点击了知道了按钮
                        // 在这里可以执行一些操作，或者直接关闭对话框
                        dialogInterface.dismiss();
                    }
                });

        // 如果需要，可以添加更多的按钮或其他定制内容
        // ...

        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
    }


};

