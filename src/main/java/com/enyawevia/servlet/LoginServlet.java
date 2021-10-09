package com.enyawevia.servlet;

import com.enyawevia.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {

    UserService userSvc = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        boolean res = userSvc.login(email, password);
        if(res) {
            pw.write("Login Successful");

        } else {
            pw.write("Login Failed");

        }

    }
}