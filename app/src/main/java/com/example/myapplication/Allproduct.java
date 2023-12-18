package com.example.myapplication;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Product;

import java.util.List;

public class Allproduct extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;

    UserDao userDao=new UserDao();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allproduct);


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.viewproduct);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE);
        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadDataTask().execute();
                // 模拟异步操作，延迟 2000 毫秒后停止刷新
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 停止刷新
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 100);
            }
        });


        // 创建适配器并设置给RecyclerView

        new LoadDataTask().execute();
    }
    @SuppressLint("StaticFieldLeak")
    class LoadDataTask extends AsyncTask<Void, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(Void... voids) {
            // 在后台线程中加载数据，可以执行耗时操作
            // 这里可以替换为你的数据加载逻辑
            return userDao.getProducts();
        }

        @Override
        protected void onPostExecute(List<Product> productlist) {
            // 在主线程中更新UI，将加载的数据设置给适配器
            YourAdapter adapter = new YourAdapter(productlist); // 你的数据模型可以是不同的
            recyclerView.setAdapter(adapter);
        }
    }
    public void back(View view){
        finish();
    }
}