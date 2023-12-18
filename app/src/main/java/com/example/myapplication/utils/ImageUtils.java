package com.example.myapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {

    public static Bitmap decodeFile(String filePath) {
        try {
            // 使用BitmapFactory解码图像文件
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888; // 配置Bitmap格式
            return BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
