package com.enyawevia.services;

import com.enyawevia.models.BaseResponse;
import com.enyawevia.models.TokenData;
import com.enyawevia.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    ConnectionService connect = new ConnectionService();

    public BaseResponse registerUser(User user) {

        BaseResponse baseResponse = new BaseResponse();

        // validate user input
        if(user.getFirstName() ==""|| user.getEmail()== ""|| user.getPassword() == "" || user.getRole() == "") {
            baseResponse.setStatus((false));
            baseResponse.setMessage("Please fill in all the fields in the form");
            return baseResponse;
        }
        String sql = "INSERT INTO users (first_name, last_name, email, password, role) VALUES (?,?,?,?,?)";

        try {
            Connection c = connect.establishConnection();

            // check for duplicate emails
            // check duplicate emails
            String dupEmailSql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement dupEmailPs = c.prepareStatement(dupEmailSql);
            dupEmailPs.setString(1, user.getEmail());
            ResultSet dupEmailRs = dupEmailPs.executeQuery();
            if(dupEmailRs.next()){
                baseResponse.setStatus(false);
                baseResponse.setMessage("Email is already taken by another user. Please use a different email address");
                return baseResponse;
            }

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole());

            int resp = ps.executeUpdate();

            if(resp == 1) {
                baseResponse.setStatus(true);
                baseResponse.setMessage("User Registered Successfully");
                return baseResponse;
            } else {
                baseResponse.setStatus(false);
                baseResponse.setMessage("Failed to Register user");
                return baseResponse;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            baseResponse.setStatus(false);
            baseResponse.setMessage("Failed to register user: " + ex.getMessage());
            return baseResponse;
        }
    }

    public BaseResponse login(String email, String password) {

        BaseResponse baseResponse = new BaseResponse();

        // validate user input
        if(email ==""|| password== "") {
            baseResponse.setStatus((false));
            baseResponse.setMessage("Please fill in all the fields in the form");
            return baseResponse;
        }

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try {
            Connection c = connect.establishConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
                String token = user.getId() + ":" + user.getRole();
                TokenData tData = new TokenData();
                tData.setToken(token);
                tData.setRole(getUserRole(token));
                baseResponse.setStatus(true);
                baseResponse.setMessage("Login Successful");
                baseResponse.setData(tData);
                return baseResponse;
            }
            baseResponse.setStatus(false);
            baseResponse.setMessage("Incorrect Credentials");
            return baseResponse;
        } catch (Exception ex) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("An error occurred: " + ex.getMessage());
            return baseResponse;
        }

    }

    public boolean validateToken(String token) {
        if(token == null){
            return false;
        }

        String[] tokenArr = token.split(":");
        if(tokenArr.length != 2){ // "a:b:c" "bananas"
            // we first check to see if our token has 2 values, separated by a colon
            // if it has more or less than 2, we return false
            return false;
        }

        // then we take a look at the first value, making sure it's numeric
        String idString = tokenArr[0];
        if(!idString.matches("^\\d+$")){ // if it does not match a number regular expression, we return false
            return false;
        }

        // then we look at the second value, making sure it matches a value in our enum
        String roleString = tokenArr[1];
        if(roleString.equals("EMPLOYEE") || roleString.equals("MANAGER")) {
            return true;
        }
        return false;
    }

    public int getUserId(String token) {
        String[] tokenArr = token.split(":");
        String userId = tokenArr[0];
        return Integer.parseInt(userId);
    }

    public String getUserRole(String token) {
        String[] tokenArr = token.split(":");
        String userRole = tokenArr[1];
        return userRole;
    }

    public String getUserName(String token) {
        String[] tokenArr = token.split(":");
        int userId = Integer.parseInt(tokenArr[0]);
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            Connection c = connect.establishConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
                return user.getFirstName() + " " + user.getLastName();
            }
            return null;
        } catch (Exception ex) {
            System.out.println("Failed to find user " + ex.getMessage());
            return null;
        }
    }

    public BaseResponse userProfile(String token) {
        BaseResponse baseResponse = new BaseResponse();
        String sql = "SELECT * FROM users WHERE id = ? LIMIT 1";
        try {
            // get user ID
            int userId = getUserId(token);
            Connection c = connect.establishConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
                baseResponse.setStatus(true);
                baseResponse.setMessage("User Profile");
                baseResponse.setData(user);
                return baseResponse;
            }
            baseResponse.setStatus(false);
            baseResponse.setMessage("Profile Not Found");
            return baseResponse;
        } catch (Exception ex) {
            baseResponse.setStatus(false);
            baseResponse.setMessage("An error occurred: " + ex.getMessage());
            return baseResponse;
        }
    }

    public BaseResponse getEmployees(String token) {

        BaseResponse baseResponse = new BaseResponse();

        try {
            String sql = "";
            PreparedStatement ps = null;

            // get user ID
            int userId = getUserId(token);

            // get the user role
            String userRole = getUserRole(token);
            if(!userRole.equals("MANAGER")){
                baseResponse.setStatus(true);
                baseResponse.setMessage("You are not authorized to call this function");
                return baseResponse;
            }

            Connection c = connect.establishConnection();

            sql = "SELECT * FROM users WHERE role = ?";
            ps = c.prepareStatement(sql);
            ps.setString(1, "EMPLOYEE");

            ResultSet rs = ps.executeQuery();
            List<User> userList = new ArrayList<>();
            // accounts found for user
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                userList.add(user);
            }
            baseResponse.setStatus(true);
            baseResponse.setMessage("All Employees");
            baseResponse.setData(userList);
            return baseResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            baseResponse.setStatus(true);
            baseResponse.setMessage("Error :: " + ex.getMessage());
            return baseResponse;
        }
    }
}
