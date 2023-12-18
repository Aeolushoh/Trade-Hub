package com.example.myapplication.dao;

import com.google.android.gms.common.api.Response;
import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class QiniuUploader {

    private static final String ACCESS_KEY = "_P3lfIUOK0o9-UoRX_-CKCTPgmVcHoyEiteXfebN";
    private static final String SECRET_KEY = "n-gOFtCUUGQBgLvurw8KpsGjNb-7y15RE4qqqna4";
    private static final String BUCKET_NAME = "tradehub";
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    static final String[] url = new String[1];



    public static String uploadImage( String filePath) {
        // 生成上传凭证
        String token = auth.uploadToken(BUCKET_NAME);

        // 创建 UploadManager
        UploadManager uploadManager = new UploadManager();
        CompletableFuture<String> future = new CompletableFuture<>();
        url[0]=null;
        // 上传文件
        // 用数组包裹以便在回调中修改值
        String key=filePath.substring(73);
        uploadManager.put(filePath, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            // 上传成功 pao
                            // 这里的 imageUrl 就是上传成功后的图片路径
                            // 可以将它保存起来或者使用它进行其他操作
                            try {
                                url[0] = "/" + response.getString("key");
                                future.complete(url[0]);
                            } catch (JSONException e) {
                                future.completeExceptionally(e);
                            }
                        } else {
                            future.completeExceptionally(new RuntimeException("Upload failed: " + info.error));
                        }
                    }
                }, new UploadOptions(null, null, false,
                        new UpProgressHandler() {
                            public void progress(String key, double percent) {
                                // 上传进度
                                System.out.println("Upload progress: " + percent);
                            }
                        }, null));
        return filePath.substring(73);// 返回上传成功后的 key
    }
}