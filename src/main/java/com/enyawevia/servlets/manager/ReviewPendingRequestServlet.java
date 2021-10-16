package com.enyawevia.servlets.manager;

import com.google.gson.Gson;
import com.enyawevia.models.BaseResponse;
import com.enyawevia.services.ReimbursementService;
import com.enyawevia.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ReviewPendingRequestServlet extends HttpServlet {

    UserService userSvc = new UserService();
    private Gson gson = new Gson();
    ReimbursementService rSvc = new ReimbursementService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        BaseResponse baseResponse = new BaseResponse();

        // get token
        String token = req.getHeader("Authorization");

        // get request parameters
        int requestId = Integer.parseInt(req.getParameter("requestId"));
        String status = req.getParameter("status");

        boolean validToken = userSvc.validateToken(token);

        if(!validToken){
            baseResponse.setStatus(false);
            baseResponse.setMessage("Invalid token");
            String baseResponseJson = gson.toJson(baseResponse);
            pw.write(baseResponseJson);
            pw.flush();
        } else {
            // token is valid, proceed to get pending reimbursement requests
            BaseResponse res = rSvc.approveDenyRequest(requestId, status, token);
            String baseResponseJson = gson.toJson(res);
            pw.print(baseResponseJson);
            pw.flush();
        }


    }
}
