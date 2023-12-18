package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MineFragment extends Fragment {



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mine, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);


        String username = preferences.getString("username", "默认值");
        MyApplication myApplication=(MyApplication) getActivity().getApplication();
        if(myApplication.getnameVariable()!=null){
            TextView minename=rootView.findViewById(R.id.mine_name);
            minename.setText(myApplication.getnameVariable());
        }
        if(myApplication.getxueyuanVariable()!=null){
            TextView minexueyuan=rootView.findViewById(R.id.mine_xueyuan);
            minexueyuan.setText(myApplication.getxueyuanVariable());
        }

        rootView.findViewById(R.id.login1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View fragmentView=getView();
                TextView minename=fragmentView.findViewById(R.id.mine_name);
                String name=myApplication.getnameVariable();
                if(name!=null){
                    Toast.makeText(rootView.getContext(),"您已登录！",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        rootView.findViewById(R.id.myrelease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View fragmentView=getView();
                TextView minename=fragmentView.findViewById(R.id.mine_name);
                String name=myApplication.getnameVariable() ;
                if(name==null){
                    Toast.makeText(rootView.getContext(),"您未登录！",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent=new Intent(getActivity(),MyreleaseActivity.class);
                    startActivity(intent);
                }

            }
        });
        rootView.findViewById(R.id.myview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View fragmentView=getView();
                TextView minename=fragmentView.findViewById(R.id.mine_name);
                String name=myApplication.getnameVariable();
                if(name==null){
                    Toast.makeText(rootView.getContext(),"您未登录！",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent=new Intent(getActivity(),MyviewActivity.class);
                    startActivity(intent);
                }

            }
        });
        rootView.findViewById(R.id.outlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View fragmentView=getView();
                TextView minename=fragmentView.findViewById(R.id.mine_name);
                String name=myApplication.getnameVariable();
                if(name==null){
                    Toast.makeText(rootView.getContext(),"您未登录！",Toast.LENGTH_LONG).show();
                }else{
                    // 获取FragmentManager
                    TextView minexueyuan=fragmentView.findViewById(R.id.mine_xueyuan);
                    MyApplication myApplication = (MyApplication) getActivity().getApplication();
                    myApplication.setSharedVariable(0);
                    myApplication.setmyidVariable(null);
                    myApplication.setxueyuanVariable(null);
                    myApplication.setnameVariable(null);
                    minename.setText(myApplication.getnameVariable());
                    minexueyuan.setText(myApplication.getxueyuanVariable());
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isLoggedIn", false);
                    editor.putInt("userid", 0);
                    editor.putString("username", null);
                    editor.putString("myid", null);
                    editor.putString("xueyuan", null);
                    editor.apply();
                    Toast.makeText(rootView.getContext(),"您已退出！",Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View fragmentView=getView();
        MyApplication myApplication=(MyApplication) getActivity().getApplication();
        TextView minename=fragmentView.findViewById(R.id.mine_name);
        TextView minexueyuan=fragmentView.findViewById(R.id.mine_xueyuan);
        Intent intent= getActivity().getIntent();
        minename.setText(myApplication.getnameVariable());
        minexueyuan.setText(myApplication.getxueyuanVariable());
    }
}