package com.example.myapplication;

import static androidx.core.content.ContentResolverCompat.query;

import static com.example.myapplication.dao.QiniuUploader.uploadImage;
import static com.google.android.gms.auth.api.Auth.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.dao.QiniuUploader;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Product;
import com.example.myapplication.entity.User;
import com.example.myapplication.utils.JDBCUtils;
import com.google.android.gms.auth.api.Auth;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ReleaseActivity extends AppCompatActivity {




    //调取系统摄像头的请求码
   private static final int PICK_IMAGE_REQUEST=1;
    private Bitmap head;
    final static  String TAG="DBU";
    Handler handler;
    private AlertDialog dialog;
    private  String[] fontStyleArr= {"电子设备","学习用品","食品生鲜","IP周边","生活用品"};//存储样式
    int pclass=0;
    TextView textView;
    private String[] classArr = {"电子设备","学习用品","食品生鲜","IP周边","生活用品"};
    String photopath1;
    String photop;
    Product product = new Product();

    private int userid=0;


    EditText ProName = null;
    EditText ProPrice = null;
    EditText ProAccount= null;
    private ImageView imageView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_release);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

        MyApplication myApplication = (MyApplication) getApplication();
        userid= myApplication.getSharedVariable();

        ProName = findViewById(R.id.pname);
        ProPrice = findViewById(R.id.price);
        ProAccount = findViewById(R.id.account);
        textView=(TextView) findViewById(R.id.Pclass);
        imageView=findViewById(R.id.photo1);

        imageView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        }));
    }


    public void release(View view){

        String cname = ProName.getText().toString();

        String caccount = ProAccount.getText().toString();
        float  cprice= Float.parseFloat(ProPrice.getText().toString());
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap cphoto=imageView.getDrawingCache();

        photopath1=saveImageToStorage(cphoto);
        photop= uploadImage(photopath1);
       /* CompletableFuture<String> uploadResult = uploadImage(photopath1);

        uploadResult.whenComplete((result, exception) -> {
            if (exception == null) {
                System.out.println("Upload successful. Image URL: " + result);
            } else {
                System.err.println("Upload failed: " + exception.getMessage());
            }
        });
        try {
            photop=uploadResult.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }*/


        product.setName(cname);
        product.setAccount(caccount);
        product.setPrice(cprice);
        product.setPclass(pclass);
        product.setPhoto(photop);

        new Thread(){
            @Override
            public void run() {
                int msg=0;
                UserDao userDao=new UserDao();
                boolean flag;
                try {
                    flag = userDao.release(product);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if(flag){
                    msg=1;
                    if(userid!=0&&!userDao.findrelease(userid,product.getId())){
                        try {
                            userDao.setrelease(userid,product.getId());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Intent intent=new Intent(ReleaseActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("visible","VISIBLE");
                    intent.putExtra("position",0);
                    finish();
                    startActivity(intent);
                }
                hand.sendEmptyMessage(msg);
            }
        }.start();

    }
    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        public void handleMessage(Message msg) {
            if(msg.what==0) {
                Toast.makeText(getApplicationContext(),"发布失败",Toast.LENGTH_LONG).show();
            }else if(msg.what==1){
                Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_LONG).show();
            }
        }
    };


        public int Choiceclass(View view) {
        //  创建对话框并设置其样式(这里采用链式方程)
        AlertDialog.Builder builder = new AlertDialog.Builder(this)//设置单选框列表
                .setTitle("选择商品类型")   //设置标题
                .setSingleChoiceItems(fontStyleArr, pclass, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pclass=i; //在OnClick方法中得到被点击的序号 i
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//在对话框中设置“确定”按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textView.setText(classArr[pclass]);
                        //设置好字体大小后关闭单选对话框
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//在对话框中设置”取消按钮“
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
        return pclass;
    }


    private void openGallery(){
            Intent galleryIntent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null){
            Uri selectedImageUri=data.getData();
            Glide.with(this)
                    .load(selectedImageUri)
                    .into(imageView);
        }
    }

    private String saveImageToStorage(Bitmap bitmap) {
        String filePath = "";
        try {
            // 获取应用的私有外部存储路径
            File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            // 创建一个唯一的文件名
            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            File file = new File(directory, fileName);
            filePath = file.getAbsolutePath();

            // 将 Bitmap 保存到文件
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
    @SuppressLint("StaticFieldLeak")
    class InputTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            // 在后台线程中加载数据，可以执行耗时操作
            // 这里可以替换为你的数据加载逻辑
             uploadImage(photopath1);
             return  product.getPhoto();
        }

        @Override
        protected void onPostExecute(String path) {
            // 在主线程中更新UI，将加载的数据设置给适配器
        }
    }
};