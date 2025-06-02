package com.example.defter.Data;

import com.example.defter.Model.Medaxil;
import com.example.defter.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogData {
    private Connection connection;
    private PreparedStatement insertUser;
    private PreparedStatement updateUser;
    private static final String DB_NAME="Log.db";

    private static final String DB_PATH="jdbc:sqlite:/Users/muhammadfeyzullayev/Documents/Huseynxan/";
    private static final String TABLE_LOGIN="Login";
    private static final String COLUMN_LOGIN_ID="id";
    private static final String COLUMN_LOGIN_USER="user";
    private static final String COLUMN_LOGIN_PASSWORD="password";
    private static final String QUERY_LOGIN="SELECT * FROM " + TABLE_LOGIN;
    private static final String INSERT_USER="INSERT INTO " + TABLE_LOGIN + " ( " + COLUMN_LOGIN_USER  + " , " + COLUMN_LOGIN_PASSWORD +" ) VALUES(?,?)";
    private static final String UPDATE_USER="UPDATE " + TABLE_LOGIN + " SET " + COLUMN_LOGIN_USER + " = ? , " + COLUMN_LOGIN_PASSWORD + " = ? WHERE " +
            COLUMN_LOGIN_ID + " = ?";


    private static final String CONNECTION_STRING=DB_PATH+DB_NAME;
    public void open(){
        try {
            connection= DriverManager.getConnection(CONNECTION_STRING);
            insertUser=connection.prepareStatement(INSERT_USER);
            updateUser = connection.prepareStatement(UPDATE_USER);
            System.out.println("Connection succesfull");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<User> queryLogin(){
        try (Statement statement = connection.createStatement();
             ResultSet resultSet= statement.executeQuery(QUERY_LOGIN)){
            List<User> userList = new ArrayList<>();
            while (resultSet.next()){
                User user = new User(resultSet.getInt(1),resultSet.getString(2),
                        resultSet.getString(3));
                userList.add(user);
            }
            return userList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void insertUser(String user,String passord){
        try {
            insertUser.setString(1,user);
            insertUser.setString(2,passord);
            insertUser.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void updateUser(int nr,String user,String password){
        try {
            updateUser.setString(1,user);
            updateUser.setString(2,password);
            updateUser.setInt(3,nr);
            updateUser.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void close(){
        try {
            if (connection!=null){
                connection.close();
            }if (insertUser!=null){
                insertUser.close();
            }if (updateUser!=null){
                updateUser.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
