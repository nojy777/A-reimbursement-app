package com.enyawevia.servlet;

import com.enyawevia.model.User;
import com.enyawevia.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RegistrationServlet extends HttpServlet {

    UserService userSvc = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();

        // collect data from request

        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String password =  req.getParameter("password");
        String role = req.getParameter("role");


        User user = new  User(firstName,lastName,email,password,role);

        // insert into the user
       String res= userSvc.registerUser(user);

        pw.write(res);





    }
}
