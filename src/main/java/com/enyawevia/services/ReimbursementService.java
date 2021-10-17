package com.enyawevia.services;

import com.enyawevia.dto.PendingReimbursementResponse;
import com.enyawevia.dto.ResolvedReimbursementReponse;
import com.enyawevia.models.BaseResponse;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementService {

    ConnectionService connect = new ConnectionService();
    BaseResponse baseResponse = new BaseResponse();
    UserService userSvc = new UserService();

    public BaseResponse createReimbursement(String title, String description, double amount, String token) {
        String sql = "INSERT INTO reimbursements (employee_id, title, description, amount, created_at, status) " +
                "VALUES (?,?,?,?,?,?)";
        try {
            // get user ID
            int userId = userSvc.getUserId(token);

            Connection c = connect.establishConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setDouble(4, amount);
            ps.setDate(5, new Date(System.currentTimeMillis()));
            ps.setString(6, "PENDING");

            int res = ps.executeUpdate();
            if(res == 1) {
                baseResponse.setStatus(true);
                baseResponse.setMessage("Reimbursement Created Successfully");
                return baseResponse;
            } else {
                baseResponse.setStatus(true);
                baseResponse.setMessage("Failed to created reimbursement");
                return baseResponse;
            }



        } catch (Exception ex) {
            ex.printStackTrace();
            baseResponse.setStatus(true);
            baseResponse.setMessage("Error :: " + ex.getMessage());
            return baseResponse;
        }
    }

    public BaseResponse getPendingRequests(String token) {

        try {
            // get the user role
            String userRole = userSvc.getUserRole(token);

            // get user ID
            int userId = userSvc.getUserId(token);
            String sql = "";

            Connection c = connect.establishConnection();
            PreparedStatement ps = null;

            if(userRole.equals("EMPLOYEE")) {
                sql = "SELECT * FROM reimbursements WHERE employee_id = ? AND status = ?";
                ps = c.prepareStatement(sql);
                ps.setInt(1, userId);
                ps.setString(2, "PENDING");
            } else if(userRole.equals("MANAGER")) {
                sql = "SELECT * FROM reimbursements WHERE status = ?";
                ps = c.prepareStatement(sql);
                ps.setString(1, "PENDING");
            }

            ResultSet rs = ps.executeQuery();
            List<PendingReimbursementResponse> requestList = new ArrayList<>();
            // accounts found for user
            while(rs.next()) {
                PendingReimbursementResponse request = new PendingReimbursementResponse();
                request.setId(rs.getInt("id"));
                request.setTitle(rs.getString("title"));
                request.setDescription(rs.getString("description"));
                request.setAmount(rs.getDouble("amount"));
                request.setCreatedAt(rs.getDate("created_at"));
                request.setStatus(rs.getString("status"));
                requestList.add(request);
            }
            baseResponse.setStatus(true);
            baseResponse.setMessage("Pending Reimbursement Requests");
            baseResponse.setData(requestList);
            return baseResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            baseResponse.setStatus(true);
            baseResponse.setMessage("Error :: " + ex.getMessage());
            return baseResponse;
        }
    }

    public BaseResponse getResolvedRequests(String token) {

        try {
            String sql = "";
            PreparedStatement ps = null;

            // get user ID
            int userId = userSvc.getUserId(token);

            // get the user role
            String userRole = userSvc.getUserRole(token);

            Connection c = connect.establishConnection();

            if(userRole.equals("EMPLOYEE")) {
                sql = "SELECT * FROM reimbursements WHERE employee_id = ? AND (status = ? OR status = ?)";
                ps = c.prepareStatement(sql);
                ps.setInt(1, userId);
                ps.setString(2, "APPROVED");
                ps.setString(3, "DENIED");

            } else if(userRole.equals("MANAGER")) {
                sql = "SELECT * FROM reimbursements WHERE status = ? OR status = ?";
                ps = c.prepareStatement(sql);
                ps.setString(1, "APPROVED");
                ps.setString(2, "DENIED");
            }

            ResultSet rs = ps.executeQuery();
            List<ResolvedReimbursementReponse> requestList = new ArrayList<>();
            // accounts found for user
            while(rs.next()) {
                ResolvedReimbursementReponse request = new ResolvedReimbursementReponse();
                request.setTitle(rs.getString("title"));
                request.setDescription(rs.getString("description"));
                request.setAmount(rs.getDouble("amount"));
                request.setCreatedAt(rs.getDate("created_at"));
                request.setStatus(rs.getString("status"));
                request.setReviewedBy(rs.getString("reviewed_by"));
                request.setReviewedAt(rs.getDate("reviewed_at"));
                requestList.add(request);
            }
            baseResponse.setStatus(true);
            baseResponse.setMessage("Resolved Reimbursement Requests");
            baseResponse.setData(requestList);
            return baseResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            baseResponse.setStatus(true);
            baseResponse.setMessage("Error :: " + ex.getMessage());
            return baseResponse;
        }
    }

    public BaseResponse approveDenyRequest(int requestId, String status, String token) {
        String sql = "UPDATE reimbursements SET status = ?, reviewed_by = ?, reviewed_at = ?" +
                "WHERE id = ?";
        try {
            // get user ID
            int userId = userSvc.getUserId(token);

            // get manager name
            String managerName = userSvc.getUserName(token);

            // get role
            String role = userSvc.getUserRole(token);
            if(!role.equals("MANAGER")){
                baseResponse.setStatus(true);
                baseResponse.setMessage("You are not authorized to call this function");
                return baseResponse;
            }

            Connection c = connect.establishConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, managerName);
            ps.setDate(3, new Date(System.currentTimeMillis()));
            ps.setInt(4, requestId);

            int res = ps.executeUpdate();
            if(res == 1) {
                baseResponse.setStatus(true);
                baseResponse.setMessage("Reimbursement Request Updated Successfully");
                return baseResponse;
            }
            baseResponse.setStatus(true);
            baseResponse.setMessage("Failed to update Reimbursement Request");
            return baseResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            baseResponse.setStatus(true);
            baseResponse.setMessage("Error :: " + ex.getMessage());
            return baseResponse;
        }
    }

}
