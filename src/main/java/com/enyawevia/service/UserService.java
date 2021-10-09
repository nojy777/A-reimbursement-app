package com.enyawevia.service;

import com.enyawevia.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserService {

    ConnectionService connect = new ConnectionService();

    public  String registerUser(User user)   {

        String sql = "INSERT INTO users (first_name, last_name, email, password,role) VALUES(?,?,?,?,?)";

        try {
            Connection c = connect.establishConnection();

            // check duplicate emails
            String dupEmailSql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement dupEmailPs = c.prepareStatement(dupEmailSql);
            dupEmailPs.setString(1, user.getEmail());
            ResultSet dupEmailRs = dupEmailPs.executeQuery();
            if(dupEmailRs.next()){

                return  "Email is already taken by another user. Please use a different email address";
            }

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, user.getFirstName());
            ps.setString(2,user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4,user.getPassword());
            ps.setString(5,user.getRole());



            int res = ps.executeUpdate();
            if(res == 1) {
                return "User registered successfully";

            } else {
                return "Failed to register user";
            }

        }catch(Exception ex) {

        return "An error has occurred:" + ex.getMessage();
    }

  }
  public boolean login(String email, String password) {

      String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
      try {
          Connection c = connect.establishConnection();
          PreparedStatement ps = c.prepareStatement(sql);
          ps.setString(1, email);
          ps.setString(2, password);

          ResultSet rs = ps.executeQuery();
          if(rs.next()) {
              // set logged In User
//                LoggedInUser.setId(rs.getInt("id"));
//                LoggedInUser.setFirstName(rs.getString("first_name"));
//                LoggedInUser.setLastName(rs.getString("last_name"));
//                LoggedInUser.setEmail(rs.getString("email"));
              return true;
          }
          return false;
      } catch (Exception ex) {
          System.out.println("An error occurred: " + ex.getMessage());
          return false;
      }
  }





}