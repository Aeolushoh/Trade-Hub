package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.utils.ImageUtils;

public class DetailProduct extends AppCompatActivity {
    TextView tv_name;
    TextView tv_account;
    TextView tv_class;
    TextView tv_price;
    ImageView tv_photo;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String account=intent.getStringExtra("account");
        String photopath=intent.getStringExtra("photopath");
        int pclass=intent.getIntExtra("pclass",0);
        float price=intent.getFloatExtra("price",0);

        tv_name =findViewById(R.id.detail_name);
        tv_account = findViewById(R.id.detail_account);
        tv_price=findViewById(R.id.detail_price);
        tv_class=findViewById(R.id.detail_class);
        tv_photo=findViewById(R.id.detail_photo);
        tv_name.setText(name);
        tv_account.setText(account);
        tv_price.setText(String.valueOf(price));
        if(pclass==0){
            tv_class.setText("电子设备");
        } else if (pclass == 1) {
            tv_class.setText("学习用品");
        }else if(pclass==2){
            tv_class.setText("食品生鲜");
        } else if (pclass == 3) {
           tv_class.setText("IP周边");
        }else if(pclass==4){
            tv_class.setText("生活用品");
        }
        String qiniuDomain = "http://s4w1c26lc.hn-bkt.clouddn.com/";

        // 图片在七牛云上的路径

        // 拼接完整的图片 URL
        String imageUrl = qiniuDomain + photopath;

        // 使用 Glide 加载图片
        Glide.with(this)
                .load(imageUrl)
                .into(tv_photo);
    }

    public void back(View view) {
        finish();
    }
}