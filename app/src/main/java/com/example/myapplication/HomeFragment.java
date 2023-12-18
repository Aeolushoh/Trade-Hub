package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.VolumeShaper;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Product;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.Configuration;
import java.util.List;


public class HomeFragment extends Fragment {

    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    UserDao userDao=new UserDao();
    RecyclerView recyclerView;
    EditText search_text;
    private YourAdapter adapter;
    private Intent intent1;
    int userid=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Button add=rootView.findViewById(R.id.add);
        intent1= getActivity().getIntent();

        String visible =intent1.getStringExtra("visible");
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        userid=myApplication.getSharedVariable();
        if(userid!=0) add.setVisibility(View.VISIBLE);
        // 初始化上传管理器
        UploadManager uploadManager = new UploadManager();

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView = rootView.findViewById(R.id.viewproduct);

        //设置下拉刷新图标的大小 只支持两种： DEFAULT  和 LARGE
        //swipeRefreshLayout.setSize(CircularProgressDrawable.LARGE);

        //设置刷新图标的颜色，在手指下滑刷新时使用第一个颜色，在刷新中，会一个个颜色进行切换 这里是传入 int... colors
        swipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE);

        //设置刷新图标的颜色, 在手指下滑刷新时使用第一个颜色，和 setColorSchemeColors 传递的参数不一样，这里是传入int colorResIds
        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.blue);

        //设置刷新图标的背景颜色
        //swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.blue);

        //设置动画样式下拉的起始点和结束点，scale设置是否需要放大或者缩小动画
        // 第一个参数：默认为false，设置为true，下拉过程中刷新图标就会从小变大
        // 第二个参数：起始位置，刷新图标距离顶部像素px
        // 第三个参数：结束位置，刷新图标距离顶部像素px
        //swipeRefreshLayout.setProgressViewOffset(false, 100, 200);

        //设置动画样式下拉的结束点  scale设置是否需要放大或者缩小动画
        // 第二个参数：结束位置，刷新图标距离顶部像素px
        //swipeRefreshLayout.setProgressViewEndTarget(false, 500);

        //设置可以将刷新指示器拉出其静止位置的距离（以像素为单位）
        //swipeRefreshLayout.setSlingshotDistance(600);


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
/*

        List<Product> productList = userDao.getProducts(); // 替换为你的商品数据
        YourAdapter adapter = new YourAdapter(productList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
*/
        add.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
               Intent intent = new Intent(getActivity(), ReleaseActivity.class);
               intent.putExtra("userid",0);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.allpro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Allproduct.class);
                intent.putExtra("userid",0);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchTask().execute();
            }
        });
        //分类按钮
        rootView.findViewById(R.id.imageView0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), class0.class);
                intent.putExtra("userid",0);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.imageView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), class1.class);
                intent.putExtra("userid",0);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), class2.class);
                intent.putExtra("userid",0);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), class3.class);
                intent.putExtra("userid",0);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), class4.class);
                intent.putExtra("userid",0);
                startActivity(intent);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));


        // 创建适配器并设置给RecyclerView

        new LoadDataTask().execute();

        return rootView;
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
            adapter = new YourAdapter(productlist,userid); // 你的数据模型可以是不同的
            recyclerView.setAdapter(adapter);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SearchTask extends AsyncTask<Void, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(Void... voids) {
            // 在后台线程中加载数据，可以执行耗时操作
            // 这里可以替换为你的数据加载逻辑
            View fragmentView=getView();
            if (fragmentView != null) {
                search_text=fragmentView.findViewById(R.id.search_text);
            }
            String searchtext=search_text.getText().toString();

            return userDao.getPro_name(searchtext);
        }

        @Override
        protected void onPostExecute(List<Product> productlist) {
            // 在主线程中更新UI，将加载的数据设置给适配器
            adapter = new YourAdapter(productlist,userid); // 你的数据模型可以是不同的
            recyclerView.setAdapter(adapter);
        }
    }
    // 模拟数据加载的方法
}
