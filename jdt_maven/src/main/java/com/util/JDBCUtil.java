package com.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/**
 * jdbc连接数据库类。
 * 手动连接数据库。
 * 通过Java操作数据库。
 * jdt平台本身不使用此Util.
 * 提供代码操作数据库的接口
 */
public class JDBCUtil {



    public static Connection getConnection(){
        String url="jdbc:postgresql://192.168.1.88:5432/jdt";
        String user="xinsight";
        String password = "root";
        try {
            //1.加载驱动
            Class.forName("org.postgresql.Driver");
            //2.获取连接
            try {
                Connection connection = DriverManager.getConnection(url, user, password);
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(ResultSet resultSet,PreparedStatement statement, Connection connection){
        try {
            if(resultSet != null){
                resultSet.close();
            }
            if(statement != null){
                statement.close();
            }
            if(connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession(){
        // 1.得到核心配置文件的文件流.
        InputStream is = null;
        try {
            is = Resources.getResourceAsStream("SqlMapConfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2.构建SqlSessionFactory工厂.
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
        // 3.根据工厂得到会话连接对象.
        SqlSession session = factory.openSession();
        return session;
    }
}
