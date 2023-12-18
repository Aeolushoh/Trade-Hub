package com.example.myapplication.dao;

import com.example.myapplication.entity.Product;
import com.example.myapplication.entity.Msg;
import com.example.myapplication.utils.JDBCUtils;
import com.example.myapplication.entity.User;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author: yan
 * date: 2022.02.17
 * **/
public class UserDao {

    private static final String TAG = "mysql-myapplication-UserDao";

    /**
     * function: 登录
     */
    public User login(String userAccount, String password) {

        User user = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConn();
            String sql = "select * from user where myid = ? and password = ?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, userAccount);
                    ps.setString(2, password);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        //注意：下标是从1开始
                        int id = rs.getInt(1);
                        String userName = rs.getString(2);
                        String userMyid = rs.getString(3);
                        String userPassword = rs.getString(4);
                        String userXueyuan = rs.getString(5);
                        user = new User(id, userName, userMyid, userPassword, userXueyuan);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "异常findUser：" + e.getMessage());
            return null;
        } finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return user;
    }


    /**
     * function: 注册
     */
    public boolean register(User user) throws SQLException {
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        PreparedStatement ps = null;

        try {
            String sql = "insert into user(name,myid,password,xueyuan) values (?,?,?,?)";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    //将数据插入数据库
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getMyid());
                    ps.setString(3, user.getPassword());
                    ps.setString(4, user.getXueyuan());
                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    if (rs > 0)
                        return true;
                    else
                        return false;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常register：" + e.getMessage());
            return false;
        } finally {
            JDBCUtils.close(connection, ps, null);
        }
    }


    public boolean release(Product product) throws SQLException {
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO pro(name,account,price,class,photopath) VALUES (?,?,?,?,?)";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                if (ps != null) {
                    //将数据插入数据库
                    ps.setString(1, product.getName());
                    ps.setString(2, product.getAccount());
                    ps.setFloat(3, product.getPrice());
                    ps.setInt(4, product.getPclass());
                    ps.setString(5,product.getPhoto());
                    //处理图片
                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();

                    if (rs > 0){
                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                long generatedId = generatedKeys.getLong(1);
                                product.setId((int) generatedId);
                                System.out.println("Generated ID: " + generatedId);
                            } else {
                                System.out.println("No generated key obtained.");
                            }
                        }
                        return true;
                    }

                    else
                        return false;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常register：" + e.getMessage());
            return false;
        } finally {
            JDBCUtils.close(connection, ps, null);
        }
    }


    /**
     * function: 根据账号进行查找该用户是否存在
     */
    public User findUser(String userAccount) {

        // 根据数据库名称，建立连接

        User user = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConn();
            String sql = "SELECT * from user where myid=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, userAccount);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        //注意：下标是从1开始
                        int id = rs.getInt(1);
                        String userName = rs.getString(2);
                        String userMyid = rs.getString(3);
                        String userPassword = rs.getString(4);
                        String userXueyuan = rs.getString(5);
                        user = new User(id, userName, userMyid, userPassword, userXueyuan);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "异常findUser：" + e.getMessage());
            return null;
        } finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return user;
    }


    public List<Product> getProducts() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer, Product> map = new HashMap<>();
        List<Product> productlist = null;
        try {
            connection = JDBCUtils.getConn();
            String sql = "SELECT * FROM pro";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                productlist = new ArrayList<>();
                if (ps != null) {
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        //注意：下标是从1开
                        int id = rs.getInt(1);
                        String name = rs.getString("name");
                        String account = rs.getString("account");
                        int pclass = rs.getInt("class");
                        float price = rs.getFloat("price");
                        String photopath=rs.getString("photopath");
                        Product product = null;
                        product = new Product(id,name, account, pclass, price, photopath);
                        productlist.add(product);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "异常getProducts：" + e.getMessage());
            return null;
        } finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return productlist;
    }
    public List<Product> getPro_class(int pclass) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer, Product> map = new HashMap<>();
        List<Product> productlist = null;
        try {
            connection = JDBCUtils.getConn();
            String sql = "SELECT * FROM pro where class =?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                productlist = new ArrayList<>();
                if (ps != null) {
                    ps.setInt(1, pclass);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        //注意：下标是从1开
                        int id = rs.getInt(1);
                        String name = rs.getString("name");
                        String account = rs.getString("account");
                        float price = rs.getFloat("price");
                        String photopath=rs.getString("photopath");
                        Product product = null;
                        product = new Product(id,name, account, pclass, price, photopath);
                        productlist.add(product);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "异常getProducts：" + e.getMessage());
            return null;
        } finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return productlist;
    }
    public List<Product> getPro_name(String namelike) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer, Product> map = new HashMap<>();
        List<Product> productlist = null;
        try {
            connection = JDBCUtils.getConn();
            String sql = "SELECT * FROM pro where name like ? ";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                productlist = new ArrayList<>();
                if (ps != null) {
                    ps.setString(1, "%" + namelike + "%");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        //注意：下标是从1开
                        int id = rs.getInt(1);
                        String name = rs.getString("name");
                        String account = rs.getString("account");
                        float price = rs.getFloat("price");
                        int pclass = rs.getInt("class");
                        String photopath=rs.getString("photopath");
                        Product product = null;
                        product = new Product(id,name, account, pclass, price, photopath);
                        productlist.add(product);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "异常getProducts：" + e.getMessage());
            return null;
        } finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return productlist;
    }

    public void Deletepro(int proid) throws SQLException {
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        PreparedStatement ps = null;

        try {
            String sql = "DELETE FROM pro WHERE id = ?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    //将数据插入数据库
                    ps.setInt(1, proid);
                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常register：" + e.getMessage());
        } finally {
            JDBCUtils.close(connection, ps, null);
        }
    }

    public boolean Updatepro(int proid,Product product) throws SQLException {
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        PreparedStatement ps = null;

        try {
            String sql = "UPDATE pro SET class = ?, name = ?, price = ?, account = ?, photopath = ? WHERE id = ?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    //将数据插入数据库
                    ps.setInt(1,product.getPclass());
                    ps.setString(2,product.getName());
                    ps.setFloat(3,product.getPrice());
                    ps.setString(4,product.getAccount());
                    ps.setString(5,product.getPhoto());
                    ps.setInt(6,proid);
                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    if(rs>0) return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常register：" + e.getMessage());
            return false;
        } finally {
            JDBCUtils.close(connection, ps, null);
        }
        return false;
    }
    public void setview(int userid, int proid) throws SQLException {
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        PreparedStatement ps = null;

        try {
            String sql = "insert into view(userid,proid) values (?,?)";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    //将数据插入数据库
                    ps.setInt(1, userid);
                    ps.setInt(2, proid);
                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常register：" + e.getMessage());
        } finally {
            JDBCUtils.close(connection, ps, null);
        }
    }

    public boolean findview(int userid,int proid) {

        // 根据数据库名称，建立连接

        User user = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConn();
            String sql = "SELECT * from view where userid=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, userid);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        //注意：下标是从1开始
                        int proidd=rs.getInt("proid");
                        if(proidd==proid) return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "异常findUser：" + e.getMessage());
            return false;
        } finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return false;
    }

    public List<Product> getViewpro(int userid) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer, Product> map = new HashMap<>();
        List<Product> productlist = null;
        ArrayList<Integer> prolist = new ArrayList<>();
        try {
            connection = JDBCUtils.getConn();
            String sql = "SELECT * FROM view where userid = ?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                ps.setInt(1,userid);
                productlist = new ArrayList<>();
                rs = ps.executeQuery();
                while (rs.next()) {
                    //注意：下标是从1开
                    prolist.add(rs.getInt("proid"));
                }
            }
            String sql1 = "SELECT * FROM pro where id = ?";
            for(int i=0;i<prolist.size();i++){
                ps = connection.prepareStatement(sql1);
                ps.setInt(1, prolist.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                    //注意：下标是从1开
                    int id = rs.getInt(1);
                    String name = rs.getString("name");
                    String account = rs.getString("account");
                    float price = rs.getFloat("price");
                    int pclass=rs.getInt("class");
                    String photopath=rs.getString("photopath");
                    Product product = null;
                    product = new Product(id,name, account, pclass, price, photopath);
                    productlist.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "异常getProducts：" + e.getMessage());
            return null;
        } finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return productlist;
    }

    public void setrelease(int userid, int proid) throws SQLException {
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        PreparedStatement ps = null;

        try {
            String sql = "insert into res(userid,proid) values (?,?)";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    //将数据插入数据库
                    ps.setInt(1, userid);
                    ps.setInt(2, proid);
                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常register：" + e.getMessage());
        } finally {
            JDBCUtils.close(connection, ps, null);
        }
    }

    public boolean findrelease(int userid,int proid) {

        // 根据数据库名称，建立连接

        User user = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConn();
            String sql = "SELECT * from res where userid=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, userid);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        //注意：下标是从1开始
                        int proidd=rs.getInt("proid");
                        if(proidd==proid) return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "异常findUser：" + e.getMessage());
            return false;
        } finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return false;
    }

    public List<Product> getReleasepro(int userid) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer, Product> map = new HashMap<>();
        List<Product> productlist = null;
        ArrayList<Integer> prolist = new ArrayList<>();
        try {
            connection = JDBCUtils.getConn();
            String sql = "SELECT * FROM res where userid = ?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                ps.setInt(1,userid);
                productlist = new ArrayList<>();
                rs = ps.executeQuery();
                while (rs.next()) {
                    //注意：下标是从1开
                    prolist.add(rs.getInt("proid"));
                }
            }
            String sql1 = "SELECT * FROM pro where id = ?";
            for(int i=0;i<prolist.size();i++){
                ps = connection.prepareStatement(sql1);
                ps.setInt(1, prolist.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                    //注意：下标是从1开
                    int id = rs.getInt(1);
                    String name = rs.getString("name");
                    String account = rs.getString("account");
                    float price = rs.getFloat("price");
                    int pclass=rs.getInt("class");
                    String photopath=rs.getString("photopath");
                    Product product = null;
                    product = new Product(id,name, account, pclass, price, photopath);
                    productlist.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "异常getProducts：" + e.getMessage());
            return null;
        } finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return productlist;
    }

    public int send_msg(String xh,String msg,String from) throws SQLException {
        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = JDBCUtils.getConn();

        PreparedStatement ps = null;

        try {
            String sql = "insert into msg(myid,msg,fro) values (?,?,?)";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                if (ps != null) {
                    //将数据插入数据库
                    ps.setString(1, xh);
                    ps.setString(2, msg);
                    ps.setString(3,from);
                    // 执行sql查询语句并返回结果集
                    int rs = ps.executeUpdate();
                    if(rs>0){
                        return 1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "异常register：" + e.getMessage());
            return 0;
        } finally {
            JDBCUtils.close(connection, ps, null);
        }
        return 0;
    }
    public List<Msg> getmsg(String myid) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer, Product> map = new HashMap<>();
        List<Msg> msglist = null;
        try {
            connection = JDBCUtils.getConn();
            String sql = "SELECT * FROM msg where myid = ? ";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                msglist = new ArrayList<>();
                if (ps != null) {
                    ps.setString(1, myid);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        //注意：下标是从1开
                        int id = rs.getInt(1);
                        String from = rs.getString("fro");
                        String tmsg = rs.getString("msg");
                        Msg msg = null;
                        msg = new Msg(tmsg,from,id);
                        msglist.add(msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "异常getProducts：" + e.getMessage());
            return null;
        } finally {
            JDBCUtils.close(connection, ps, rs);
        }
        return msglist;
    }
}
