package com.example.myapplication;// ProductAdapter.java

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.example.myapplication.dao.UserDao;
import com.example.myapplication.entity.Product;
import com.example.myapplication.entity.User;

import java.security.AccessController;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class YourAdapter extends RecyclerView.Adapter<YourAdapter.ViewHolder> {

    private  List<Product> productList ;

    private  int userid;
    Product product;
    // 构造函数接受上下文参数

    public YourAdapter(List<Product> productList,int userid) {
        this.productList = productList;
        this.userid=userid;
    }
    public YourAdapter(List<Product> productList) {
        this.productList = productList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_class;
        ImageView tv_photo;
        ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_class=itemView.findViewById(R.id.tv_class);
            tv_photo=itemView.findViewById(R.id.img_product);
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    // 确保位置有效
                    if (position != RecyclerView.NO_POSITION) {
                        // 获取相应位置的商品数据
                        product = productList.get(position);

                        try {
                            // 处理点击事件的代码
                            new InsertTask().execute();
                            Intent intent = new Intent(view.getContext(), DetailProduct.class);
                            intent.putExtra("name",product.getName());
                            intent.putExtra("price",product.getPrice());
                            intent.putExtra("account",product.getAccount());
                            intent.putExtra("photopath",product.getPhoto());
                            intent.putExtra("pclass",product.getPclass());
                            view.getContext().startActivity(intent);
                            // 例如，启动新的 Activity、执行数据库操作等
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("YourAdapter", "Error in onClick: " + e.getMessage());
                        }
                        // 启动新的活动（Activity）并传递商品数据
                    }
                }
            });
            tv_class.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    // 确保位置有效
                    if (position != RecyclerView.NO_POSITION) {
                        // 获取相应位置的商品数据
                        product = productList.get(position);

                        try {
                            // 处理点击事件的代码
                            new InsertTask().execute();
                            Intent intent = new Intent(view.getContext(), DetailProduct.class);
                            intent.putExtra("name",product.getName());
                            intent.putExtra("price",product.getPrice());
                            intent.putExtra("account",product.getAccount());
                            intent.putExtra("photopath",product.getPhoto());
                            intent.putExtra("pclass",product.getPclass());
                            view.getContext().startActivity(intent);
                            // 例如，启动新的 Activity、执行数据库操作等
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("YourAdapter", "Error in onClick: " + e.getMessage());
                        }
                    }
                }
            });
            tv_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    // 确保位置有效
                    if (position != RecyclerView.NO_POSITION) {
                        // 获取相应位置的商品数据
                        product = productList.get(position);

                        try {
                            // 处理点击事件的代码
                            new InsertTask().execute();
                            Intent intent = new Intent(view.getContext(), DetailProduct.class);
                            intent.putExtra("name",product.getName());
                            intent.putExtra("price",product.getPrice());
                            intent.putExtra("account",product.getAccount());
                            intent.putExtra("photopath",product.getPhoto());
                            intent.putExtra("pclass",product.getPclass());
                            view.getContext().startActivity(intent);
                            // 例如，启动新的 Activity、执行数据库操作等
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("YourAdapter", "Error in onClick: " + e.getMessage());
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tv_name.setText(product.getName());
        int pclass=product.getPclass();
        if(pclass==0){
            holder.tv_class.setText("电子设备");
        } else if (pclass == 1) {
            holder.tv_class.setText("学习用品");
        }else if(pclass==2){
            holder.tv_class.setText("食品生鲜");
        } else if (pclass == 3) {
            holder.tv_class.setText("IP周边");
        }else if(pclass==4){
            holder.tv_class.setText("生活用品");
        }
        // 拼接完整的图片 URL
        String imageUrl = "http://s4w1c26lc.hn-bkt.clouddn.com/" + product.getPhoto();;

        // 使用你的图片加载库或方法加载图片，这里使用 Glide 作为例子
        Glide.with(holder.tv_photo.getContext())
                .load(imageUrl)
                .into(holder.tv_photo);

    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        } else {
            return 0; // 或者根据实际情况返回其他合适的值
        }
    }
    // YourAdapter 类中的 InsertTask 内部类
/*
    class InsertTask extends AsyncTask<Void, Void, String> {

        private Context context;  // 将 context 作为实例变量

        // 构造函数接受 Context 参数
        public InsertTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                UserDao userDao = new UserDao();
                if (userid != 0 && !userDao.findview(userid, product.getId())) {
                    userDao.setview(userid, product.getId());
                }
            } catch (SQLException e) {
                Log.e("YourAdapter", "Error in doInBackground: " + e.getMessage());
            }
        }
    }
*/
    @SuppressLint("StaticFieldLeak")
    class InsertTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            // 在后台线程中加载数据，可以执行耗时操作
            // 这里可以替换为你的数据加载逻辑
            UserDao userDao=new UserDao();
            if(userid!=0&&!userDao.findview(userid,product.getId())){
                try {
                    userDao.setview(userid,product.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e("YourAdapter", "Error in InsertTask: " + e.getMessage());
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String productlist) {
            // 在主线程中更新UI，将加载的数据设置给适配器
        }
    }
}
