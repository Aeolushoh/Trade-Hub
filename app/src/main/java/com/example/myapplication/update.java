package com.example.myapplication;

import static com.example.myapplication.dao.QiniuUploader.uploadImage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class update extends AppCompatActivity {

    //调取系统摄像头的请求码
    int proid=0;
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
    EditText tv_name;
    EditText tv_account;
    TextView tv_class;
    EditText tv_price;
    ImageView tv_photo;
    @SuppressLint({"WrongViewCast", "MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String account=intent.getStringExtra("account");
        String photopath=intent.getStringExtra("photopath");
        int pclass=intent.getIntExtra("pclass",0);
        float price=intent.getFloatExtra("price",0);
        proid=intent.getIntExtra("proid",0);

        tv_name =findViewById(R.id.pname_up);
        tv_account = findViewById(R.id.account_up);
        tv_price=findViewById(R.id.price_up);
        tv_class=findViewById(R.id.Pclass_up);
        tv_photo=findViewById(R.id.photo_up);
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
        tv_photo.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        }));
    }
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
                        tv_class.setText(classArr[pclass]);
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
        Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null){
            Uri selectedImageUri=data.getData();
            Glide.with(this)
                    .load(selectedImageUri)
                    .into(tv_photo);
        }
    }

    public void update(View view){

        String cname = tv_name.getText().toString();
        String caccount = tv_account.getText().toString();
        float  cprice= Float.parseFloat(tv_price.getText().toString());

        tv_photo.setDrawingCacheEnabled(true);
        tv_photo.buildDrawingCache();
        Bitmap cphoto=tv_photo.getDrawingCache();
        photopath1=saveImageToStorage(cphoto);
        photop= uploadImage(photopath1);


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
                boolean flag=false;
                try {
                    flag = userDao.Updatepro(proid,product);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if(flag){
                    msg=1;
                    Intent intent=new Intent(update.this,MyreleaseActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_LONG).show();
            }else if(msg.what==1){
                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_LONG).show();
            }
        }
    };
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
}