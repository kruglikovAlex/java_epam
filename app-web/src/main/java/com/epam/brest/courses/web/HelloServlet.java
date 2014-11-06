package com.epam.brest.courses.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet extends HttpServlet{

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String parameter = req.getParameter("name");

        resp.setContentType("text/plain");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.print("Hello ");
            out.print(parameter);
        } catch (IOException e) {
            System.out.printf("ошибка создания PrintWriter",e);
        } finally {
            out.close();
        }

    }
}
