package com.example.myapplication;

import static com.example.myapplication.dao.QiniuUploader.uploadImage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Msg;
import com.example.myapplication.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


public class MessageFragment extends Fragment {
    EditText xh;
    EditText msg;
    String name=null;

    String myid=null;

    private msgAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootview=inflater.inflate(R.layout.fragment_message, container, false);

        EditText xh=rootview.findViewById(R.id.input_xh);
        EditText msg=rootview.findViewById(R.id.input_text);
        MyApplication myApplication = (MyApplication) requireActivity().getApplication();
        name=myApplication.getnameVariable();
        myid=myApplication.getmyidVariable();
        recyclerView = rootview.findViewById(R.id.msg_recycler);
        swipeRefreshLayout = rootview.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE);

        //设置刷新图标的颜色, 在手指下滑刷新时使用第一个颜色，和 setColorSchemeColors 传递的参数不一样，这里是传入int colorResIds
        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.blue);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new InputTask().execute();
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

        int verticalSpaceHeight = getResources().getDimensionPixelSize(R.dimen.vertical_space);
        VerticalSpaceItemDecoration itemDecoration = new VerticalSpaceItemDecoration(verticalSpaceHeight);
// 设置RecyclerView
        if(name!=null){
            recyclerView.setLayoutManager(new LinearLayoutManager(rootview.getContext()));
        }
// 添加间隔
        recyclerView.addItemDecoration(itemDecoration);

        rootview.findViewById(R.id.send_msg).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(xh.length()==14&&msg.length()!=0&&myid!=null) {
                   new Thread(){
                       @Override
                       public void run() {
                           UserDao userDao = new UserDao();
                           int flag = 0;
                           User uu = userDao.findUser(xh.getText().toString());
                           if(uu==null){
                               flag=2;
                           }
                           else {
                               try {
                                   flag = userDao.send_msg(xh.getText().toString(),msg.getText().toString(),name);
                               } catch (SQLException e) {
                                   throw new RuntimeException(e);
                               }
                           }
                           new InputTask().execute();
                           hand1.sendEmptyMessage(flag);
                       }
                   }.start();
               }else{
                   Toast.makeText(rootview.getContext(),"信息输入错误或还未登录！",Toast.LENGTH_LONG).show();
               }
           }
       });
       if(myid!=null) new InputTask().execute();
       return rootview;
    }
    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler() {
    @Override
    public void handleMessage(Message msg) {
            if (msg.what == 0){
            Toast.makeText((getContext()), "发送失败!", Toast.LENGTH_LONG).show();
            } else if (msg.what == 1) {
            Toast.makeText(getContext(), "发送成功！", Toast.LENGTH_LONG).show();
            }else if(msg.what==2){
                Toast.makeText(getContext(), "该用户不存在！", Toast.LENGTH_LONG).show();
            }
            }
            };
    @SuppressLint("StaticFieldLeak")
    class InputTask extends AsyncTask<Void, Void, List<Msg>> {

        @Override
        protected List<Msg> doInBackground(Void... voids) {
            // 在后台线程中加载数据，可以执行耗时操作
            // 这里可以替换为你的数据加载逻辑
            UserDao userdao =new UserDao();
            return  userdao.getmsg(myid);
        }
        @Override
        protected void onPostExecute(List<Msg> msglist) {
            // 在主线程中更新UI，将加载的数据设置给适配器
            adapter = new msgAdapter(msglist); // 你的数据模型可以是不同的
            recyclerView.setAdapter(adapter);
        }
    }
}