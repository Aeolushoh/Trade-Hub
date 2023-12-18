package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tvCountdown;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 3000; // 设置倒计时时长，单位为毫秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //初始化控件
        tvCountdown = findViewById(R.id.tv_countdown);
        // 启动倒计时


        startCountdown();
        if(findViewById(R.id.tv_countdown)==null){
            startCountdown();
        }
        else
        {
            findViewById(R.id.tv_countdown).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    private void startCountdown() {
        countDownTimer =new CountDownTimer(timeLeftInMillis,1000) {


            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int secondsRemaining = (int) (millisUntilFinished / 800);
                tvCountdown.setText(secondsRemaining +" s");
            }

            @Override
            public void onFinish() {
                // 倒计时结束后的操作，例如跳转到主页面
                finish();
                //然后跳转到登录页面（看自己逻辑想跳转哪个页面）
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));

            }

        }.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
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
}
