package com.enyawevia.servlets;

import com.google.gson.Gson;
import com.enyawevia.models.BaseResponse;
import com.enyawevia.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {

    UserService userSvc = new UserService();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter pw = resp.getWriter();

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        BaseResponse res = userSvc.login(email, password);
        String baseResponseJson = gson.toJson(res);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        pw.print(baseResponseJson);
        pw.flush();
    }
}
