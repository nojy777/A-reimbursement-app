package com.enyawevia.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.enyawevia.models.BaseResponse;
import com.enyawevia.models.User;
import com.enyawevia.services.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RegistrationServlet extends HttpServlet {

    UserService userSvc = new UserService();
    private Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter pw = response.getWriter();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        User user = new User(firstName, lastName, email, password, role);

        BaseResponse resp = userSvc.registerUser(user);

        String baseResponseJson = gson.toJson(resp);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        pw.print(baseResponseJson);
        pw.flush();

    }
}
