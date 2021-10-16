package com.enyawevia.servlets;

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

public class ReimbursementServlet  extends HttpServlet {

    ReimbursementService rSvc = new ReimbursementService();
    UserService userSvc = new UserService();

    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        BaseResponse baseResponse = new BaseResponse();

        PrintWriter pw = resp.getWriter();
        String token = req.getHeader("Authorization");

        boolean validToken = userSvc.validateToken(token);

        if(!validToken){
            baseResponse.setStatus(false);
            baseResponse.setMessage("Invalid token");
            String baseResponseJson = gson.toJson(baseResponse);
            pw.write(baseResponseJson);
            pw.flush();
        } else {
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            double amount = Double.parseDouble(req.getParameter("amount"));

            BaseResponse res = rSvc.createReimbursement(title, description, amount, token);

            String baseResponseJson = gson.toJson(res);

            pw.print(baseResponseJson);
            pw.flush();
        }
    }

}
